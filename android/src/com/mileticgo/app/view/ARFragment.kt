package com.mileticgo.app.view

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.TransformableNode
import com.mileticgo.app.CityPin
import com.mileticgo.app.R
import com.mileticgo.app.ar.PlaceNode
import com.mileticgo.app.ar.PlacesArFragment
import com.mileticgo.app.databinding.FragmentArBinding
import com.mileticgo.app.model.Geometry
import com.mileticgo.app.model.GeometryLocation
import com.mileticgo.app.model.Place
import com.mileticgo.app.model.getPositionVector
import com.mileticgo.app.view_model.ARViewModel
import java.util.*

class ARFragment  : Fragment(), SensorEventListener {

    private var cityPin: CityPin? = null
    private lateinit var arFragment: PlacesArFragment
    private lateinit var binding : FragmentArBinding
    private lateinit var sensorManager: SensorManager
    private var anchorNode: AnchorNode? = null
    private val orientationAngles = FloatArray(3)
    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)
    private val rotationMatrix = FloatArray(9)

    var modelRenderable: ModelRenderable? = null

    private val arViewModel by viewModels<ARViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ar, container, false)

        sensorManager = requireActivity().getSystemService()!!

        arFragment = childFragmentManager.findFragmentById(R.id.ux_fragment) as PlacesArFragment

        if (arguments?.getSerializable("details") != null) {
            cityPin = arguments?.getSerializable("details") as CityPin
            println("##### AR FRAGMENT result - $cityPin")
        }

        //setupModel()
        //setupPlane()

        binding.myToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    /*private fun setupPlane() {
        arFragment.setOnTapArPlaneListener { hitresult, plane, motionEvent ->
            val anchor = hitresult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)
            createModel(anchorNode)
        }
    }*/

    /*private fun createModel(anchorNode: AnchorNode) {
        val node = TransformableNode(arFragment.transformationSystem).apply {
            setOnTapListener{_, _ ->
                println("##### node touch listener")
            }
        }
        node.setParent(anchorNode)
        node.renderable = modelRenderable
        node.scaleController
        node.select()
    }*/

    /*private fun setupModel() {
        //println("###### PARSE URI ${Uri.parse("wolves.sfb")}")
        ModelRenderable.builder()
            .setSource(context, R.raw.android) //Uri.parse("wolves.sfb")
            .build()
            .thenAccept { renderable -> modelRenderable = renderable }
            .exceptionally {
                Toast.makeText(context, "Model can't be loaded", Toast.LENGTH_SHORT).show()
                return@exceptionally null
            }
    }*/

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }*/

    override fun onResume() {
        super.onResume()
        //println("##### onResume")
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
        setupAR()
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    private fun setupAR() {
        val session: Session? = arFragment.arSceneView.session

        if (session != null) {
            val filter = CameraConfigFilter(session)
            // Return only camera configs that target 30 fps camera capture frame rate.
            filter.targetFps = EnumSet.of(CameraConfig.TargetFps.TARGET_FPS_30)
            // Return only camera configs that will not use the depth sensor.
            filter.depthSensorUsage = EnumSet.of(CameraConfig.DepthSensorUsage.DO_NOT_USE)
            // Get list of configs that match filter settings.
            // In this case, this list is guaranteed to contain at least one element,
            // because both TargetFps.TARGET_FPS_30 and DepthSensorUsage.DO_NOT_USE
            // are supported on all ARCore supported devices.
            val cameraConfigList = session.getSupportedCameraConfigs(filter)
            // Use element 0 from the list of returned camera configs. This is because
            // it contains the camera config that best matches the specified filter settings.
            session.cameraConfig = cameraConfigList!![0]
        }

        val pos = floatArrayOf(0F, 0.05F, -1f)
        val rotation = floatArrayOf(0f, 0f, 0f, 1f)
        //val anchor: Anchor? = session?.createAnchor(Pose(pos, rotation))
        val anchor: Anchor? = session?.createAnchor(arFragment.arSceneView.arFrame?.camera?.pose
            ?.compose(Pose.makeTranslation(50f, 50f ,0f))
            ?.extractTranslation())
        anchorNode = AnchorNode(anchor)
        anchorNode?.setParent(arFragment.arSceneView.scene)
        addPlaces(anchorNode!!)

        /*arFragment.setOnTapArPlaneListener { hitResult, _, _ ->
            // Create anchor
            val anchor = hitResult.createAnchor()
            anchorNode = AnchorNode(anchor)
            anchorNode?.setParent(arFragment.arSceneView.scene)
            addPlaces(anchorNode!!)
        }*/

        //Add an Anchor and a renderable in front of the camera
        /*val session: Session = Session(context)  // arFragment.arSceneView.session
        arFragment.arSceneView.setupSession(session)
        println("#### session ${session}")
        val pos = floatArrayOf(0f, 0f, -1f)
        val rotation = floatArrayOf(0f, 0f, 0f, 1f)
        session.resume()
        val anchor: Anchor = session.createAnchor(Pose(pos, rotation))
        anchorNode = AnchorNode(anchor)
        //anchorNode!!.renderable = andyRenderable
        anchorNode!!.setParent(arFragment.arSceneView.scene)
        addPlaces(anchorNode!!)*/

    }

    private fun addPlaces(anchorNode: AnchorNode) {
        // Add the place in AR
        val place = Place(Geometry(GeometryLocation(cityPin!!.lat, cityPin!!.lng)))
        val placeNode = PlaceNode(requireContext(), place).apply {
            val bundle = Bundle()
            bundle.putSerializable("details", cityPin)
            setOnTapListener { hitTestResult, _ ->
                println("##### add place on tap listener")
                Navigation.findNavController(binding.root).navigate(R.id.action_ARFragment_to_placeDetailsFragment, bundle)
            }
        }
        placeNode.setParent(anchorNode)
        println("###### get position vector ${place.getPositionVector(orientationAngles[0], place.geometry.location.latLng)}")
        placeNode.localPosition = Vector3(0f, 0f, -8f)
        //placeNode.localPosition = place.getPositionVector(orientationAngles[0], place.geometry.location.latLng)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) {
            return
        }
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
        }

        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading
        )
        SensorManager.getOrientation(rotationMatrix, orientationAngles)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}
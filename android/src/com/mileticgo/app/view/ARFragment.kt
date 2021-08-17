package com.mileticgo.app.view

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.ar.core.Anchor
import com.google.ar.core.Pose
import com.google.ar.core.Session
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.TransformableNode
import com.mileticgo.app.R
import com.mileticgo.app.ar.PlaceNode
import com.mileticgo.app.ar.PlacesArFragment
import com.mileticgo.app.databinding.FragmentArBinding
import com.mileticgo.app.model.Place
import com.mileticgo.app.model.getPositionVector
import com.mileticgo.app.view_model.ARViewModel
import com.mileticgo.app.view_model.MapViewModel


class ARFragment  : Fragment(), SensorEventListener {

    private var place: Place? = null
    private lateinit var arFragment: PlacesArFragment
    private lateinit var binding : FragmentArBinding
    private val mapViewModel by viewModels<MapViewModel>()
    private var anchorNode: AnchorNode? = null
    private val orientationAngles = FloatArray(3)
    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)
    private val rotationMatrix = FloatArray(9)

    private val model_URL = "https://modelviewer.dev/shared-assets/models/Astronaut.glb"

    var modelRenderable: ModelRenderable? = null

    private val arViewModel by viewModels<ARViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ar, container, false)

        arFragment = childFragmentManager.findFragmentById(R.id.ux_fragment) as PlacesArFragment

        if (arguments?.getSerializable("location_data") != null) {
            place = arguments?.getSerializable("location_data") as Place
            println("##### AR FRAGMENT result - $place")
        }

        //setupModel()
        //setupPlane()
        return binding.root
    }

    private fun setupPlane() {
        arFragment.setOnTapArPlaneListener { hitresult, plane, motionEvent ->
            val anchor = hitresult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)
            createModel(anchorNode)
        }
    }

    private fun createModel(anchorNode: AnchorNode) {
        val node = TransformableNode(arFragment.transformationSystem).apply {
            setOnTapListener{_, _ ->
                println("##### node touch listener")
            }
        }
        node.setParent(anchorNode)
        node.renderable = modelRenderable
        node.scaleController
        node.select()
    }

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
        setupAR()
    }

    private fun setupAR() {
        arFragment.setOnTapArPlaneListener { hitResult, _, _ ->
            // Create anchor
            val anchor = hitResult.createAnchor()
            anchorNode = AnchorNode(anchor)
            anchorNode?.setParent(arFragment.arSceneView.scene)
            addPlaces(anchorNode!!)
        }
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
        val placeNode = PlaceNode(requireContext(), place).apply {
            setOnTapListener { hitTestResult, motionEvent ->
                println("##### add place on tap listener")
                Navigation.findNavController(binding.root).navigate(R.id.action_ARFragment_to_placeDetailsFragment)
            }
        }
        placeNode.setParent(anchorNode)
        placeNode.localPosition = place!!.getPositionVector(orientationAngles[0])

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
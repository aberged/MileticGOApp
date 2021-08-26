package com.mileticgo.app.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.ar.sceneform.AnchorNode
import com.mileticgo.app.R
import com.mileticgo.app.api.PlacesService
import com.mileticgo.app.ar.PlacesArFragment
import com.mileticgo.app.databinding.FragmentMapBinding
import com.mileticgo.app.model.Place
import com.mileticgo.app.view_model.MapViewModel

class MapsFragment : Fragment() {

    private lateinit var binding : FragmentMapBinding

    private lateinit var placesService: PlacesService
    private lateinit var arFragment: PlacesArFragment
    private lateinit var mapFragment: SupportMapFragment
    private val mapViewModel by viewModels<MapViewModel>()

    // Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Sensor
    private lateinit var sensorManager: SensorManager

    private var anchorNode: AnchorNode? = null
    private var markers: MutableList<Marker> = emptyList<Marker>().toMutableList()
    //private var places: List<Place>? = null
    private var currentLocation: Location? = null
    private lateinit var map: GoogleMap

    private var locationPermission = false

    //private lateinit var locationRequest : LocationRequest
    private lateinit var locationCallback : LocationCallback

    private var isArrSupported = false

    val Location.latLng: LatLng
        get() = LatLng(this.latitude, this.longitude)

    val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        //println("##### isGranted = $isGranted")
        locationPermission = isGranted
        if (isGranted) {
            setUpMaps()
        } else {
            //todo location permission is not granted
            Navigation.findNavController(binding.root).popBackStack()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //arFragment = childFragmentManager.findFragmentById(R.id.ar_fragment) as PlacesArFragment

        mapFragment = (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!
        //mapFragment.getMapAsync(callback)

        sensorManager = requireActivity().getSystemService()!!
        placesService = PlacesService.create()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        /*locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 20 * 1000*/

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                //println("##### onLocationResult locationResult - ${locationResult.locations}")
                super.onLocationResult(locationResult)
                currentLocation = locationResult.lastLocation

            }

            override fun onLocationAvailability(p0: LocationAvailability) {
                //println("##### onLocationAvailability - $p0")
                super.onLocationAvailability(p0)
            }
        }
        //check if ar is supported
        isArrSupported = isARSupported()
        //setUpMaps()
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermission = true
            setUpMaps()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    private fun setUpMaps() {
        getLastLocation()

        mapFragment.getMapAsync { googleMap ->
            map = googleMap
            map.isMyLocationEnabled = true
            //println("###### setUpMaps - current location - $currentLocation")
            val miletic = LatLng(45.2550458, 19.8447484) //todo we should get marker location from server
            map.addMarker(MarkerOptions().position(miletic).title("spomenik sivom тићу Милетићу"))
            map.setMinZoomPreference(12.0F)

            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style_json))

            /*googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(
                currentLocation!!.latLng, 13f)))*/

            /*getCurrentLocation {
                val pos = CameraPosition.fromLatLngZoom(it.latLng, 13f)
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos))
            }*/


            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(miletic))

            if (!isArrSupported) { //todo proveri da li je vec prikazan dijalog sa obavestenjem
                requireContext().oneButtonDialog(null, getString(R.string.ar_not_supported), getString(R.string.ok))
            }

            googleMap.setOnInfoWindowClickListener {

                val infoText = "Grickao si klitoris Lepi Mario\n" +
                        "Dobio si sifilis Lepi Mario\n" +
                        "Jebi se sada ti\n" +
                        " \n" +
                        "Puno sperme rukometno igralište\n" +
                        "Moja guzica bogato nalazište\n" +
                        "Evo ti moj probušeni kišobran\n" +
                        "Evo ti moj uzavreli jorgovan\n" +
                        " \n" +
                        "Novi Zeland ima šume\n" +
                        "a Jamajka uzgaja pume\n" +
                        "Patike Adidas\n"
                val place = currentLocation?.let { it1 -> Place(currentLocation!!.latitude, currentLocation!!.longitude, it1.altitude) }
                //println("####### SEND PLACE $place")

                val bundle = Bundle()
                bundle.putSerializable("location_data", place)
                if (isArrSupported) {
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_mapFragment_to_arFragment, bundle)
                } else {
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_mapFragment_to_placeDetailsFragment)
                }
            }
        }
    }

    /*private fun getCurrentLocation(onSuccess: (Location) -> Unit) {
        try {
            println("###### getCurrentLocation try")
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper()).addOnSuccessListener {
                println("###### addOnSuccessListener - $it")
            }.addOnFailureListener {
                println("###### addOnFailureListener exception - ${it.message}")
            }
        } catch (e: Exception) {
            println("###### getCurrentLocation Exception - ${e.message}")
        }

        *//*fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                println("##### current location - $currentLocation")
                onSuccess(location)
            }
        }.addOnFailureListener {
            println("###### current location exception - ${it.message}")
        }*//*
    }*/

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        //println("####### isLocationEnabled() ${isLocationEnabled()} ")
        if (isLocationEnabled()) {
            fusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                val location : Location? = task.result
                if (location == null) {
                    requestNewLocationData()
                } else {
                    currentLocation = location
                    map.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(
                        currentLocation!!.latLng, 12f)))
                }
            }
        } else {
            Toast.makeText(requireContext(), "Ukljucite lokaciju", Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        //println("##### requestNewLocationData")
        val locationRequest = LocationRequest.create().apply {
            this.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            this.interval = 0
            this.fastestInterval = 0
            this.numUpdates = 1
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun isLocationEnabled() : Boolean {
        val locationManager : LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    override fun onResume() {
        super.onResume()
        if (isLocationEnabled()) {
            checkPermissions()
        } else {
            Toast.makeText(requireContext(), "Morate ukljuciti lokaciju da bi ste koristili aplikaciju", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(binding.root).popBackStack()
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun isARSupported() : Boolean {
        val sharedPreferences = requireActivity().getPreferences(MODE_PRIVATE)
        return sharedPreferences.getBoolean(getString(R.string.ar_supported_flag), false)
    }
}
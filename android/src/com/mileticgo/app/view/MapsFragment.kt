package com.mileticgo.app.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.mileticgo.app.AndroidApplication
import com.mileticgo.app.CityPin
import com.mileticgo.app.R
import com.mileticgo.app.databinding.FragmentMapBinding
import com.mileticgo.app.utils.SharedPrefs
import com.mileticgo.app.view_model.MapViewModel
import java.util.concurrent.TimeUnit

class MapsFragment : Fragment() {

    private var pins: MutableList<CityPin>? = null
    private lateinit var binding: FragmentMapBinding
    private lateinit var mapFragment: SupportMapFragment
    private val mapViewModel by viewModels<MapViewModel>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null
    private lateinit var map: GoogleMap
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var isArrSupported = false

    private val Location.latLng: LatLng
        get() = LatLng(this.latitude, this.longitude)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)

        binding.myToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        pins = (activity?.application as AndroidApplication).repository.activeCityPins
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapFragment = (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(60)

            // Sets the fastest rate for active location updates. This interval is exact, and your
            // application will never receive updates more frequently than this value.
            fastestInterval = TimeUnit.SECONDS.toMillis(30)

            // Sets the maximum time when batched location updates are delivered. Updates may be
            // delivered sooner than this interval.
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                currentLocation = locationResult.lastLocation
                map.clear()
                if (pins!!.size > 0) {
                    for (pin in pins!!) {
                        mapViewModel.calculateDistance(locationResult.lastLocation, pin)
                        map.addMarker(
                            MarkerOptions().position(LatLng(pin.lat, pin.lng)).title(pin.title)
                        )?.setIcon(mapViewModel.setMarkerColor(pin.unlocked, pin.isNear))
                    }
                }
            }

            override fun onLocationAvailability(p0: LocationAvailability) {
                super.onLocationAvailability(p0)
            }
        }

        //check if ar is supported
        //isArrSupported = isARSupported() todo for first milestone we don't have ar functionality
    }

    @SuppressLint("MissingPermission")
    private fun setUpMaps() {
        getLastLocation()

        mapFragment.getMapAsync { googleMap ->
            map = googleMap
            map.isMyLocationEnabled = true
            if (pins != null) {
                if (pins!!.size > 0) {
                    for (pin in pins!!) {
                        map.addMarker(
                            MarkerOptions().position(LatLng(pin.lat, pin.lng)).title(pin.title)
                        )?.setIcon(mapViewModel.setMarkerColor(pin.unlocked, pin.isNear))
                    }
                }
            }

            map.setMinZoomPreference(12.0F)

            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.map_style_json
                )
            )

            //todo for first milestone we don't have ar functionality
            /*if (!isArrSupported) { //proveri da li je vec prikazan dijalog sa obavestenjem
                if (!wasDialogAlreadyShown()) {
                    requireContext().oneButtonDialog(null, getString(R.string.ar_not_supported), getString(R.string.ok))
                    setARDialogFlag()
                }
            }*/

            googleMap.setOnInfoWindowClickListener {
                //val place = currentLocation?.let { Place(Geometry(GeometryLocation(it1.latitude, it1.longitude))) }
                for (pin in pins!!) {
                    if (pin.lat == it.position.latitude && pin.lng == it.position.longitude) {
                        val bundle = Bundle()
                        //bundle.putSerializable("location_data", pin)
                        bundle.putSerializable("details", pin)
                        if (pin.unlocked) {
                            if (isArrSupported) {
                                Navigation.findNavController(binding.root)
                                    .navigate(R.id.action_mapFragment_to_arFragment, bundle)
                            } else {
                                Navigation.findNavController(binding.root)
                                    .navigate(R.id.action_mapFragment_to_placeDetailsFragment, bundle)
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

        if (isLocationEnabled()) {
            fusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                val location: Location? = task.result
                if (location == null) {
                    requestNewLocationData()
                } else {
                    currentLocation = location
                    map.moveCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.fromLatLngZoom(
                                currentLocation!!.latLng, mapViewModel.zoomLevel
                            )
                        )
                    )
                }
            }
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.turn_on_location),
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
    }

    private fun requestNewLocationData() {
        locationRequest = LocationRequest.create().apply {
            this.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            this.interval = 60
            this.fastestInterval = 30
            //this.numUpdates = 1
        }
    }

    private fun setARDialogFlag() {
        SharedPrefs.save(requireActivity(), getString(R.string.was_ar_dialog_shown), true)
    }

    private fun wasDialogAlreadyShown(): Boolean {
        return SharedPrefs.get(
            requireActivity(),
            getString(R.string.was_ar_dialog_shown),
            false
        ) as Boolean
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onResume() {
        super.onResume()
        setUpMaps()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
        mapViewModel.zoomLevel = map.cameraPosition.zoom
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun isARSupported(): Boolean {
        return SharedPrefs.get(
            requireActivity(),
            getString(R.string.ar_supported_flag),
            false
        ) as Boolean
    }
}
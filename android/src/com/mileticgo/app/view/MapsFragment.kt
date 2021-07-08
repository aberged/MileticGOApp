package com.mileticgo.app.view

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mileticgo.app.R
import com.mileticgo.app.databinding.FragmentMapBinding

class MapsFragment : Fragment() {

    private lateinit var binding : FragmentMapBinding

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val miletic = LatLng(45.2550458, 19.8447484)
        googleMap.addMarker(MarkerOptions().position(miletic).title("spomenik sivom тићу Милетићу"))
        googleMap.setMinZoomPreference(15.0F)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(miletic))
        googleMap.setOnInfoWindowClickListener {

            val infoText = "Слава, слава, слава Србину!\n" +
                    "Оро кличе са висине, глас се чује из дубине,\n" +
                    "ни бриге те, сиви тићу, ми смо с тобом Светозаре Милетићу!\n" +
                    "Кад извесни шалај куцне час, зови само ево нас!\n" +
                    "\n" +
                    "Слава, слава, слава Србину!\n" +
                    "Петсто века већ је прошло како Србин робује,\n" +
                    "ни бриге те, сиви тићу, ми смо с тобом Светозаре Милетићу!\n" +
                    "Кад извесни шалај куцне час, зови само ево нас!\n" +
                    "\n" +
                    "Слава, слава, слава Србину!\n" +
                    "Светозаре, српски сине, ти си сине дика Војводине,\n" +
                    "ни бриге те, сиви тићу, ми смо с тобом Светозаре Милетићу!\n" +
                    "Кад извесни шалај куцне час, зови само ево нас!"

            //setting data for sending to place details fragment
            parentFragmentManager.setFragmentResult("420", bundleOf("info_data" to infoText))

            Navigation.findNavController(binding.root).navigate(R.id.action_mapFragment_to_placeDetailsFragment)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}
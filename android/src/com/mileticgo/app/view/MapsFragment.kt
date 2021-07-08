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
        val miletic = LatLng(45.2550458, 19.8447484)
        googleMap.addMarker(MarkerOptions().position(miletic).title("spomenik sivom тићу Милетићу"))
        googleMap.setMinZoomPreference(15.0F)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(miletic))
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

            //setting data for sending to place details fragment
            parentFragmentManager.setFragmentResult("420", bundleOf("info_data" to infoText))

            Navigation.findNavController(binding.root).navigate(R.id.action_mapFragment_to_placeDetailsFragment)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}
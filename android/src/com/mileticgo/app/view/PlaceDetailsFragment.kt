package com.mileticgo.app.view

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mileticgo.app.CityPin
import com.mileticgo.app.R
import com.mileticgo.app.databinding.FragmentPlaceDetailsBinding
import com.mileticgo.app.view_model.PlaceDetailsViewModel

class PlaceDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPlaceDetailsBinding

    private val placeDetailsViewModel by viewModels<PlaceDetailsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_place_details, container, false)

        //getting data from collection list item click or from click on pin in map
        if (arguments?.getSerializable("details") != null) {
            if (arguments?.getSerializable("details") is CityPin) {
                val cityPin : CityPin = arguments?.getSerializable("details") as CityPin
                binding.tvDetailsTitle.text = cityPin.title
                binding.tvDetailsDescription.movementMethod = ScrollingMovementMethod()
                binding.tvDetailsDescription.text = cityPin.description

                placeDetailsViewModel.addPinToInventory(cityPin)
            }
        }

        binding.myToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }
}
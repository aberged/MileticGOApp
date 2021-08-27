package com.mileticgo.app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mileticgo.app.R
import com.mileticgo.app.databinding.FragmentPlaceDetailsBinding
import com.mileticgo.app.model.Place
import com.mileticgo.app.view_model.PlaceDetailsViewModel

class PlaceDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPlaceDetailsBinding

    private val placeDetailsViewModel by viewModels<PlaceDetailsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_place_details, container, false)

        //getting data from maps fragment
        parentFragmentManager.setFragmentResultListener("420", this) { key, bundle ->
            val result = bundle.getString("info_data")
            binding.textView3.text = result
        }

        //getting data from collection list item click
        if (arguments?.getSerializable("details_name") != null) {
            binding.textView3.text = arguments?.getString("details_name")
        }

        binding.myToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }
}
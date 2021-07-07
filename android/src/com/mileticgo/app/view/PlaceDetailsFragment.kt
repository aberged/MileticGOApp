package com.mileticgo.app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mileticgo.app.R
import com.mileticgo.app.databinding.FragmentPlaceDetailsBinding
import com.mileticgo.app.view_model.PlaceDetailsViewModel

class PlaceDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPlaceDetailsBinding

    private val placeDetailsViewModel by viewModels<PlaceDetailsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_place_details, container, false)

        return binding.root
    }
}
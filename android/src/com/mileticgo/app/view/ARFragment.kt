package com.mileticgo.app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mileticgo.app.R
import com.mileticgo.app.databinding.FragmentArBinding
import com.mileticgo.app.view_model.ARViewModel

class ARFragment  : Fragment() {

    private lateinit var binding : FragmentArBinding

    private val arViewModel by viewModels<ARViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ar, container, false)

        return binding.root
    }
}
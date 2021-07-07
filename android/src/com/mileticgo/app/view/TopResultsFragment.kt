package com.mileticgo.app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mileticgo.app.R
import com.mileticgo.app.databinding.FragmentTopResultsBinding
import com.mileticgo.app.view_model.TopResultsViewModel

class TopResultsFragment : Fragment() {

    private lateinit var binding: FragmentTopResultsBinding

    private val topResultViewModel by viewModels<TopResultsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_top_results, container, false)

        return binding.root
    }
}
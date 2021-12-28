package com.mileticgo.app.view

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.mileticgo.app.R
import com.mileticgo.app.databinding.AboutFragmentBinding

class AboutFragment : Fragment() {

    private lateinit var binding : AboutFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.about_fragment, container, false)

        binding.tvAboutText.movementMethod = ScrollingMovementMethod()

        return binding.root
    }

}
package com.mileticgo.app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.mileticgo.app.R
import com.mileticgo.app.databinding.FragmentSettingsBinding
import com.mileticgo.app.view_model.SettingsViewModel

class SettingsFragment : Fragment() {

    private lateinit var binding : FragmentSettingsBinding

    private val settingsViewModel by viewModels<SettingsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        binding.btnSettingsLogin.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_settingsFragment_to_loginFragment)
        }

        binding.myToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }
}
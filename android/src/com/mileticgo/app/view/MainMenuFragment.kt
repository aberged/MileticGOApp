package com.mileticgo.app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.mileticgo.app.R
import com.mileticgo.app.databinding.FragmentMainMenuBinding
import com.mileticgo.app.view_model.MainMenuViewModel

class MainMenuFragment : Fragment() {

    private lateinit var binding: FragmentMainMenuBinding

    private val mainMenuViewModel by viewModels<MainMenuViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_menu, container, false)

        binding.btnMap.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mainMenuFragment_to_mapFragment)
        }

        binding.btnCollection.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mainMenuFragment_to_collectionFragment)
        }

        binding.btnResults.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mainMenuFragment_to_topResultsFragment)
        }

        binding.btnSettings.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mainMenuFragment_to_settingsFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoginInfo()
    }

    //if user is not logged in - show dialog
    private fun showLoginInfo() {
        requireContext().oneButtonDialog(getString(R.string.login_dialog_info_title),
            getString(R.string.login_dialog_info_message), getString(R.string.ok))
    }
}
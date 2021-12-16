package com.mileticgo.app.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.mileticgo.app.R
import com.mileticgo.app.Repository
import com.mileticgo.app.databinding.FragmentMainMenuBinding
import com.mileticgo.app.utils.SharedPrefs
import com.mileticgo.app.utils.oneButtonDialog
import com.mileticgo.app.utils.twoButtonsDialog
import com.mileticgo.app.view_model.MainMenuViewModel

class MainMenuFragment : Fragment() {

    private lateinit var binding: FragmentMainMenuBinding

    private val mainMenuViewModel by viewModels<MainMenuViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_menu, container, false)

        binding.btnMap.setOnClickListener {
            if (isLocationEnabled()) {
                checkPermissions()
               // Repository.get().addRandomPinToInventory()
            } else {
                requireContext().twoButtonsDialog(getString(R.string.info_dialog_title), getString(R.string.turn_location_on_text),
                    getString(R.string.yes), getString(R.string.no), firstButtonCallback = {
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(intent)
                    })
            }
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

        if (!Repository.get().user.isAnonymous) {
            //user is logged in, set flag for dialog to true
            SharedPrefs.save(requireActivity(), getString(R.string.was_login_info_dialog_shown), true)
        } else {
            if (!(SharedPrefs.get(requireActivity(), getString(R.string.was_login_info_dialog_shown), false) as Boolean)) {
                showLoginInfo()
            }
        }

        return binding.root
    }

    //if user is not logged in - show dialog
    private fun showLoginInfo() {
        requireContext().oneButtonDialog(getString(R.string.login_dialog_info_title),
            getString(R.string.login_dialog_info_message), getString(R.string.ok))

        SharedPrefs.save(requireActivity(), getString(R.string.was_login_info_dialog_shown), true)
    }

    private fun isLocationEnabled() : Boolean {
        val locationManager : LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Navigation.findNavController(binding.root).navigate(R.id.action_mainMenuFragment_to_mapFragment)
        } else {
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        var isGrantedFineLocation = false
        for (entry in it.entries) {
            if (entry.key == Manifest.permission.ACCESS_FINE_LOCATION) {
                if (!entry.value) {
                    isGrantedFineLocation = false
                    //location permission is not granted
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.location_permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    isGrantedFineLocation = true
                }
            }
            if (isGrantedFineLocation) {
                Navigation.findNavController(binding.root).navigate(R.id.action_mainMenuFragment_to_mapFragment)
            }
        }
    }
}
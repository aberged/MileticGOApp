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
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.mileticgo.app.R
import com.mileticgo.app.databinding.FragmentSettingsBinding
import com.mileticgo.app.utils.SharedPrefs
import com.mileticgo.app.view_model.SettingsViewModel

class SettingsFragment : Fragment() {

    private lateinit var binding : FragmentSettingsBinding

    private val settingsViewModel by viewModels<SettingsViewModel>()

    private var isLogged: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        setLoginButtonText()

        binding.btnSettingsLogin.setOnClickListener {
            if (!isLogged) {
                Navigation.findNavController(it)
                    .navigate(R.id.action_settingsFragment_to_loginFragment)
            } else {
                //sign out user
                requireContext().twoButtonsDialog(getString(R.string.sign_out_dialog_title), getString(R.string.sign_out_dialog_message),
                getString(R.string.yes), getString(R.string.no), firstButtonCallback = {
                        //todo sign out
                    })
            }
        }

        binding.myToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun setLoginButtonText() {
        isLogged = SharedPrefs.get(requireActivity(), getString(R.string.is_user_logged_in), false) as Boolean
        if (isLogged) {
            binding.btnSettingsLogin.text = getString(R.string.sign_out_button_text)
        } else {
            binding.btnSettingsLogin.text = getString(R.string.login_button_text)
        }
    }
}
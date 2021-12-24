package com.mileticgo.app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.mileticgo.app.R
import com.mileticgo.app.Repository
import com.mileticgo.app.databinding.FragmentSettingsBinding
import com.mileticgo.app.utils.twoButtonsDialog

class SettingsFragment : Fragment() {

    private lateinit var binding : FragmentSettingsBinding

    private var isLogged: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        setLoginButtonText()

        setUserInfoText()

        binding.btnSettingsLogin.setOnClickListener {
            if (!isLogged) {
                Navigation.findNavController(it)
                    .navigate(R.id.action_settingsFragment_to_loginFragment)
            } else {
                //sign out user
                requireContext().twoButtonsDialog(getString(R.string.sign_out_dialog_title), getString(R.string.sign_out_dialog_message),
                getString(R.string.yes), getString(R.string.no), firstButtonCallback = {
                        //todo sign out
                        Repository.get().logout { ready, updating, error, msg ->
                            setLoginButtonText()
                            setUserInfoText() }
                    })
            }
        }

        binding.btnSettingsAbout.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_settingsFragment_to_aboutFragment)
        }

        return binding.root
    }

    private fun setUserInfoText() {
        if (isLogged) {
            binding.tvUserInfo.text = Repository.get().user.name
        } else {
            binding.tvUserInfo.text = getString(R.string.anonymous)
        }
    }

    private fun setLoginButtonText() {
        isLogged = !Repository.get().user.isAnonymous
        if (isLogged) {
            binding.btnSettingsLogin.text = getString(R.string.sign_out_button_text)
        } else {
            binding.btnSettingsLogin.text = getString(R.string.login_button_text)
        }
    }
}
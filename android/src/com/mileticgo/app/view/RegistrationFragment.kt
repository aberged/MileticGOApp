package com.mileticgo.app.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.mileticgo.app.R
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mileticgo.app.Repository
import com.mileticgo.app.databinding.RegistrationFragmentBinding
import com.mileticgo.app.utils.oneButtonDialog
import com.mileticgo.app.view_model.RegistrationViewModel

class RegistrationFragment : Fragment() {

    private val registrationViewModel by viewModels<RegistrationViewModel>()

    private lateinit var binding : RegistrationFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.registration_fragment, container, false)
        binding.registrationViewModel = registrationViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        registrationViewModel.alreadyRegistered.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().popBackStack()
            }
        })

        registrationViewModel.register.observe(viewLifecycleOwner, {
            if (checkUserName() && checkEmailAndPassword() && passwordMatch()) {
                showLoader()
                registerUser(binding.etUserName.text.toString(), binding.etLoginEmail.text.toString(), binding.etLoginPassword.text.toString())
            }
        })

        return binding.root
    }

    private fun checkUserName(): Boolean {
        if (binding.etUserName.text.isNotBlank()) {
            return true
        } else {
            binding.etUserName.error = getString(R.string.user_name)
        }
        return false
    }

    private fun passwordMatch(): Boolean {
        if (binding.etRepeatRegisterPassword.text.isNotBlank()) {
            if (binding.etLoginPassword.text.toString() == binding.etRepeatRegisterPassword.text.toString()) {
                return true
            } else {
                binding.etRepeatRegisterPassword.error = getString(R.string.passwords_match_false)
            }
        } else {
            binding.etRepeatRegisterPassword.error = getString(R.string.enter_password)
        }
        return false
    }

    private fun checkEmailAndPassword(): Boolean {
        if (binding.etLoginEmail.text.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etLoginEmail.text).matches()) {
            //email is ok, check if password field is empty
            if (binding.etLoginPassword.text.isNotBlank()) {
                return true
            } else {
                binding.etLoginPassword.error = getString(R.string.enter_password)
            }
        } else {
            binding.etLoginEmail.error = getString(R.string.not_valid_email)
        }
        return false
    }

    private fun registerUser(userName: String, email: String, password: String) {
        Repository.get().register(userName, email, password) { ready, updating, error, msg ->
            if (!error) {
                hideLoader()
                //return to previous fragment/screen
                findNavController().popBackStack()
            } else {
                hideLoader()
                this.activity?.runOnUiThread {
                    requireContext().oneButtonDialog(getString(R.string.login_dialog_info_title), getString(R.string.registration_unsuccessful), getString(R.string.ok))
                }
            }
        }
    }

    private fun showLoader() {
        this.activity?.runOnUiThread {
            binding.progressContainer.visibility = View.VISIBLE
        }
    }

    private fun hideLoader() {
        this.activity?.runOnUiThread {
            binding.progressContainer.visibility = View.GONE
        }
    }
}
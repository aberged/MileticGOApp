package com.mileticgo.app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mileticgo.app.AndroidApplication
import com.mileticgo.app.R
import com.mileticgo.app.Repository
import com.mileticgo.app.databinding.FragmentLoginBinding
import com.mileticgo.app.utils.SharedPrefs
import com.mileticgo.app.utils.oneButtonDialog
import com.mileticgo.app.view_model.LoginViewModel

class LoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBinding

    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        loginViewModel.login.observe(viewLifecycleOwner, {
            if (checkEmailAndPassword()) {
                sendUser(binding.etLoginEmail.text.toString(), binding.etLoginPassword.text.toString())
            }
        })

        loginViewModel.register.observe(viewLifecycleOwner, {
            if (checkUserName() && checkEmailAndPassword() && passwordMatch()) {
                registerUser(binding.etUserName.text.toString(), binding.etLoginEmail.text.toString(), binding.etLoginPassword.text.toString())
            }
        })

        binding.myToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

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

    private fun sendUser(email: String, password: String) {
        Repository.get().login(email, password) { successful ->
            if (successful) {
                //return to previous fragment/screen
                this.activity?.runOnUiThread {
                    findNavController().popBackStack()
                }
            } else {
                this.activity?.runOnUiThread {
                    requireContext().oneButtonDialog(getString(R.string.login_dialog_info_title), getString(R.string.login_unsuccessful), getString(R.string.ok))
                }
            }
        }
    }

    private fun registerUser(userName: String, email: String, password: String) {
        Repository.get().register(userName, email, password) { successful ->
            if (successful) {
                //return to previous fragment/screen
                findNavController().popBackStack()
            } else {
                requireContext().oneButtonDialog(getString(R.string.login_dialog_info_title), getString(R.string.registration_unsuccessful), getString(R.string.ok))
            }
        }
    }
}
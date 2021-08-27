package com.mileticgo.app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.fragment.findNavController
import com.mileticgo.app.AndroidApplication
import com.mileticgo.app.R
import com.mileticgo.app.databinding.FragmentLoginBinding
import com.mileticgo.app.view_model.LoginViewModel

class LoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBinding

    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        
        binding.btnLogin.setOnClickListener {
            checkEmailAndPassword()
        }

        binding.myToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun checkEmailAndPassword() {
        if (binding.etLoginEmail.text.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etLoginEmail.text).matches()) {
            //email is ok, check if password field is empty
            if (binding.etLoginPassword.text.isNotBlank()) {
                sendUser(binding.etLoginEmail.text.toString(), binding.etLoginPassword.text.toString())
            } else {
                binding.etLoginPassword.error = getString(R.string.enter_password)
            }
        } else {
            binding.etLoginEmail.error = getString(R.string.not_valid_email)
        }
    }

    private fun sendUser(email: String, password: String) {
        (activity?.application as AndroidApplication).repository.setUser(email, password)

        //return to previous fragment/screen
        requireActivity().supportFragmentManager.popBackStack()
    }
}
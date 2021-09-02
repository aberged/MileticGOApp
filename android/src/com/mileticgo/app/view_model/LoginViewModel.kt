package com.mileticgo.app.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mileticgo.app.view.LoginFragment

class LoginViewModel : ViewModel() {

    private val _onRegisterClick = MutableLiveData<Boolean>()

    val onRegisterClick: LiveData<Boolean>
        get() = _onRegisterClick

    fun onRegisterClick() {
        _onRegisterClick.value = true
    }


}
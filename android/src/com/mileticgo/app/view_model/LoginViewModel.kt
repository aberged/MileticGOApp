package com.mileticgo.app.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    //click on text field to register new user
    private val _onRegisterNewUserClick = MutableLiveData<Boolean>()
    val onRegisterNewUserClick: LiveData<Boolean>
        get() = _onRegisterNewUserClick

    fun onRegisterNewUserClick() {
        _onRegisterNewUserClick.value = true
    }

    //click on button register
    private val _register = MutableLiveData<Boolean>()
    val register: LiveData<Boolean>
        get() = _register
    fun register() {
        _register.value = true
    }

    //click on button login
    private val _login = MutableLiveData<Boolean>()
    val login: LiveData<Boolean>
        get() = _login
    fun login() {
        _login.value = true
    }

    init {
        _onRegisterNewUserClick.value = false
    }
}
package com.mileticgo.app.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    //click on button login
    private val _login = MutableLiveData<Boolean>()
    val login: LiveData<Boolean>
        get() = _login
    fun login() {
        _login.value = true
    }
}
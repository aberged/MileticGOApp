package com.mileticgo.app.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegistrationViewModel : ViewModel() {

    //click on button register
    private val _register = MutableLiveData<Boolean>()
    val register: LiveData<Boolean>
        get() = _register
    fun register() {
        _register.value = true
    }

    private val _alreadyRegistered = MutableLiveData<Boolean>()
    val alreadyRegistered : LiveData<Boolean>
        get() = _alreadyRegistered

    fun alreadyRegisteredClick() {
        _alreadyRegistered.value = true
    }
}
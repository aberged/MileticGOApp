package com.mileticgo.app.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mileticgo.app.AndroidApplication
import com.mileticgo.app.Entities.CityPin

class CollectionViewModel(application: Application) : AndroidViewModel(application) {

    private val _cityPins = MutableLiveData<List<CityPin>>()
    val cityPins : LiveData<List<CityPin>>
        get() = _cityPins

    init {
        _cityPins.value = (application as AndroidApplication).repository.user.userInventoryCityPins //get city pins from server
    }
}
package com.mileticgo.app.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mileticgo.app.AndroidApplication
import com.mileticgo.app.CityPin
import com.mileticgo.app.Repository

class CollectionViewModel(application: Application) : AndroidViewModel(application) {

    private val _cityPins = MutableLiveData<List<CityPin>>()
    val cityPins : LiveData<List<CityPin>>
        get() = _cityPins

    init {
        _cityPins.value = Repository.get().userInventoryCityPinsForActiveCityProfile //get city pins from server
    }
}
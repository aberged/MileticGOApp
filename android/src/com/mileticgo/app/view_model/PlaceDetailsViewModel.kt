package com.mileticgo.app.view_model

import androidx.lifecycle.ViewModel
import com.mileticgo.app.CityPin
import com.mileticgo.app.Repository

class PlaceDetailsViewModel : ViewModel() {

    fun addPinToInventory(cityPin: CityPin) {
        if (!cityPin.unlocked) {
            Repository.get().addPinToInventory(cityPin)
        }
    }
}
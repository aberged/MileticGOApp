package com.mileticgo.app.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mileticgo.app.model.Place

class MapViewModel : ViewModel() {

    var place = MutableLiveData<Place>()

    fun selectPlace(place: Place) {
       this.place.value = place
    }
}
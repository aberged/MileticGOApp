package com.mileticgo.app.view_model

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.mileticgo.app.CityPin
import com.mileticgo.app.R
import com.mileticgo.app.Repository

class MapViewModel : ViewModel() {

    private var _pins = MutableLiveData<List<CityPin>>()
    val pins : LiveData<List<CityPin>>
        get() = _pins

    init {
        _pins.value = Repository.get().activeCityPins
    }

    //default map zoom level
    var zoomLevel = 12f

    /**
     * Set marker color
     *
     * @param unlocked set color to gray if false
     * @param isNear if user is near the marker color marker to green
     */
    fun setMarkerColor(unlocked: Boolean, isNear: Boolean): BitmapDescriptor? {
        return if (unlocked) {
            BitmapDescriptorFactory.fromResource(R.drawable.pin_checked)
        } else {
            if (isNear) {
                BitmapDescriptorFactory.fromResource(R.drawable.pin_active)
            } else {
                BitmapDescriptorFactory.fromResource(R.drawable.pin_locked)
            }
        }
    }

    fun calculateDistance(currentLocation: Location, pin: CityPin) {
        val earthRadius = 6371 //in km, in miles - 3958.75
        if (!pin.unlocked) {
            val tmpPinLocation = Location("")
            tmpPinLocation.latitude = pin.lat
            tmpPinLocation.longitude = pin.lng
            pin.isNear = currentLocation.distanceTo(tmpPinLocation) <= 50
        }
    }
}
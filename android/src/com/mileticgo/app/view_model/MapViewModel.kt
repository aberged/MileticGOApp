package com.mileticgo.app.view_model

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.mileticgo.app.Entities.CityPin
import com.mileticgo.app.R
import com.mileticgo.app.model.Place

class MapViewModel : ViewModel() {

    var place = MutableLiveData<Place>()

    fun selectPlace(place: Place) {
       this.place.value = place
    }

    /**
     * Set marker color
     *
     * @param unlocked set color to gray if false
     * @param isNear if user is near the marker color marker to green
     */
    fun setMarkerColor(unlocked: Boolean, isNear: Boolean): BitmapDescriptor? {
        val hsv = FloatArray(3)
        return if (isNear) {
            BitmapDescriptorFactory.fromResource(R.drawable.pin_green_small)
        } else {
            if (unlocked) {
                BitmapDescriptorFactory.fromResource(R.drawable.pin_blue_small)
            } else {
                BitmapDescriptorFactory.fromResource(R.drawable.pin_gray_small)
            }
        }
    }

    fun calculateDistance(currentLocation: Location, pin: CityPin) {
        val earthRadius = 6371 //in km, in miles - 3958.75
        val tmpPinLocation = Location("")
        tmpPinLocation.latitude = pin.lat
        tmpPinLocation.longitude = pin.lng
        if (currentLocation.distanceTo(tmpPinLocation) <= 50) {
            pin.isNear = true
        }
    }
}
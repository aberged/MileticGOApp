package com.mileticgo.app.model

import com.google.android.gms.maps.model.LatLng
import com.google.ar.sceneform.math.Vector3
import com.google.maps.android.ktx.utils.sphericalHeading
import java.io.Serializable
import kotlin.math.cos
import kotlin.math.sin

/*
data class Place(
    val geometry: Geometry
) : Serializable {
    */
/*override fun equals(other: Any?): Boolean {
        if (other !is Place) {
            return false
        }
        return this.id == other.id
    }*//*


    */
/*override fun hashCode(): Int {
        return this.id.hashCode()
    }*//*

}

fun Place.getPositionVector(azimuth: Float, latLng: LatLng): Vector3 {
    val placeLatLng = this.geometry.location.latLng
    // TODO compute heading
    val heading = latLng.sphericalHeading(placeLatLng)
    val r = -2f
    val x = r * sin(azimuth + heading).toFloat()
    val y = 1f
    val z = r * cos(azimuth + heading).toFloat()
    return Vector3(x, y, z)
}


data class Geometry(
    val location: GeometryLocation
)

data class GeometryLocation(
    val lat: Double,
    val lng: Double
) {
    val latLng: LatLng
        get() = LatLng(lat, lng)
}*/

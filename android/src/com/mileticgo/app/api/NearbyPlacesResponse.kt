package com.mileticgo.app.api

import com.google.gson.annotations.SerializedName
import com.mileticgo.app.model.Place

/**
 * Data class encapsulating a response from the nearby search call to the Places API.
 */
data class NearbyPlacesResponse(
    @SerializedName("results") val results: List<Place>
)
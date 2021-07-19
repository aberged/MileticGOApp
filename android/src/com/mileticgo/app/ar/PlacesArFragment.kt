package com.mileticgo.app.ar

import com.google.ar.sceneform.ux.ArFragment

class PlacesArFragment : ArFragment() {

    override fun getAdditionalPermissions(): Array<String> =
        // TODO return location permission
        emptyArray()
}
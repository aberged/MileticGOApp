package com.mileticgo.app.view_model

import androidx.lifecycle.ViewModel
import com.mileticgo.app.view.CollectionItem

class CollectionViewModel : ViewModel() {

    fun getDummyItems(): List<CollectionItem> {
        val list = arrayListOf<CollectionItem>()
        list.add(CollectionItem("mirko"))
        list.add(CollectionItem("slavko"))
        list.add(CollectionItem("sirogojno"))
        list.add(CollectionItem("bosko"))
        list.add(CollectionItem("savo"))
        list.add(CollectionItem("krcun"))
        list.add(CollectionItem("branko"))
        list.add(CollectionItem("ranko"))
        list.add(CollectionItem("pinki"))
        list.add(CollectionItem("jovanka"))

        return list
    }
}
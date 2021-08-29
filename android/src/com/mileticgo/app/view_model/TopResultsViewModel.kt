package com.mileticgo.app.view_model

import androidx.lifecycle.ViewModel
import com.mileticgo.app.view.TopResultItem

class TopResultsViewModel : ViewModel() {

    fun getDummyItems(): List<TopResultItem> {
        val list = arrayListOf<TopResultItem>()
        list.add(TopResultItem("mirko", 45))
        list.add(TopResultItem("slavko", 66))
        list.add(TopResultItem("sirogojno", 90))
        list.add(TopResultItem("bosko", 11))
        list.add(TopResultItem("savo", 4))
        list.add(TopResultItem("krcun", 23))
        list.add(TopResultItem("branko", 51))
        list.add(TopResultItem("ranko", 87))
        list.add(TopResultItem("pinki", 55))
        list.add(TopResultItem("jovanka", 49))

        return list
    }
}
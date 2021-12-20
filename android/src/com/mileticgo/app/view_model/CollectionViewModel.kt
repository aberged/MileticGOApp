package com.mileticgo.app.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.tabs.TabLayout
import com.mileticgo.app.CityPin
import com.mileticgo.app.Repository

class CollectionViewModel(application: Application) : AndroidViewModel(application) {

    var viewPagerPosition: Int = 0
    private val _cityPins = MutableLiveData<List<CityPin>>()
    val cityPins: LiveData<List<CityPin>>
        get() = _cityPins

    init {
        _cityPins.value =
            Repository.get().userInventoryCityPinsForActiveCityProfile //get city pins from server
    }

    private val _categoryList = MutableLiveData<List<List<CityPin>>>()
    val categoryList: LiveData<List<List<CityPin>>>
        get() = _categoryList

    var list: MutableList<ArrayList<CityPin>> = mutableListOf()

    fun createCategorySubLists(cityPins: List<CityPin>) {
        list.clear()
        //_categoryList.value = ArrayList()

        val groupByCategory = cityPins.groupBy { cityPin ->
            cityPin.category
        }
        for ((key, value) in groupByCategory) {
            list.add(value as ArrayList<CityPin>)
        }
        _categoryList.value = list
    }
}
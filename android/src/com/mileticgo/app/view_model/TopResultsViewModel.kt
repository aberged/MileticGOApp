package com.mileticgo.app.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mileticgo.app.Repository
import com.mileticgo.app.TopScoreListItem

class TopResultsViewModel : ViewModel() {

    private val _topScores = MutableLiveData<List<TopScoreListItem>>()
    val topScores : LiveData<List<TopScoreListItem>>
        get() = _topScores

    init {
        _topScores.value = emptyList()
    }
}
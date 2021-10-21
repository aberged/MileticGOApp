package com.mileticgo.app.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mileticgo.app.LeaderboardCallback
import com.mileticgo.app.Repository
import com.mileticgo.app.TopScoreListItem
import java.util.*

class TopResultsViewModel : ViewModel(), LeaderboardCallback {

    private val _topScores = MutableLiveData<List<TopScoreListItem>>()
    val topScores : LiveData<List<TopScoreListItem>>
        get() = _topScores

    init {
        Repository.get().getLeaderboard(this)
    }

    override fun result(list: MutableList<TopScoreListItem>?) {
        _topScores.postValue(list!!)
    }

    override fun error(message: String?) {
        _topScores.value = ArrayList()
    }
}
package com.mileticgo.app.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mileticgo.app.LeaderboardCallback
import com.mileticgo.app.Repository
import com.mileticgo.app.TopScoreListItem

class TopResultsViewModel : ViewModel(), LeaderboardCallback {

    private val _topScores = MutableLiveData<List<TopScoreListItem>>()
    val topScores : LiveData<List<TopScoreListItem>>
        get() = _topScores

    init {
        Repository.get().getLeaderboard(this)
    }

    override fun result(list: MutableList<TopScoreListItem>) {
        if (list.isNotEmpty()) {
            for(position in list.indices) {
                list[position].position = position + 1

                if (!Repository.get().user.isAnonymous && (Repository.get().userInventoryCityPinsForActiveCityProfile.isNotEmpty())
                    && list[position].userName == Repository.get().user.name ) {
                    setUserResult(list[position])
                }
            }
        }
        _topScores.postValue(list)
    }

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage : LiveData<String>
        get() = _errorMessage

    override fun error(message: String) {
        //_topScores.value = emptyList()
        _errorMessage.postValue(message)
    }

    private var _userResult = MutableLiveData<TopScoreListItem>()
    val userResult : LiveData<TopScoreListItem>
            get() = _userResult

    private fun setUserResult(userResult: TopScoreListItem) {
        _userResult.postValue(userResult)
    }
}
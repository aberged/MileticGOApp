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
            var isUserFound = false
            for(position in list.indices) {
                list[position].position = position + 1

                if (!Repository.get().user.isAnonymous && (Repository.get().userInventoryCityPinsForActiveCityProfile.isNotEmpty())
                    && list[position].email == Repository.get().user.email ) {
                    setUserResultFromList(list[position])
                    isUserFound = true
                }
            }
            if (!isUserFound) {
                setUserResultFromRepository(RepositoryResult(Repository.get().userInventoryCityPinsForActiveCityProfile.size,
                    Repository.get().user.name, 0))
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

    private var _userResultFromList = MutableLiveData<TopScoreListItem>()
    val userResultFromList : LiveData<TopScoreListItem>
            get() = _userResultFromList

    private fun setUserResultFromList(userResult: TopScoreListItem) {
        _userResultFromList.postValue(userResult)
    }

    private var _userResultFromRepository = MutableLiveData<RepositoryResult>()
    val userResultFromRepository : LiveData<RepositoryResult>
        get() = _userResultFromRepository

    private fun setUserResultFromRepository(repositoryResult: RepositoryResult) {
        _userResultFromRepository.postValue(repositoryResult)
    }

    data class RepositoryResult(val score: Int, val name: String, val position: Int)
}
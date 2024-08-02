package com.example.project.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.AppDomain
import com.example.project.data.model.Achievement
import com.example.project.data.model.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel : ViewModel() {
    private val appDomain = AppDomain()
    private lateinit var game : LiveData<Game>
    private lateinit var listAchievements : LiveData<List<Achievement>>
    fun getListAchievementsByGame(gameId: Int): LiveData<List<Achievement>>
    {
        listAchievements = appDomain.getAllGameAchievements(gameId)
        return listAchievements
    }
}
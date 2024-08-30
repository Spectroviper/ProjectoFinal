package com.example.project.ui.gameAdded

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.project.AppDomain
import com.example.project.data.model.Achievement
import com.example.project.data.model.Game

class GameAddedViewModel : ViewModel() {
    private val appDomain = AppDomain()
    private lateinit var game : LiveData<Game>
    private lateinit var listAchievements : LiveData<List<Achievement>>
    fun getListAchievementsByGame(gameId: Int): LiveData<List<Achievement>>
    {
        listAchievements = appDomain.getAllGameAchievements(gameId)
        return listAchievements
    }
}
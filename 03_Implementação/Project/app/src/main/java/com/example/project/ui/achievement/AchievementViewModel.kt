package com.example.project.ui.achievement

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.project.AppDomain
import com.example.project.data.model.Achievement
import com.example.project.data.model.Game

class AchievementViewModel : ViewModel() {
    private val appDomain = AppDomain()
    private lateinit var game : LiveData<Game>
    private lateinit var achievementDetail : LiveData<Achievement>
    fun getAchievementDetail(gameId: Int): LiveData<Achievement>
    {
        achievementDetail = appDomain.getAchievementDetail(gameId)
        return achievementDetail
    }
}
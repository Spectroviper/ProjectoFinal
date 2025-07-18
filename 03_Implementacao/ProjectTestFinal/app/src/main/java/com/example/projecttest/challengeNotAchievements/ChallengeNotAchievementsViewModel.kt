package com.example.projecttest.challengeNotAchievements

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.projecttest.data.model.Achievement
import com.example.projecttest.data.model.Message
import com.example.projecttest.data.model.User
import com.example.projecttest.services.ServiceLocator.userService


class ChallengeNotAchievementsViewModel : ViewModel() {
    fun getAllAchievementsNotInAChallenge(challengeId: String) : LiveData<List<Achievement>?>
    {
        return userService.getAllAchievementsNotInAChallenge(challengeId)
    }

    fun addChallengeAchievement(challengeId: String, achievementId: String): LiveData<Message>
    {
        return userService.createChallengeAchievement(challengeId, achievementId)
    }
}
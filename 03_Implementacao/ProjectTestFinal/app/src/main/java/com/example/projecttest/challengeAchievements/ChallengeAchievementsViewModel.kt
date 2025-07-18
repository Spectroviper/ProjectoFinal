package com.example.projecttest.challengeAchievements

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projecttest.services.mock.AppDomain
import com.example.projecttest.data.model.Achievement
import com.example.projecttest.data.model.Challenge
import com.example.projecttest.data.model.User
import com.example.projecttest.data.model.UserChallengeAchievement
import com.example.projecttest.services.ServiceLocator

class ChallengeAchievementsViewModel : ViewModel() {
    private val userService = ServiceLocator.userService

    fun getChallengeByChallengeId(challengeId: String): LiveData<Challenge?>
    {
        return userService.getChallengeByChallengeId(challengeId)
    }

    /*fun function(userId: String): LiveData<List<UserChallengeAchievement?>>
    {
        return userService.getUserUserChallengeAchievements(userId)
    }*/
}
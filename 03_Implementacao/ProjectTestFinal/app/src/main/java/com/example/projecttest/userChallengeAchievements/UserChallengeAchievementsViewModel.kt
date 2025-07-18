package com.example.projecttest.userChallengeAchievements

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.projecttest.data.model.Challenge
import com.example.projecttest.data.model.DetailedUserChallenge
import com.example.projecttest.data.model.User
import com.example.projecttest.services.ServiceLocator

class UserChallengeAchievementsViewModel : ViewModel() {
    private val userService = ServiceLocator.userService

    fun getUserById(userId: String): LiveData<User>
    {
        return userService.getUserById(userId)
    }

    fun getUserChallengeByUserChallengeId(userChallengeId: String): LiveData<DetailedUserChallenge?>
    {
        return userService.getUserChallengeByUserChallengeId(userChallengeId)
    }

    fun getChallengeByChallengeId(challengeId: String): LiveData<Challenge?>
    {
        return userService.getChallengeByChallengeId(challengeId)
    }
}
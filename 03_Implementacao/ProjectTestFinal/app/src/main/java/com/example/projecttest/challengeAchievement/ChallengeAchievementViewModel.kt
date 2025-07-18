package com.example.projecttest.challengeAchievement

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.projecttest.data.model.Achievement
import com.example.projecttest.data.model.Challenge
import com.example.projecttest.data.model.ChallengeAchievement
import com.example.projecttest.data.model.Message
import com.example.projecttest.data.model.User
import com.example.projecttest.data.model.UserAchievement
import com.example.projecttest.services.ServiceLocator

class ChallengeAchievementViewModel : ViewModel() {
    private val userService = ServiceLocator.userService

    fun deleteChallengeAchievement(achievementId: String): LiveData<Message>
    {
        return userService.deleteChallengeAchievement(achievementId)
    }

    fun lockOrUnlockAchievement(userId: String, userAchievementId: String): LiveData<Message>
    {
        return userService.lockOrUnlockAchievement(userId,userAchievementId)
    }

    fun getAchievementByAchievementId(achievementId: String): LiveData<Achievement?>
    {
        return userService.getAchievementByAchievementId(achievementId)
    }

    fun getUserByUserId(userId: String): LiveData<User>
    {
        return userService.getUserById(userId)
    }

    fun getChallengeAchievementByChallengeAchievementId(challengeAchievementId: String): LiveData<ChallengeAchievement?>
    {
        return userService.getChallengeAchievementByChallengeAchievementId(challengeAchievementId)
    }

    fun getChallengeByChallengeId(challengeId: String): LiveData<Challenge?>
    {
        return userService.getChallengeByChallengeId(challengeId)
    }

    fun getUserUserAchievements(userId: String): LiveData<List<UserAchievement>?>
    {
        return userService.getUserUserAchievements(userId)
    }
}
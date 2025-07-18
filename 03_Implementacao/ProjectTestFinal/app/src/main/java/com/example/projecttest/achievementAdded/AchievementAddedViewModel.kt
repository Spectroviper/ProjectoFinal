package com.example.projecttest.achievementAdded

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.projecttest.data.model.Achievement
import com.example.projecttest.data.model.Message
import com.example.projecttest.data.model.User
import com.example.projecttest.data.model.UserAchievement
import com.example.projecttest.services.ServiceLocator

class AchievementAddedViewModel : ViewModel() {
    private val userService = ServiceLocator.userService

    fun deleteAchievement(achievementId: String): LiveData<Message>
    {
        return userService.deleteAchievement(achievementId)
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

    fun getUserAchievementByUserAchievementId(userAchievementId: String): LiveData<UserAchievement?>
    {
        return userService.getUserAchievementByUserAchievementId(userAchievementId)
    }
}
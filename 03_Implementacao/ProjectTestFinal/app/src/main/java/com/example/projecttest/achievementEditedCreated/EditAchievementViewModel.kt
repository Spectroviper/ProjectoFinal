package com.example.projecttest.achievementEditedCreated

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.projecttest.data.model.Achievement
import com.example.projecttest.data.model.Message
import com.example.projecttest.data.model.User
import com.example.projecttest.services.ServiceLocator.userService

class EditAchievementViewModel : ViewModel() {

    fun getAchievementByAchievementId(achievementId: String): LiveData<Achievement?> {
        val result = MediatorLiveData<Achievement?>()

        val source = userService.getAchievementByAchievementId(achievementId)

        result.addSource(source) { achievement ->
            if (achievement != null) {
                result.value = achievement
                result.removeSource(source)
            }
        }

        return result
    }

    fun getUserByUserId(userId: String): LiveData<User> {
        return userService.getUserById(userId)
    }
    fun updateAchievement(
        achievementId: String,
        newAchievementName: String,
        newAbout: String,
        newTotalCollectable: Int,
        newImage: String
    ): LiveData<Message> {
        val result = MediatorLiveData<Message>()

        val source = userService.updateAchievement(
            achievementId, newAchievementName, newAbout, newTotalCollectable, newImage
        )

        result.addSource(source) { message ->
            if (message.successfull == true) {
                result.value = message
                result.removeSource(source)
            }
        }
        return result
    }

    fun uploadAchievementImage(uri: Uri, context: Context): LiveData<String?> {
        return userService.uploadImage(uri, context)
    }
}
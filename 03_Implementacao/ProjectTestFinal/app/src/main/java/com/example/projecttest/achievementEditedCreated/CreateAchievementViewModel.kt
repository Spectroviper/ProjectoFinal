package com.example.projecttest.achievementEditedCreated

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.projecttest.data.model.Message
import com.example.projecttest.services.mock.AppDomain
import com.example.projecttest.data.model.User
import com.example.projecttest.services.ServiceLocator
import com.example.projecttest.services.ServiceLocator.userService

class CreateAchievementViewModel : ViewModel() {

    fun createAchievement(
        achievementName: String,
        about: String,
        totalCollectable: Int,
        createdBy: String,
        image: String,
        gameId: String
    ): LiveData<Message> {
        val result = MediatorLiveData<Message>()

        val source = userService.createAchievement(
            achievementName, about, totalCollectable, createdBy, image,
            gameId
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
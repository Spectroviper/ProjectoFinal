package com.example.projecttest.challengeEditedCreated

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.projecttest.data.model.Challenge
import com.example.projecttest.data.model.Message
import com.example.projecttest.data.model.User
import com.example.projecttest.services.ServiceLocator.userService


class CreateChallengeViewModel : ViewModel() {

    fun createChallenge(
        userId: String,
        challengeName: String,
        type: String,
        image: String
    ): LiveData<Message> {
        val result = MediatorLiveData<Message>()

        val source = userService.createChallenge(
            userId, challengeName, type, image
        )

        result.addSource(source) { message ->
            if (message.successfull == true) {
                result.value = message
                result.removeSource(source)
            }
        }
        return result
    }

    fun uploadChallengeImage(uri: Uri, context: Context): LiveData<String?> {
        return userService.uploadImage(uri, context)
    }


}
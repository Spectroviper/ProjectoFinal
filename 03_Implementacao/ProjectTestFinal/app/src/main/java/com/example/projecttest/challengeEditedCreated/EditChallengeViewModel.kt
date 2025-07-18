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

class EditChallengeViewModel : ViewModel() {

    fun getChallengeByChallengeId(challengeId: String): LiveData<Challenge?>
    {
        return userService.getChallengeByChallengeId(challengeId)
    }

    fun updateChallenge(
        challengeId: String,
        newChallengeName: String,
        newType: String,
        newImage: String
    ): LiveData<Message> {
        val result = MediatorLiveData<Message>()

        val source = userService.updateChallenge(
            challengeId,
            newChallengeName,
            newType,
            newImage
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
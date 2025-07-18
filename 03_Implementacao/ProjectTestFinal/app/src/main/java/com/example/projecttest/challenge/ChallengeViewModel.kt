package com.example.projecttest.challenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.projecttest.services.mock.AppDomain
import com.example.projecttest.data.model.Achievement
import com.example.projecttest.data.model.Challenge
import com.example.projecttest.data.model.Game
import com.example.projecttest.data.model.Message
import com.example.projecttest.data.model.User
import com.example.projecttest.data.model.UserChallenge
import com.example.projecttest.services.ServiceLocator
import okhttp3.Request

class ChallengeViewModel : ViewModel() {
    private val userService = ServiceLocator.userService

    fun getChallengeByChallengeId(challengeId: String): LiveData<Challenge?>
    {
        return userService.getChallengeByChallengeId(challengeId)
    }

    fun getUserByUserId(userId: String): LiveData<User> {

        val result = MediatorLiveData<User>()

        val source = ServiceLocator.userService.getUserById(userId)

        result.addSource(source) { user ->
            if (user != null) {
                result.value = user
                result.removeSource(source)
            }
        }

        return result
    }

    fun deleteChallenge(challengeId: String): LiveData<Message>
    {
        return userService.deleteChallenge(challengeId)
    }

    fun startChallenge(userId: String, challengeId: String): LiveData<Message>
    {
        return userService.startChallenge(userId,challengeId)
    }

    fun inviteUserToChallenge(userId: String, challengeId: String, isRequest: Boolean): LiveData<Message>
    {
        return userService.inviteUserToChallenge(userId,challengeId,isRequest)
    }

    fun endChallenge(challengeId: String): LiveData<Message>
    {
        return userService.endChallenge(challengeId)
    }
}
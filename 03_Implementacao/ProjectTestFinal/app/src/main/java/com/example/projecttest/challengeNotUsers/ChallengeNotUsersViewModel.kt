package com.example.projecttest.challengeNotUsers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projecttest.data.model.Challenge
import com.example.projecttest.data.model.Message
import com.example.projecttest.data.model.User
import com.example.projecttest.services.ServiceLocator
import com.example.projecttest.services.ServiceLocator.userService

class ChallengeNotUsersViewModel : ViewModel() {



    fun getAllUsersNotInChallenge(challengeId:String): LiveData<List<User>?>
    {
        return userService.getAllUsersNotInChallenge(challengeId)
    }

    fun inviteUserToChallenge(userId: String, challengeId: String, isRequest: Boolean): LiveData<Message>
    {
        return userService.inviteUserToChallenge(userId,challengeId,isRequest)
    }

    fun getChallengeByChallengeId(challengeId: String): LiveData<Challenge?>
    {
        return userService.getChallengeByChallengeId(challengeId)
    }
}
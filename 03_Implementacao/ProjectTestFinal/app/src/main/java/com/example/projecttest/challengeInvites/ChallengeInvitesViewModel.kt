package com.example.projecttest.challengeInvites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projecttest.data.model.Challenge
import com.example.projecttest.data.model.ChallengeInvite
import com.example.projecttest.data.model.Message
import com.example.projecttest.data.model.User
import com.example.projecttest.services.ServiceLocator.userService


class ChallengeInvitesViewModel : ViewModel() {


    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    fun getAllChallengeInvitesByChallengeId(challengeId: String): LiveData<List<ChallengeInvite>?>
    {
        return userService.getAllChallengeInvitesByChallengeId(challengeId)
    }

    fun getChallengeByChallengeId(challengeId: String): LiveData<Challenge?>
    {
        return userService.getChallengeByChallengeId(challengeId)
    }

    fun getUserByUserId(userId: String): LiveData<User>
    {
        return userService.getUserById(userId)
    }

    fun acceptChallengeInvite(challengeInviteId: String, userId: String): LiveData<Message>
    {
        return userService.acceptChallengeInvite(challengeInviteId, userId)
    }

    fun rejectChallengeInvite(challengeInviteId: String, userId: String): LiveData<Message>
    {
        return userService.rejectChallengeInvite(challengeInviteId, userId)
    }

    fun deleteChallengeInvite(challengeInviteId: String): LiveData<Message>
    {
        return userService.deleteChallengeInvite(challengeInviteId)
    }
    /*fun getUserId(): String {
        return SessionManager.currentUser?.id ?: ""
    }*/

    /*fun getSpecificUser(): Int{
        return _appDomain.getSpecificUser()
    }*/
}
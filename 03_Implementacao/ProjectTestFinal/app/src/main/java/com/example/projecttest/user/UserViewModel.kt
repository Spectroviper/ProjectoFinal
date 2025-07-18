package com.example.projecttest.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projecttest.services.mock.AppDomain
import com.example.projecttest.MyId
import com.example.projecttest.data.mock.AppMock
import com.example.projecttest.data.model.Achievement
import com.example.projecttest.data.model.Challenge
import com.example.projecttest.data.model.ChallengeInvite
import com.example.projecttest.data.model.Friend
import com.example.projecttest.data.model.Game
import com.example.projecttest.data.model.Message
import com.example.projecttest.data.model.User
import com.example.projecttest.data.model.UserChallenge
import com.example.projecttest.services.ServiceLocator
import com.example.projecttest.services.ServiceLocator.userService
import com.example.projecttest.signIn.SignInState

class UserViewModel : ViewModel() {


    fun getUserByUserId(userId: String): LiveData<User> {

        val result = MediatorLiveData<User>()

        val source = userService.getUserById(userId)

        result.addSource(source) { user ->
            if (user != null) {
                result.value = user
                result.removeSource(source)
            }
        }

        return result
    }

    fun invite(sender: String, receiver: String): LiveData<Message>
    {
        return userService.inviteFriend(sender,receiver)
    }
}
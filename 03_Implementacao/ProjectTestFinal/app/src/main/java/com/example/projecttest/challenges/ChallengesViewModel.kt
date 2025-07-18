package com.example.projecttest.challenges

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.projecttest.services.mock.AppDomain
import com.example.projecttest.data.model.Challenge
import com.example.projecttest.data.model.User
import com.example.projecttest.services.ServiceLocator
import com.example.projecttest.services.ServiceLocator.userService

class ChallengesViewModel : ViewModel() {

    fun getAllJoinableChallenges(userId: String): LiveData<List<Challenge>?>
    {
        return userService.getAllJoinableChallenges(userId)
    }
    fun getUserByUserId(userId: String): LiveData<User>
    {
        return userService.getUserById(userId)
    }
}
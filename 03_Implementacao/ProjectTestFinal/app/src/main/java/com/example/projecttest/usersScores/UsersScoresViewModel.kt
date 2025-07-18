package com.example.projecttest.usersScores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projecttest.data.model.User
import com.example.projecttest.services.ServiceLocator

class UsersScoresViewModel : ViewModel() {
    private val userService = ServiceLocator.userService

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    fun getAllUsersByTotalPoints(): LiveData<List<User>>
    {
        return userService.getAllUsersByTotalPoints()
    }
}
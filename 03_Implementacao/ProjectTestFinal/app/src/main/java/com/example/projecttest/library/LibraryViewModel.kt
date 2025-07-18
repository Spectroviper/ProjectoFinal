package com.example.projecttest.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projecttest.data.model.Game
import com.example.projecttest.services.mock.AppDomain
import com.example.projecttest.data.model.Achievement
import com.example.projecttest.data.model.User
import com.example.projecttest.data.model.UserGame
import com.example.projecttest.services.ServiceLocator
import com.example.projecttest.services.ServiceLocator.userService

class LibraryViewModel : ViewModel() {


    fun searchUserGames(userId: String, gameName: String): LiveData<List<UserGame>>
    {
        return userService.searchUserGames(userId, gameName)
    }

    fun getUserByUserId(userId: String): LiveData<User> {

        return userService.getUserById(userId)
    }

    fun getUserUserGames(userId: String): LiveData<List<UserGame>> {
        return userService.getUserUserGames(userId)
    }
}
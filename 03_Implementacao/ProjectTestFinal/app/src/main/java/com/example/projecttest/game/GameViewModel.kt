package com.example.projecttest.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.projecttest.services.mock.AppDomain
import com.example.projecttest.data.model.Achievement
import com.example.projecttest.data.model.Game
import com.example.projecttest.data.model.Message
import com.example.projecttest.data.model.User
import com.example.projecttest.services.ServiceLocator

class GameViewModel : ViewModel() {
    private val userService = ServiceLocator.userService

    fun getGameByGameId(gameId: String): LiveData<Game?> {
        val result = MediatorLiveData<Game?>()

        val source = userService.getGameByGameId(gameId)

        result.addSource(source) { game ->
            if (game != null) {
                result.value = game
                result.removeSource(source)
            }
        }

        return result
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

    fun deleteGame(gameId: String): LiveData<Message>
    {
        return userService.deleteGame(gameId)
    }

    fun addGameToUser(userId: String, gameId: String): LiveData<Message>
    {
        return userService.addGameToUser(userId,gameId)
    }
}
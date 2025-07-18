package com.example.projecttest.gameAdded

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.projecttest.data.model.Game
import com.example.projecttest.data.model.Message
import com.example.projecttest.data.model.User
import com.example.projecttest.data.model.UserAchievement
import com.example.projecttest.data.model.UserGame
import com.example.projecttest.services.ServiceLocator

class GameAddedViewModel : ViewModel() {
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

    fun getUserGameByUserGameId(userGameId: String): LiveData<UserGame?> {
        val result = MediatorLiveData<UserGame?>()

        val source = userService.getUserGameByUserGameId(userGameId)

        result.addSource(source) { userGame ->
            if (userGame != null) {
                result.value = userGame
                result.removeSource(source)
            }
        }

        return result
    }

    fun getUserUserAchievements(userId: String): LiveData<List<UserAchievement>?>
    {
        return userService.getUserUserAchievements(userId)
    }

    fun deleteGame(gameId: String): LiveData<Message>
    {
        return userService.deleteGame(gameId)
    }

    fun addGameToUser(userId: String, gameId: String): LiveData<Message>
    {
        return userService.addGameToUser(userId,gameId)
    }

    fun removeGameFromUser(userId: String, gameId: String): LiveData<Message>
    {
        return userService.removeGameFromUser(userId,gameId)
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
}
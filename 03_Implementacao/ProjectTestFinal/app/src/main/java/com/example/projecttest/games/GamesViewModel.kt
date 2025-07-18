package com.example.projecttest.games

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.projecttest.data.model.Game
import com.example.projecttest.data.model.Message
import com.example.projecttest.services.mock.AppDomain
import com.example.projecttest.data.model.User
import com.example.projecttest.services.ServiceLocator
import com.example.projecttest.services.ServiceLocator.userService
import com.google.firebase.events.Publisher

class GamesViewModel : ViewModel() {

    fun getAllGames(): LiveData<List<Game>>
    {
        return userService.getAllGames()
    }

    fun searchGames(gameName: String? = null, console: String? = null, developer: String? = null, publisher: String? = null, genre: String? = null): LiveData<List<Game>>
    {
        return userService.searchGames(gameName, console, developer, publisher, genre)
    }

    fun getUserByUserId(userId: String): LiveData<User>
    {
        return userService.getUserById(userId)
    }
}
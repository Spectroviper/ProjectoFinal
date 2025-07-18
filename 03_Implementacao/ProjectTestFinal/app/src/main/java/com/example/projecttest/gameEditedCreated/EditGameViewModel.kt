package com.example.projecttest.gameEditedCreated

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.projecttest.data.model.Challenge
import com.example.projecttest.data.model.Game
import com.example.projecttest.data.model.Message
import com.example.projecttest.data.model.User
import com.example.projecttest.services.ServiceLocator.userService


class EditGameViewModel : ViewModel() {

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
        return userService.getUserById(userId)
    }


    fun updateGame(
        gameId: String,
        newGameName: String,
        newAbout: String,
        newConsole: String,
        newDeveloper: String,
        newPublisher: String,
        newGenre: String,
        newReleaseDate: String,
        newImage: String
    ): LiveData<Message> {
        val result = MediatorLiveData<Message>()

        val source = userService.updateGame(
            gameId,
            newGameName,
            newAbout,
            newConsole,
            newDeveloper,
            newPublisher,
            newGenre,
            newReleaseDate,
            newImage
        )

        result.addSource(source) { message ->
            if (message.successfull == true) {
                result.value = message
                result.removeSource(source)
            }
        }

        return result
    }

    fun uploadGameImage(uri: Uri, context: Context): LiveData<String?> {
        return userService.uploadImage(uri, context)
    }

    fun getChallengeByChallengeId(challengeId: String): LiveData<Challenge?> {
        return userService.getChallengeByChallengeId(challengeId)
    }

}
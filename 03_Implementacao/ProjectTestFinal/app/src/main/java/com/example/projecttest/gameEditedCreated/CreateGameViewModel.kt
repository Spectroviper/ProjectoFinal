package com.example.projecttest.gameEditedCreated

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.projecttest.data.model.Message
import com.example.projecttest.data.model.User
import com.example.projecttest.services.ServiceLocator.userService
import kotlin.coroutines.coroutineContext


class CreateGameViewModel : ViewModel() {

    fun createGame(
        gameName: String,
        about: String,
        console: String,
        developer: String,
        publisher: String,
        genre: String,
        createdBy: String,
        releaseDate: String,
        image: String
    ): LiveData<Message> {
        val result = MediatorLiveData<Message>()

        val source = userService.createGame(
            gameName, about, console, developer, publisher,
            genre, createdBy, releaseDate, image
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
}
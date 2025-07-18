package com.example.projecttest.userEdited

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.projecttest.data.model.Message
import com.example.projecttest.data.model.User
import com.example.projecttest.services.ServiceLocator.userService


class EditUserViewModel : ViewModel() {

    fun uploadUserImage(uri: Uri, context: Context): LiveData<String?> {
        return userService.uploadImage(uri, context)
    }

    fun updateUser(
        userId: String,
        newUserName: String,
        newBiography: String,
        newImage: String
    ): LiveData<Message> {
        val result = MediatorLiveData<Message>()

        val source = userService.updateUser(
            userId, newUserName, newBiography, newImage
        )

        result.addSource(source) { message ->
            if (message.successfull == true) {
                result.value = message
                result.removeSource(source)
            }
        }
        return result
    }
}
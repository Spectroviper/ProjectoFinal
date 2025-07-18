package com.example.projecttest.friendListInvites

import android.support.v4.os.ResultReceiver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttest.data.model.Friend
import com.example.projecttest.data.model.Game
import com.example.projecttest.data.model.Message
import com.example.projecttest.data.model.User
import com.example.projecttest.services.ServiceLocator.userService
import kotlinx.coroutines.launch


class FriendListInvitesViewModel : ViewModel() {

    fun acceptFriendInvite(friendId: String) : LiveData<Message>
    {
        return userService.acceptFriendInvite(friendId)
    }

    fun rejectFriendInvite(friendId: String) : LiveData<Message>
    {
        return userService.rejectFriendInvite(friendId)
    }

    fun deleteFriendInvite(friendId: String) : LiveData<Message>
    {
        return userService.deleteFriendInvite(friendId)
    }

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
    fun getAllFriendInvites(userId: String): LiveData<List<Friend>> {

        return userService.getAllFriendInvites(userId)
    }

    fun getUserByUserId(userId: String): LiveData<User> {
        return userService.getUserById(userId)
    }
}
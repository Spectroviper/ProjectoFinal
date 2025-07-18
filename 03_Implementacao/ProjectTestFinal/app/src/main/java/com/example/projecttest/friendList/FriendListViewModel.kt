package com.example.projecttest.friendList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttest.data.model.Friend
import com.example.projecttest.services.mock.AppDomain
import com.example.projecttest.data.model.User
import com.example.projecttest.services.ServiceLocator
import com.example.projecttest.services.ServiceLocator.userService
import kotlinx.coroutines.launch

class FriendListViewModel : ViewModel() {


    fun searchUsers(userName: String): LiveData<List<User>> {
        return userService.searchUsers(userName)
    }

    fun getAllFriendInvites(userId: String): LiveData<List<Friend>>
    {
        return userService.getAllFriendInvites(userId)
    }

    fun getAllUserFriends(userId: String): LiveData<List<User>>
    {
        return userService.getAllUserFriends(userId)
    }

    fun getUserByUserId(userId: String): LiveData<User>
    {
        return userService.getUserById(userId)
    }
    /*fun getUserId(): String {
        return SessionManager.currentUser?.id ?: ""
    }*/

    /*fun getSpecificUser(): Int{
        return _appDomain.getSpecificUser()
    }*/
}
package com.example.projecttest.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttest.data.model.Game
import com.example.projecttest.services.mock.AppDomain
import com.example.projecttest.MyId
import com.example.projecttest.data.model.Challenge
import com.example.projecttest.data.model.Message
import com.example.projecttest.data.model.User
import com.example.projecttest.services.DataSource
import com.example.projecttest.services.ServiceLocator
import com.example.projecttest.services.ServiceLocator.userService
import com.example.projecttest.services.UserService
import kotlinx.coroutines.launch

class UsersViewModel : ViewModel() {


    fun searchUsers(userName: String): LiveData<List<User>> {
        return userService.searchUsers(userName)
    }

    fun getAllUsers(userId: String): LiveData<List<User>>
    {
        val result = MediatorLiveData<List<User>>()

        val source = userService.getAllUsers(userId)

        result.addSource(source) { allUsers ->
            if (allUsers != null) {
                result.value = allUsers
                result.removeSource(source) // Stop observing after first value
            }
        }

        return result
    }

    fun getUserByUserId(userId: String): LiveData<User> {

        val result = MediatorLiveData<User>()

        val source = userService.getUserById(userId)

        result.addSource(source) { user ->
            if (user != null) {
                result.value = user
                result.removeSource(source) // Stop observing after first value
            }
        }

        return result
    }
}
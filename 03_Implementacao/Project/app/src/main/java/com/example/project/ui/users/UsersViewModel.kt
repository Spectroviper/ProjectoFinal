package com.example.project.ui.users

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.generated.GetAllPersonsQuery
import com.example.project.AppDomain
import com.example.project.MyApolloClient
import com.example.project.SessionManager
import com.example.project.data.model.Game
import com.example.project.data.model.User
import kotlinx.coroutines.launch

class UsersViewModel : ViewModel() {
    private val appDomain = AppDomain()

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    /*fun getAllUsers(): LiveData<List<User>>
    {
        return _appDomain.getAllUsers()
    }*/
    fun fetchAllUsers() {
        viewModelScope.launch {
            val usersList = appDomain.getAllUsers()
            //_users.postValue(usersList)
        }
    }
    /*fun getUserId(): Int{
        return _appDomain.getUserId()
    }*/
    fun getUserId(): String {
        return SessionManager.currentUser?.id ?: ""
    }

    /*fun getSpecificUser(): Int{
        return _appDomain.getSpecificUser()
    }*/
}
package com.example.project.ui.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project.AppDomain
import com.example.project.data.model.Game
import com.example.project.data.model.User

class UsersViewModel : ViewModel() {
    private val _appDomain = AppDomain()
    fun getAllUsers(): LiveData<List<User>>
    {
        return _appDomain.getAllUsers()
    }
}
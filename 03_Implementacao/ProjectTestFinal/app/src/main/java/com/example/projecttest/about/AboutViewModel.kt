package com.example.projecttest.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.projecttest.services.mock.AppDomain
import com.example.projecttest.data.model.User

class AboutViewModel : ViewModel(){
    private val appDomain = AppDomain()
    fun getUserByUserId(userId: String): LiveData<User>
    {
        return appDomain.getUserById(userId)
    }
}
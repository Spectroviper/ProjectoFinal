package com.example.project.ui.myProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.project.AppDomain
import com.example.project.data.model.User

class MyProfileViewModel : ViewModel() {
    private val appDomain = AppDomain()
    private lateinit var user : LiveData<User>
    private lateinit var userDetail : LiveData<User>
    fun getUserDetail(gameId: Int): LiveData<User>
    {
        userDetail = appDomain.getUserDetail(gameId)
        return userDetail
    }
}
package com.example.project.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.project.AppDomain
import com.example.project.data.model.Game

class HomeViewModel : ViewModel() {
    private val _appDomain = AppDomain()
    fun getMyGames(): LiveData<List<Game>>
    {
        return _appDomain.getAllMyGames()
    }
}
package com.example.project.ui.games

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project.AppDomain
import com.example.project.data.model.Game

class GamesViewModel : ViewModel() {
    private val _appDomain = AppDomain()
    fun getAllGames(): LiveData<List<Game>>
    {
        return _appDomain.getAllGames()
    }
}
package com.example.project.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.AppDomain
import com.example.project.data.model.Game
import com.example.project.data.model.User
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _appDomain = AppDomain()

    private val _myGames = MutableLiveData<List<Game>>()
    val myGames: LiveData<List<Game>> get() = _myGames
    /*fun getMyGames(): LiveData<List<Game>>
    {
        return _appDomain.getAllMyGames()
    }*/
    fun fetchAllMyGames() {
        viewModelScope.launch {
            val myGamesList = _appDomain.getAllMyGames()
            _myGames.postValue(myGamesList)
        }
    }
}
package com.example.project

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.project.data.mock.AppMock
import com.example.project.data.model.Achievement
import com.example.project.data.model.Game
import com.example.project.data.model.User

class AppDomain
{
    val id: Int = 1

    fun getAllUsers() : LiveData<List<User>>
    {
        return MutableLiveData<List<User>>(AppMock.users)
    }

    fun getAllUsersScores() : MutableList<Int>
    {
        val indexes = mutableListOf<Int>()
        for(i in AppMock.users){
            indexes.add(i.siteRank)
        }
        var temp: Int
        for (i in 0 until indexes.size) {
            for (j in i + 1 until indexes.size) {
                if (indexes[i] > indexes[j]) {
                    temp = indexes[i]
                    indexes[i] = indexes[j]
                    indexes[j] = temp
                }
            }
        }
        return indexes
    }
    fun getAllScores() : LiveData<List<User>>
    {
        val myScores = mutableListOf<User>()
        for(j in getAllUsersScores()){
            for(i in AppMock.users) {
                if (i.siteRank == j) {
                    myScores.add(i)
                }
            }
        }

        return MutableLiveData<List<User>>(myScores)
    }
    /*fun getAllScores() : LiveData<List<User>>
    {
        val users = MutableLiveData<List<User>>(AppMock.users)
        users.
        return MutableLiveData<List<User>>(AppMock.users).getAllUsersScores()})
    }*/
    fun getAllGameAchievements(gameId: Int): LiveData<List<Achievement>>
    {
        return MutableLiveData<List<Achievement>>(AppMock.achievements.filter {
            it.gameId == gameId })
    }
    fun getAllGames(): LiveData<List<Game>>
    {
        return MutableLiveData<List<Game>>(AppMock.games)
    }
    fun getUserGames(): MutableList<Int> {
        val indexes = mutableListOf<Int>()
        for(i in AppMock.userGame){
            if(id==i.userId){
                indexes.add(i.gameId)
            }
        }
        return indexes
    }
    fun getLastGameIndex(): MutableList<Int> {
        val indexes = mutableListOf<Int>()
        for(i in AppMock.userGame){
            if(id==i.userId && i.lastPlayedGame==true){
                indexes.add(i.gameId)
            }
        }
        return indexes
    }
    fun getAllMyGames(): LiveData<List<Game>>
    {
        val myGames = mutableListOf<Game>()
        for(i in AppMock.games){
            for(j in getUserGames()) {
                if (i.id == j) {
                    myGames.add(i)
                }
            }
        }

        return MutableLiveData<List<Game>>(myGames)
    }
    fun getLastGame(): LiveData<List<Game>>
    {
        val lastGame = mutableListOf<Game>()
        for(i in AppMock.games){
            for(j in getLastGameIndex()) {
                if (i.id == j) {
                    lastGame.add(i)
                }
            }
        }

        return MutableLiveData<List<Game>>(lastGame)
    }
    fun getAchievementDetail(gameId: Int): LiveData<Achievement> {
        return MutableLiveData(
            AppMock.achievements.find { it.gameId == gameId })
    }
    fun getUserDetail(userId: Int): LiveData<User> {
        return MutableLiveData(
            AppMock.users.find { it.id == userId })
    }
    /*fun defineUserId(newUserId: Int){
        userId = newUserId
    }*/
    fun getUserId(): Int{
        return id
    }

    fun getSpecificUser(): Int{
        var finalIndex: Int = 0
        var users = mutableListOf<User>()
        for (i in users){
            if (i.id == id){
                finalIndex = users.indexOf(i)
            }
        }
        return finalIndex
    }
    /*
    fun getPokemonsByRegion(region: PokemonRegion): LiveData<List<Pokemon>>
    {
        return MutableLiveData<List<Pokemon>>(PokemonMockData.pokemons.filter { it.
        region == region })
    }
    fun getPokemonTypes(): List<PokemonType>
    {
        return ArrayList<PokemonType>(PokemonMockData.pokemonTypeMock)
    }
    fun getPokemonDetail(pokemon:Pokemon): LiveData<PokemonDetail>
    {
        return MutableLiveData(
            12
                    Tutorial 3 - Android Jetpack
                    Figure 7: Region Screen.
            PokemonMockData.pokemonDetail.find { it.pokemon == pokemon })
    }*/
}
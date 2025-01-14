package com.example.project

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.generated.GetAllPersonsQuery
import com.example.generated.GetPersonQuery
import com.example.generated.SignInQuery
import com.example.project.data.mock.AppMock
import com.example.project.data.model.Achievement
import com.example.project.data.model.Game
import com.example.project.data.model.User

class AppDomain
{
    private val apolloClient = MyApolloClient.instance

    val id: Int = 1

    /*fun getAllUsers() : LiveData<List<User>>
    {
        return MutableLiveData<List<User>>(AppMock.users)
    }*/
    suspend fun getAllUsers(): List<User> {
        return try {
            val response = MyApolloClient.instance.query(GetAllPersonsQuery()).execute()
            Log.d("GraphQL", "Query: ${GetAllPersonsQuery().toString()}")

            response.data?.getAllPersons?.map { user ->
                User(
                    id = user?.id ?: "",
                    userName = user?.UserName ?: "",
                    email = user?.Email ?: "",
                    biography = user?.Biography ?: "",
                    memberSince = (user?.MemberSince ?: "").toString(),
                    lastLogin = (user?.LastLogin ?: "").toString(),
                    points = user?.TotalPoints ?: 0,
                    averageCompletion = user?.AverageCompletion ?: 0,
                    siteRank = user?.SiteRank ?: 0,
                    imageUrl = user?.Image ?: ""
                )
            } ?: emptyList()
        } catch (e: Exception) {
            Log.d("GraphQL", "Query: ${GetAllPersonsQuery().toString()}")

            Log.e("API", "Error fetching users", e)
            emptyList()
        }
    }

    /*fun getAllUsersScores() : MutableList<Int>
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
    }*/
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
    /*fun getAllGames(): LiveData<List<Game>>
    {
        return MutableLiveData<List<Game>>(AppMock.games)
    }*/
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
    fun getAllMyGames(): List<Game> {
        Log.d("API", SessionManager.currentUser.toString())
        return try {
            Log.d("API", SessionManager.currentUser.toString())
            SessionManager.currentUser?.Plays?.map { game ->
                Game(
                    id = game?.id ?: "",
                    gameName = game?.GameName ?: "",
                    about = game?.About ?: "",
                    console = game?.Console ?: "",
                    developer = game?.Developer ?: "",
                    publisher = game?.Publisher ?: "",
                    genre = game?.Genre ?: "",
                    releaseDate = (game?.ReleaseDate ?: "").toString(),
                    imageUrl = game?.Image ?: ""
                )
            } ?: emptyList()
        } catch (e: Exception) {
            Log.e("API", "Error fetching myGames", e)
            emptyList()
        }
    }


    /*fun getLastGame(): LiveData<List<Game>>
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
    }*/
    fun getAchievementDetail(gameId: Int): LiveData<Achievement> {
        return MutableLiveData(
            AppMock.achievements.find { it.gameId == gameId })
    }
    suspend fun getUserDetail(email: String): User {
        try {
            val response = apolloClient.query(GetPersonQuery(email)).execute()
            val returnedUser = response.data?.getPerson
            return User(
                    id = returnedUser?.id ?: "",
                    userName = returnedUser?.UserName ?: "",
                    email = returnedUser?.Email ?: "",
                    biography = returnedUser?.Biography ?: "",
                    memberSince = (returnedUser?.MemberSince ?: "").toString(),
                    lastLogin = (returnedUser?.LastLogin ?: "").toString(),
                    points = returnedUser?.TotalPoints ?: 0,
                    averageCompletion = returnedUser?.AverageCompletion ?: 0,
                    siteRank = returnedUser?.SiteRank ?: 0,
                    imageUrl = returnedUser?.Image ?: ""
                )
        } catch (e: Exception) {
            Log.e("API", "Error fetching user", e)
        }
        return User("", "", "", "", 0, 0, 0, "", "", "")
        /*return MutableLiveData(
            AppMock.users.find { it.id == userId })*/
    }
    /*fun defineUserId(newUserId: Int){
        userId = newUserId
    }*/
    fun getUserId(): Int{
        return id
    }

    /*fun getSpecificUser(): Int{
        var finalIndex: Int = 0
        var users = mutableListOf<User>()
        for (i in users){
            if (i.id == id){
                finalIndex = users.indexOf(i)
            }
        }
        return finalIndex
    }*/
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
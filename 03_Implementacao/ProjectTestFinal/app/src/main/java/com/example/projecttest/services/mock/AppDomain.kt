package com.example.projecttest.services.mock

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.projecttest.MyApplication
import com.example.projecttest.MyId
import com.example.projecttest.data.mock.AppMock
import com.example.projecttest.data.model.Achievement
import com.example.projecttest.data.model.Challenge
import com.example.projecttest.data.model.ChallengeAchievement
import com.example.projecttest.data.model.ChallengeInvite
import com.example.projecttest.data.model.DetailedUserChallenge
import com.example.projecttest.data.model.Friend
import com.example.projecttest.data.model.Game
import com.example.projecttest.data.model.Message
import com.example.projecttest.data.model.User
import com.example.projecttest.data.model.UserAchievement
import com.example.projecttest.data.model.UserChallenge
import com.example.projecttest.data.model.UserChallengeAchievement
import com.example.projecttest.data.model.UserGame
import com.example.projecttest.services.UserService

class AppDomain: UserService
{

    //all Queries

    /*override fun getAllAchievements(): LiveData<List<Achievement>?> {
        return MutableLiveData(AppMock.achievements)
    }*/

    override fun getAchievementByAchievementId(achievementId: String): LiveData<Achievement?> {
        return MutableLiveData(AppMock.achievements.find { it.achievementId == achievementId })
    }

    override fun getAllAchievementsNotInAChallenge(challengeId: String): LiveData<List<Achievement>?> {
        /*val includedIds = AppMock.challengeAchievements.filter { it.challengeId == challengeId }
            .map { it.achievementId }
        return MutableLiveData(AppMock.achievements.filter { it.achievementId !in includedIds })
        */
        TODO("Not yet implemented")
    }

    override fun searchAchievements(
        achievementName: String?,
        gameId: String?,
        challengeId: String
    ): LiveData<List<Achievement>> {
        /*
        val challengeAchievementIds = AppMock.challengeAchievements
            .filter { it.challengeId == challengeId }
            .map { it.achievementId }

        val results = AppMock.achievements.filter {
            (achievementName == null || it.achievementName?.contains(achievementName, ignoreCase = true) == true) &&
                    (gameId == null || it.gameId == gameId) &&
                    it.achievementId !in challengeAchievementIds
        }
        return MutableLiveData(results)*/
        TODO("Not yet implemented")
    }

    override fun getUserGameByUserGameId(userGameId: String): LiveData<UserGame?> {
        TODO("Not yet implemented")
    }

    override fun getUserAchievementByUserAchievementId(userAchievementId: String): LiveData<UserAchievement?> {
        TODO("Not yet implemented")
    }



    override fun getUserUserAchievements(userId: String): LiveData<List<UserAchievement>?> {
        TODO("Not yet implemented")
    }

    override fun getAllUserChallengeInvites(userId: String): LiveData<List<ChallengeInvite>?> {
        TODO("Not yet implemented")
    }

    override fun getAllChallengeInvitesByChallengeId(challengeId: String): LiveData<List<ChallengeInvite>?> {
        TODO("Not yet implemented")
    }

    override fun getAllJoinableChallenges(userId: String): LiveData<List<Challenge>?> {
        /*val joined = AppMock.userChallenges.filter { it.userId == userId }.map { it.challengeId }
        return MutableLiveData(AppMock.challenges.filter { it.challengeId !in joined })
        */
        TODO("Not yet implemented")
    }

    override fun getChallengeByChallengeId(challengeId: String): LiveData<Challenge?> {
        return MutableLiveData(AppMock.challenges.find { it.challengeId == challengeId })
    }

    override fun searchChallenges(challengeName: String?, type: String?): LiveData<List<Challenge>> {
        val results = AppMock.challenges.filter {
            (challengeName == null || it.challengeName?.contains(challengeName, ignoreCase = true) == true) &&
                    (type == null || it.type.equals(type, ignoreCase = true))
        }
        return MutableLiveData(results)
    }

    override fun getUserChallengeByUserChallengeId(userChallengeId: String): LiveData<DetailedUserChallenge?> {
        TODO("Not yet implemented")
    }

    override fun getUserChallengeAchievementByUserChallengeAchievementId(userChallengeAchievementId: String): LiveData<UserChallengeAchievement?> {
        TODO("Not yet implemented")
    }

    override fun getChallengeAchievementByChallengeAchievementId(challengeAchievementId: String): LiveData<ChallengeAchievement?> {
        TODO("Not yet implemented")
    }

    override fun getAllFriendInvites(userId: String): LiveData<List<Friend>> {
        TODO("Not yet implemented")
    }

    override fun searchFriends(
        userId: String,
        userName: String?
    ): LiveData<List<Friend>?> {
        TODO("Not yet implemented")
    }

    override fun getAllGames(): LiveData<List<Game>> {
        return MutableLiveData(AppMock.games)
    }

    override fun getGameByGameId(gameId: String): LiveData<Game?> {
        return MutableLiveData(AppMock.games.find { it.gameId == gameId })
    }

    override fun searchGames(
        gameName: String?,
        console: String?,
        developer: String?,
        publisher: String?,
        genre: String?
    ): LiveData<List<Game>> {
        return MutableLiveData(AppMock.games.filter {
            (gameName == null || it.gameName?.contains(gameName, ignoreCase = true) == true) &&
                    (console == null || it.console.equals(console, ignoreCase = true)) &&
                    (developer == null || it.developer.equals(developer, ignoreCase = true)) &&
                    (publisher == null || it.publisher.equals(publisher, ignoreCase = true)) &&
                    (genre == null || it.genre.equals(genre, ignoreCase = true))
        })
    }

    override fun getAllUsers(userId: String): LiveData<List<User>> {
        return MutableLiveData(AppMock.users)
    }

    override fun getUserById(userId: String): LiveData<User> {
        return MutableLiveData(AppMock.users.find { it.userId == userId })
    }

    override fun getUserUserGames(userId: String): LiveData<List<UserGame>> {
        TODO("Not yet implemented")
    }

    override fun getAllUsersByTotalPoints(): LiveData<List<User>> {
        return MutableLiveData(AppMock.users.sortedByDescending { it.totalPoints })
    }

    override fun getAllUserFriends(userId: String): LiveData<List<User>> {
        /*
        val userFriends = AppMock.friends.filter {
            (it.userSenderId == userId || it.userReceiverId == userId) && it.state == "Friends"
        }.mapNotNull { friend ->
            val friendId = if (friend.userSenderId == userId) friend.userReceiverId else friend.userSenderId
            AppMock.users.find { it.userId == friendId }
        }*/
        TODO("Not yet implemented")
        //return null
    }

    override fun getAllUsersNotInChallenge(challengeId: String): LiveData<List<User>?> {
        TODO("Not yet implemented")
    }

    override fun searchUsers(userName: String?): LiveData<List<User>> {
        return MutableLiveData(AppMock.users.filter {
            userName == null || it.userName?.contains(userName, ignoreCase = true) == true
        })
    }

    override fun searchUserGames(userId: String, gameName: String?): LiveData<List<UserGame>> {
        val filteredUserGames = AppMock.userGames.filter { userGame ->
            userGame.userId == userId &&
                    (gameName == null || AppMock.games.find { it.gameId == userGame.gameId }?.gameName?.contains(gameName, ignoreCase = true) == true)
        }
        return MutableLiveData(filteredUserGames)
    }

    //All Mutations

    override fun createAchievement(
        achievementName: String,
        about: String,
        totalCollectable: Int,
        createdBy: String,
        image: String,
        gameId: String
    ): LiveData<Message> {
        return MutableLiveData<Message>() // Placeholder
    }

    override fun updateAchievement(
        achievementId: String,
        newAchievementName: String,
        newAbout: String,
        newTotalCollectable: Int,
        newImage: String
    ): LiveData<Message> {
        return MutableLiveData<Message>() // Placeholder
    }

    override fun deleteAchievement(
        achievementId: String
    ): LiveData<Message> {
        return MutableLiveData<Message>() // Placeholder
    }

    override fun lockOrUnlockAchievement(
        userId: String,
        userAchievementId: String
    ): LiveData<Message> {
        return MutableLiveData<Message>() // Placeholder
    }

    override fun lockOrUnlockChallengeAchievement(
        userId: String,
        userChallengeAchievementId: String
    ): LiveData<Message> {
        TODO("Not yet implemented")
    }

    override fun createChallenge(
        userId: String,
        challengeName: String,
        type: String,
        image: String
    ): LiveData<Message> {
        return MutableLiveData<Message>() // Placeholder
    }

    override fun updateChallenge(
        challengeId: String,
        newChallengeName: String,
        newType: String,
        newImage: String
    ): LiveData<Message> {
        return MutableLiveData<Message>() // Placeholder
    }

    override fun deleteChallenge(
        challengeId: String
    ): LiveData<Message> {
        return MutableLiveData<Message>() // Placeholder
    }

    override fun startChallenge(
        userId: String,
        challengeId: String
    ): LiveData<Message> {
        TODO("Not yet implemented")
    }

    override fun endChallenge(challengeId: String): LiveData<Message> {
        TODO("Not yet implemented")
    }

    override fun createChallengeAchievement(
        challengeId: String,
        achievementId: String
    ): LiveData<Message> {
        return MutableLiveData<Message>() // Placeholder
    }

    override fun deleteChallengeAchievement(
        challengeAchievementId: String
    ): LiveData<Message> {
        return MutableLiveData<Message>() // Placeholder
    }

    override fun acceptChallengeInvite(
        userId: String,
        challengeInviteId: String
    ): LiveData<Message> {
        return MutableLiveData<Message>() // Placeholder
    }

    override fun rejectChallengeInvite(
        userId: String,
        challengeInviteId: String
    ): LiveData<Message> {
        return MutableLiveData<Message>() // Placeholder
    }

    override fun deleteChallengeInvite(
        challengeInviteId: String
    ): LiveData<Message> {
        return MutableLiveData<Message>() // Placeholder
    }

    override fun inviteFriend(
        senderId: String,
        receiverId: String
    ): LiveData<Message> {
        return MutableLiveData<Message>() // Placeholder
    }

    override fun acceptFriendInvite(
        friendId: String
    ): LiveData<Message> {
        return MutableLiveData<Message>() // Placeholder
    }

    override fun rejectFriendInvite(friendId: String): LiveData<Message> {
        TODO("Not yet implemented")
    }

    override fun deleteFriendInvite(
        friendId: String
    ): LiveData<Message> {
        return MutableLiveData<Message>() // Placeholder
    }

    override fun uploadImage(
        imageUri: Uri,
        context: Context
    ): LiveData<String?> {
        TODO("Not yet implemented")
    }

    override fun createGame(
        gameName: String,
        about: String,
        console: String,
        developer: String,
        publisher: String,
        genre: String,
        createdBy: String,
        releaseDate: String,
        image: String
    ): LiveData<Message> {
        return MutableLiveData<Message>() // Placeholder
    }

    override fun updateGame(
        gameId: String,
        newGameName: String,
        newAbout: String,
        newConsole: String,
        newDeveloper: String,
        newPublisher: String,
        newGenre: String,
        newReleaseDate: String,
        newImage: String
    ): LiveData<Message> {
        return MutableLiveData<Message>() // Placeholder
    }

    override fun deleteGame(
        gameId: String
    ): LiveData<Message> {
        return MutableLiveData<Message>() // Placeholder
    }

    override fun addGameToUser(
        userId: String,
        gameId: String
    ): LiveData<Message> {
        return MutableLiveData<Message>() // Placeholder
    }

    override fun removeGameFromUser(
        userId: String,
        gameId: String
    ): LiveData<Message> {
        return MutableLiveData<Message>() // Placeholder
    }

    override fun toggleChallengeAchievementClearState(
        userId: String,
        userChallengeAchievementId: String
    ): LiveData<Message> {
        return MutableLiveData<Message>() // Placeholder for actual implementation
    }

    override fun inviteUserToChallenge(
        userId: String,
        challengeId: String,
        isRequest: Boolean
    ): LiveData<Message> {
        TODO("Not yet implemented")
    }

    override fun createUser(
        userName: String?,
        email: String?,
        biography: String?,
        image: String?
    ): LiveData<Message> {

        return MutableLiveData<Message>()
    }

    // UPDATE_USER
    override fun updateUser(
        userId: String?,
        newUserName: String?,
        newBiography: String?,
        newImage: String?
    ): LiveData<Message> {

        return MutableLiveData<Message>()
    }


    override fun deleteUser(userId: String?): LiveData<Message> {

        return MutableLiveData<Message>()
    }

    override fun logIn(email: String): LiveData<User?> {
        return MutableLiveData<User?>(AppMock.users.find { it.email == email })
    }
}
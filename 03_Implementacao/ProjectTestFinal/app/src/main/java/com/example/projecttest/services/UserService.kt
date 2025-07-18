package com.example.projecttest.services

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
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

interface UserService {

    // Queries
    fun getAllUsers(userId: String): LiveData<List<User>>
    fun getUserById(userId: String): LiveData<User>
    fun getUserUserGames(userId: String): LiveData<List<UserGame>>
    fun getAllUsersByTotalPoints(): LiveData<List<User>>
    fun getAllUserFriends(userId: String): LiveData<List<User>>
    fun getAllUsersNotInChallenge(challengeId: String): LiveData<List<User>?>
    fun searchUsers(userName: String?): LiveData<List<User>>
    fun searchUserGames(userId: String, gameName: String?): LiveData<List<UserGame>>

    fun getAllGames(): LiveData<List<Game>>
    fun getGameByGameId(gameId: String): LiveData<Game?>
    fun searchGames(
        gameName: String?,
        console: String?,
        developer: String?,
        publisher: String?,
        genre: String?
    ): LiveData<List<Game>>

    fun getAchievementByAchievementId(achievementId: String): LiveData<Achievement?>
    fun getAllAchievementsNotInAChallenge(challengeId: String): LiveData<List<Achievement>?>
    fun searchAchievements(
        achievementName: String?,
        gameId: String?,
        challengeId: String
    ): LiveData<List<Achievement>>

    fun getUserGameByUserGameId(userGameId: String): LiveData<UserGame?>
    fun getUserAchievementByUserAchievementId(userAchievementId: String): LiveData<UserAchievement?>

    fun getUserUserAchievements(userId: String): LiveData<List<UserAchievement>?>

    fun getAllUserChallengeInvites(userId: String): LiveData<List<ChallengeInvite>?>
    fun getAllChallengeInvitesByChallengeId(challengeId: String): LiveData<List<ChallengeInvite>?>

    fun getAllJoinableChallenges(userId: String): LiveData<List<Challenge>?>
    fun getChallengeByChallengeId(challengeId: String): LiveData<Challenge?>
    fun searchChallenges(challengeName: String?, type: String?): LiveData<List<Challenge>>

    fun getUserChallengeByUserChallengeId(userChallengeId: String): LiveData<DetailedUserChallenge?>
    fun getUserChallengeAchievementByUserChallengeAchievementId(userChallengeAchievementId: String): LiveData<UserChallengeAchievement?>
    fun getChallengeAchievementByChallengeAchievementId(challengeAchievementId: String): LiveData<ChallengeAchievement?>

    fun getAllFriendInvites(userId: String): LiveData<List<Friend>>
    fun searchFriends(userId: String, userName: String?): LiveData<List<Friend>?>

    // Mutations
    fun createUser(
        userName: String?,
        email: String?,
        biography: String?,
        image: String?
    ): LiveData<Message>

    fun deleteUser(userId: String?): LiveData<Message>

    fun updateUser(
        userId: String?,
        newUserName: String?,
        newBiography: String?,
        newImage: String?
    ): LiveData<Message>

    fun logIn(email: String): LiveData<User?>

    fun createGame(
        gameName: String,
        about: String,
        console: String,
        developer: String,
        publisher: String,
        genre: String,
        createdBy: String,
        releaseDate: String,
        image: String
    ): LiveData<Message>

    fun deleteGame(gameId: String): LiveData<Message>

    fun updateGame(
        gameId: String,
        newGameName: String,
        newAbout: String,
        newConsole: String,
        newDeveloper: String,
        newPublisher: String,
        newGenre: String,
        newReleaseDate: String,
        newImage: String
    ): LiveData<Message>

    fun addGameToUser(userId: String, gameId: String): LiveData<Message>
    fun removeGameFromUser(userId: String, gameId: String): LiveData<Message>

    fun createAchievement(
        achievementName: String,
        about: String,
        totalCollectable: Int,
        createdBy: String,
        image: String,
        gameId: String
    ): LiveData<Message>

    fun deleteAchievement(achievementId: String): LiveData<Message>

    fun updateAchievement(
        achievementId: String,
        newAchievementName: String,
        newAbout: String,
        newTotalCollectable: Int,
        newImage: String
    ): LiveData<Message>

    fun lockOrUnlockAchievement(userId: String, userAchievementId: String): LiveData<Message>
    fun lockOrUnlockChallengeAchievement(userId: String, userChallengeAchievementId: String): LiveData<Message>

    fun createChallengeAchievement(challengeId: String, achievementId: String): LiveData<Message>
    fun deleteChallengeAchievement(challengeAchievementId: String): LiveData<Message>
    fun toggleChallengeAchievementClearState(
        userId: String,
        userChallengeAchievementId: String
    ): LiveData<Message>

    fun inviteUserToChallenge(
        userId: String,
        challengeId: String,
        isRequest: Boolean
    ): LiveData<Message>

    fun acceptChallengeInvite(challengeInviteId: String, userId: String): LiveData<Message>
    fun rejectChallengeInvite(challengeInviteId: String, userId: String): LiveData<Message>
    fun deleteChallengeInvite(challengeInviteId: String): LiveData<Message>

    fun createChallenge(
        userId: String,
        challengeName: String,
        type: String,
        image: String
    ): LiveData<Message>

    fun updateChallenge(
        challengeId: String,
        newChallengeName: String,
        newType: String,
        newImage: String
    ): LiveData<Message>

    fun deleteChallenge(challengeId: String): LiveData<Message>

    fun startChallenge(userId: String, challengeId: String): LiveData<Message>
    fun endChallenge(challengeId: String): LiveData<Message>

    fun inviteFriend(senderId: String, receiverId: String): LiveData<Message>
    fun acceptFriendInvite(friendId: String): LiveData<Message>
    fun rejectFriendInvite(friendId: String): LiveData<Message>
    fun deleteFriendInvite(friendId: String): LiveData<Message>

    fun uploadImage(imageUri: Uri, context: Context): LiveData<String?>
}
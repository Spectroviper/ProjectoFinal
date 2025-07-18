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
import com.example.projecttest.data.model.User
import com.example.projecttest.data.model.UserGame
import com.example.projecttest.data.model.Message
import com.example.projecttest.data.model.UserAchievement
import com.example.projecttest.data.model.UserChallenge
import com.example.projecttest.data.model.UserChallengeAchievement

class DataSource(private val service: UserService) : UserService {

    // Queries
    override fun getAllUsers(userId: String): LiveData<List<User>> = service.getAllUsers(userId)

    override fun getUserById(userId: String): LiveData<User> = service.getUserById(userId)

    override fun getUserUserGames(userId: String): LiveData<List<UserGame>> = service.getUserUserGames(userId)

    override fun getAllUsersByTotalPoints(): LiveData<List<User>> = service.getAllUsersByTotalPoints()

    override fun getAllUserFriends(userId: String): LiveData<List<User>> = service.getAllUserFriends(userId)

    override fun getAllUsersNotInChallenge(challengeId: String): LiveData<List<User>?> =
        service.getAllUsersNotInChallenge(challengeId)

    override fun searchUsers(userName: String?): LiveData<List<User>> = service.searchUsers(userName)

    override fun searchUserGames(userId: String, gameName: String?): LiveData<List<UserGame>> =
        service.searchUserGames(userId, gameName)

    override fun getAllGames(): LiveData<List<Game>> = service.getAllGames()

    override fun getGameByGameId(gameId: String): LiveData<Game?> = service.getGameByGameId(gameId)

    override fun searchGames(
        gameName: String?,
        console: String?,
        developer: String?,
        publisher: String?,
        genre: String?
    ): LiveData<List<Game>> = service.searchGames(gameName, console, developer, publisher, genre)

    override fun getAchievementByAchievementId(achievementId: String): LiveData<Achievement?> =
        service.getAchievementByAchievementId(achievementId)

    override fun getAllAchievementsNotInAChallenge(challengeId: String): LiveData<List<Achievement>?> =
        service.getAllAchievementsNotInAChallenge(challengeId)

    override fun searchAchievements(
        achievementName: String?,
        gameId: String?,
        challengeId: String
    ): LiveData<List<Achievement>> = service.searchAchievements(achievementName, gameId, challengeId)

    override fun getUserGameByUserGameId(userGameId: String): LiveData<UserGame?> =
        service.getUserGameByUserGameId(userGameId)

    override fun getUserAchievementByUserAchievementId(userAchievementId: String): LiveData<UserAchievement?> =
        service.getUserAchievementByUserAchievementId(userAchievementId)

    override fun getAllJoinableChallenges(userId: String): LiveData<List<Challenge>?> =
        service.getAllJoinableChallenges(userId)

    override fun getAllUserChallengeInvites(userId: String): LiveData<List<ChallengeInvite>?> =
        service.getAllUserChallengeInvites(userId)

    override fun getAllChallengeInvitesByChallengeId(challengeId: String): LiveData<List<ChallengeInvite>?> =
        service.getAllChallengeInvitesByChallengeId(challengeId)

    override fun getUserUserAchievements(userId: String): LiveData<List<UserAchievement>?> =
        service.getUserUserAchievements(userId)



    override fun getChallengeByChallengeId(challengeId: String): LiveData<Challenge?> =
        service.getChallengeByChallengeId(challengeId)

    override fun searchChallenges(challengeName: String?, type: String?): LiveData<List<Challenge>> =
        service.searchChallenges(challengeName, type)

    override fun getUserChallengeByUserChallengeId(userChallengeId: String): LiveData<DetailedUserChallenge?> =
        service.getUserChallengeByUserChallengeId(userChallengeId)

    override fun getUserChallengeAchievementByUserChallengeAchievementId(userChallengeAchievementId: String): LiveData<UserChallengeAchievement?> =
        service.getUserChallengeAchievementByUserChallengeAchievementId(userChallengeAchievementId)

    override fun getChallengeAchievementByChallengeAchievementId(challengeAchievementId: String): LiveData<ChallengeAchievement?> =
        service.getChallengeAchievementByChallengeAchievementId(challengeAchievementId)

    override fun getAllFriendInvites(userId: String): LiveData<List<Friend>> =
        service.getAllFriendInvites(userId)

    override fun searchFriends(userId: String, userName: String?): LiveData<List<Friend>?> =
        service.searchFriends(userId, userName)

    // Mutations
    override fun createUser(userName: String?, email: String?, biography: String?, image: String?): LiveData<Message> =
        service.createUser(userName, email, biography, image)

    override fun deleteUser(userId: String?): LiveData<Message> = service.deleteUser(userId)

    override fun updateUser(
        userId: String?,
        newUserName: String?,
        newBiography: String?,
        newImage: String?
    ): LiveData<Message> = service.updateUser(userId, newUserName, newBiography, newImage)

    override fun logIn(email: String): LiveData<User?> = service.logIn(email)

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
    ): LiveData<Message> = service.createGame(gameName, about, console, developer, publisher, genre, createdBy, releaseDate, image)

    override fun deleteGame(gameId: String): LiveData<Message> = service.deleteGame(gameId)

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
    ): LiveData<Message> = service.updateGame(gameId, newGameName, newAbout, newConsole, newDeveloper, newPublisher, newGenre, newReleaseDate, newImage)

    override fun addGameToUser(userId: String, gameId: String): LiveData<Message> =
        service.addGameToUser(userId, gameId)

    override fun removeGameFromUser(userId: String, gameId: String): LiveData<Message> =
        service.removeGameFromUser(userId, gameId)

    override fun createAchievement(
        achievementName: String,
        about: String,
        totalCollectable: Int,
        createdBy: String,
        image: String,
        gameId: String
    ): LiveData<Message> = service.createAchievement(achievementName, about, totalCollectable, createdBy, image, gameId)

    override fun deleteAchievement(achievementId: String): LiveData<Message> = service.deleteAchievement(achievementId)

    override fun updateAchievement(
        achievementId: String,
        newAchievementName: String,
        newAbout: String,
        newTotalCollectable: Int,
        newImage: String
    ): LiveData<Message> = service.updateAchievement(achievementId, newAchievementName, newAbout, newTotalCollectable, newImage)

    override fun lockOrUnlockAchievement(userId: String, userAchievementId: String): LiveData<Message> =
        service.lockOrUnlockAchievement(userId, userAchievementId)

    override fun lockOrUnlockChallengeAchievement(userId: String, userChallengeAchievementId: String): LiveData<Message> =
        service.lockOrUnlockChallengeAchievement(userId, userChallengeAchievementId)
    override fun createChallengeAchievement(challengeId: String, achievementId: String): LiveData<Message> =
        service.createChallengeAchievement(challengeId, achievementId)

    override fun deleteChallengeAchievement(challengeAchievementId: String): LiveData<Message> =
        service.deleteChallengeAchievement(challengeAchievementId)

    override fun toggleChallengeAchievementClearState(userId: String, userChallengeAchievementId: String): LiveData<Message> =
        service.toggleChallengeAchievementClearState(userId, userChallengeAchievementId)

    override fun inviteUserToChallenge(
        userId: String,
        challengeId: String,
        isRequest: Boolean
    ): LiveData<Message> = service.inviteUserToChallenge(userId, challengeId, isRequest)

    override fun acceptChallengeInvite(challengeInviteId: String, userId: String): LiveData<Message> =
        service.acceptChallengeInvite(challengeInviteId, userId)

    override fun rejectChallengeInvite(challengeInviteId: String, userId: String): LiveData<Message> =
        service.rejectChallengeInvite(challengeInviteId, userId)

    override fun deleteChallengeInvite(challengeInviteId: String): LiveData<Message> =
        service.deleteChallengeInvite(challengeInviteId)

    override fun createChallenge(userId: String, challengeName: String, type: String, image: String): LiveData<Message> =
        service.createChallenge(userId, challengeName, type, image)

    override fun updateChallenge(
        challengeId: String,
        newChallengeName: String,
        newType: String,
        newImage: String
    ): LiveData<Message> = service.updateChallenge(challengeId, newChallengeName, newType, newImage)

    override fun deleteChallenge(challengeId: String): LiveData<Message> = service.deleteChallenge(challengeId)

    override fun startChallenge(userId: String, challengeId: String): LiveData<Message> =
        service.startChallenge(userId, challengeId)

    override fun endChallenge(challengeId: String): LiveData<Message> = service.endChallenge(challengeId)

    override fun inviteFriend(senderId: String, receiverId: String): LiveData<Message> =
        service.inviteFriend(senderId, receiverId)

    override fun acceptFriendInvite(friendId: String): LiveData<Message> = service.acceptFriendInvite(friendId)

    override fun rejectFriendInvite(friendId: String): LiveData<Message> = service.rejectFriendInvite(friendId)

    override fun deleteFriendInvite(friendId: String): LiveData<Message> = service.deleteFriendInvite(friendId)

    override fun uploadImage(imageUri: Uri, context: Context): LiveData<String?> = service.uploadImage(imageUri, context)
}
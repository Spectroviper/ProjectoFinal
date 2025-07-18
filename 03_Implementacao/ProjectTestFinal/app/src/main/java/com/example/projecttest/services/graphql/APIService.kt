package com.example.projecttest.services.graphql

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo3.api.Optional
import com.example.projecttest.AppConfig
import com.example.projecttest.data.model.Achievement
import com.example.projecttest.data.model.Challenge
import com.example.projecttest.data.model.ChallengeAchievement
import com.example.projecttest.data.model.ChallengeInvite
import com.example.projecttest.data.model.DetailedUserChallenge
import com.example.projecttest.data.model.Friend
import com.example.projecttest.data.model.Game
import com.example.projecttest.data.model.User
import com.example.projecttest.data.model.UserGame
import com.example.projecttest.services.UserService
import com.example.projecttest.data.model.Message
import com.example.projecttest.data.model.UserAchievement
import com.example.projecttest.data.model.UserChallenge
import com.example.projecttest.data.model.UserChallengeAchievement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIService : UserService {

    val BASE_URL = AppConfig.DOMAIN + AppConfig.PORT

    private val apolloClient = ApolloClientInstance.apolloClient

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val imageApiService: ImageUploadAPI = retrofit.create(ImageUploadAPI::class.java)

    // --- Queries ---

    override fun getAllUsers(userId: String): LiveData<List<User>> {
        val liveData = MutableLiveData<List<User>>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response =
                    apolloClient.query(GetAllUsersQuery(Optional.Present(userId))).execute()
                val users = response.data?.getAllUsers?.mapNotNull {
                    User(
                        userId = it?.UserId,
                        userName = it?.UserName,
                        totalPoints = it?.TotalPoints,
                        imageUrl = it?.Image,
                    )
                }
                liveData.postValue(users ?: emptyList())
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(emptyList())
            }
        }
        return liveData
    }

    override fun getUserById(userId: String): LiveData<User> {
        val liveData = MutableLiveData<User>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response =
                    apolloClient.query(GetUserByUserIdQuery(Optional.Present(userId))).execute()
                val result = response.data?.getUserByUserId
                if (result != null) {
                    val user = User(
                        userId = result.UserId,
                        userName = result.UserName,
                        email = result.Email,
                        biography = result.Biography,
                        imageUrl = result.Image,
                        memberSince = result.MemberSince.toString(),
                        lastLogin = result.LastLogin.toString(),
                        totalPoints = result.TotalPoints,
                        averageCompletion = result.AverageCompletion,
                        userChallenges = result.UserChallenges?.mapNotNull { uc ->
                            val challenge = uc?.Challenge
                            if (uc != null && challenge != null) {
                                UserChallenge(
                                    userChallengeId = uc.UserChallengeId,
                                    challenge = Challenge(
                                        challengeId = challenge.ChallengeId,
                                        challengeName = challenge.ChallengeName,
                                        imageUrl = challenge.Image
                                    )
                                )
                            } else null
                        } ?: emptyList()
                    )
                    liveData.postValue(user)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return liveData
    }

    override fun getUserUserGames(userId: String): LiveData<List<UserGame>> {
        val liveData = MutableLiveData<List<UserGame>>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("getUserUserGames", "Starting query with userId: $userId")

                val response = apolloClient.query(
                    GetUserUserGamesQuery(userId = Optional.Present(userId))
                ).execute()

                // Check for GraphQL or server-side errors
                if (response.hasErrors()) {
                    Log.e("getUserUserGames", "GraphQL errors: ${response.errors}")
                }

                // Log raw data
                val rawData = response.data?.getUserUserGames
                Log.d("getUserUserGames", "Raw data received: $rawData")

                // Transform the data
                val userGames = rawData?.mapNotNull { userGame ->
                    if (userGame == null) {
                        Log.w("getUserUserGames", "Null userGame object found and skipped")
                        return@mapNotNull null
                    }

                    val game = userGame.Game
                    if (game == null) {
                        Log.w(
                            "getUserUserGames",
                            "userGame with null game found: ${userGame.UserGameId}"
                        )
                        return@mapNotNull null
                    }

                    val avgCompletionFloat = (userGame.AverageCompletion as? Double)?.toFloat()
                    Log.d(
                        "getUserUserGames",
                        "Mapped userGameId=${userGame.UserGameId}, gameName=${game.GameName}, avgCompletion=$avgCompletionFloat"
                    )

                    UserGame(
                        userGameId = userGame.UserGameId,
                        averageCompletion = avgCompletionFloat,
                        game = Game(
                            gameId = game.GameId,
                            gameName = game.GameName,
                            console = game.Console,
                            imageUrl = game.Image
                        )
                    )
                }

                Log.d("getUserUserGames", "Mapped userGames list: $userGames")
                liveData.postValue(userGames ?: emptyList())

            } catch (e: Exception) {
                Log.e("getUserUserGames", "Exception occurred", e)
                liveData.postValue(emptyList())
            }
        }

        return liveData
    }

    override fun getAllUsersByTotalPoints(): LiveData<List<User>> {
        val liveData = MutableLiveData<List<User>>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.query(GetAllUsersByTotalPointsQuery()).execute()
                val users = response.data?.getAllUsersByTotalPoints?.mapNotNull {
                    User(
                        userId = it?.UserId,
                        userName = it?.UserName,
                        totalPoints = it?.TotalPoints,
                        imageUrl = it?.Image,
                    )
                }
                liveData.postValue(users ?: emptyList())
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(emptyList())
            }
        }
        return liveData
    }


    override fun getAllUserFriends(userId: String): LiveData<List<User>> {
        val liveData = MutableLiveData<List<User>>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response =
                    apolloClient.query(GetAllUserFriendsQuery(Optional.Present(userId))).execute()
                val friends = response.data?.getAllUserFriends?.mapNotNull {
                    User(
                        userId = it?.UserId,
                        userName = it?.UserName,
                        totalPoints = it?.TotalPoints,
                        imageUrl = it?.Image,
                    )
                }
                liveData.postValue(friends ?: emptyList())
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(emptyList())
            }
        }
        return liveData
    }

    override fun getAllUsersNotInChallenge(challengeId: String): LiveData<List<User>?> {
        val liveData = MutableLiveData<List<User>?>()

        Log.d("ApolloCall", "Fetching users not in challenge with ID: $challengeId")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.query(
                    GetAllUsersNotInChallengeQuery(Optional.Present(challengeId))
                ).execute()

                if (response.hasErrors()) {
                    Log.e("ApolloCall", "GraphQL errors: ${response.errors}")
                }

                val users = response.data?.getAllUsersNotInChallenge?.mapNotNull { user ->
                    user?.let {
                        Log.d("ApolloCall", "Found user: ${it.UserName} (ID: ${it.UserId})")
                        User(
                            userId = it.UserId,
                            userName = it.UserName,
                            imageUrl = it.Image
                        )
                    }
                } ?: emptyList()

                Log.d("ApolloCall", "Total users returned: ${users.size}")
                liveData.postValue(users)
            } catch (e: Exception) {
                Log.e("ApolloCall", "Exception occurred: ${e.message}", e)
                liveData.postValue(emptyList())
            }
        }

        return liveData
    }

    override fun searchUsers(userName: String?): LiveData<List<User>> {
        val liveData = MutableLiveData<List<User>>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response =
                    apolloClient.query(SearchUsersQuery(Optional.Present(userName))).execute()
                val users = response.data?.searchUsers?.mapNotNull {
                    User(
                        userId = it?.UserId,
                        userName = it?.UserName,
                        totalPoints = it?.TotalPoints,
                        imageUrl = it?.Image,
                    )
                }
                liveData.postValue(users ?: emptyList())
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(emptyList())
            }
        }
        return liveData
    }

    override fun searchUserGames(userId: String, gameName: String?): LiveData<List<UserGame>> {
        val liveData = MutableLiveData<List<UserGame>>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("UserServiceImpl", "Searching user games: userId=$userId, gameName=$gameName")

                val response = apolloClient.query(
                    SearchUserGamesQuery(
                        userId = Optional.Present(userId),
                        gameName = gameName?.let { Optional.Present(it) } ?: Optional.Absent
                    )
                ).execute()

                Log.d("UserServiceImpl", "Apollo raw response: ${response.data}")
                Log.d("UserServiceImpl", "Errors: ${response.errors}")

                val userGames = response.data?.searchUserGames?.mapNotNull { ug ->
                    ug?.let {
                        UserGame(
                            userGameId = it.UserGameId,
                            averageCompletion = it.AverageCompletion?.toFloat(),
                            game = it.Game?.let { g ->
                                Game(
                                    gameId = g.GameId,
                                    gameName = g.GameName,
                                    console = g.Console,
                                    imageUrl = g.Image
                                )
                            }
                        )
                    }
                }

                Log.d("UserServiceImpl", "Parsed ${userGames?.size ?: 0} user games")

                liveData.postValue(userGames ?: emptyList())
            } catch (e: Exception) {
                Log.e("UserServiceImpl", "Error searching user games", e)
                liveData.postValue(emptyList())
            }
        }
        return liveData
    }

    override fun getAllGames(): LiveData<List<Game>> {
        val liveData = MutableLiveData<List<Game>>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.query(GetAllGamesQuery()).execute()
                val games = response.data?.getAllGames?.mapNotNull {
                    Game(
                        gameId = it?.GameId,
                        gameName = it?.GameName,
                        imageUrl = it?.Image,
                        console = it?.Console,
                    )
                }
                liveData.postValue(games ?: emptyList())
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(emptyList())
            }
        }
        return liveData
    }

    override fun getGameByGameId(gameId: String): LiveData<Game?> {
        val liveData = MutableLiveData<Game?>()
        Log.d("UserServiceImpl", "Fetching all users for userId: $gameId")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response =
                    apolloClient.query(GetGameByGameIdQuery(Optional.Present(gameId))).execute()
                val result = response.data?.getGameByGameId
                if (result != null) {
                    Log.d("UserServiceImpl", "Finding Game: $result")
                    val game = Game(
                        gameId = result.GameId,
                        gameName = result.GameName,
                        genre = result.Genre,
                        imageUrl = result.Image,
                        publisher = result.Publisher,
                        developer = result.Developer,
                        console = result.Console,
                        about = result.About,
                        createdBy = result.CreatedBy,
                        achievements = result.Achievements?.mapNotNull { gqlAchievement ->
                            gqlAchievement?.let {
                                Achievement(
                                    achievementId = it.AchievementId,
                                    achievementName = it.AchievementName,
                                    about = it.About,
                                )
                            }
                        } ?: emptyList()
                    )
                    liveData.postValue(game)
                }
                Log.d("UserServiceImpl", "Game Found: $result")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("UserServiceImpl", "Error fetching games", e)
                liveData.postValue(null)
            }
        }
        return liveData
    }

    override fun searchGames(
        gameName: String?,
        console: String?,
        developer: String?,
        publisher: String?,
        genre: String?
    ): LiveData<List<Game>> {
        val liveData = MutableLiveData<List<Game>>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.query(
                    SearchGamesQuery(
                        Optional.Present(gameName),
                        Optional.Present(console),
                        Optional.Present(developer),
                        Optional.Present(publisher),
                        Optional.Present(genre)
                    )
                ).execute()

                val games = response.data?.searchGames?.mapNotNull {
                    Game(
                        gameId = it?.GameId,
                        gameName = it?.GameName,
                        imageUrl = it?.Image,
                        console = it?.Console,
                    )
                }
                liveData.postValue(games ?: emptyList())
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(emptyList())
            }
        }
        return liveData
    }

    override fun getAchievementByAchievementId(achievementId: String): LiveData<Achievement?> {
        val liveData = MutableLiveData<Achievement?>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.query(
                    GetAchievementByAchievementIdQuery(
                        achievementId = Optional.Present(achievementId)
                    )
                ).execute()

                val achievement = response.data?.getAchievementByAchievementId?.let {
                    Achievement(
                        achievementId = it.AchievementId,
                        achievementName = it.AchievementName,
                        about = it.About,
                        createdBy = it.CreatedBy,
                        imageUrl = it.Image,
                        totalCollectable = it.TotalCollectable
                    )
                }

                liveData.postValue(achievement)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(null)
            }
        }
        return liveData
    }

    override fun getAllAchievementsNotInAChallenge(challengeId: String): LiveData<List<Achievement>?> {
        val liveData = MutableLiveData<List<Achievement>?>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.query(
                    GetAllAchievementsNotInAChallengeQuery(
                        challengeId = Optional.Present(challengeId)
                    )
                ).execute()

                val achievements = response.data?.getAllAchievementsNotInAChallenge?.mapNotNull {
                    it?.let { achievement ->
                        Achievement(
                            achievementId = achievement.AchievementId,
                            achievementName = achievement.AchievementName,
                            about = achievement.About,
                            imageUrl = achievement.Image
                        )
                    }
                }

                liveData.postValue(achievements ?: emptyList())
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(emptyList())
            }
        }
        return liveData
    }

    override fun searchAchievements(
        achievementName: String?,
        gameId: String?,
        challengeId: String
    ): LiveData<List<Achievement>> {
        val liveData = MutableLiveData<List<Achievement>>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.query(
                    SearchAchievementsQuery(
                        achievementName = if (achievementName != null) Optional.Present(
                            achievementName
                        ) else Optional.Absent,
                        gameId = if (gameId != null) Optional.Present(gameId) else Optional.Absent,
                        challengeId = Optional.Present(challengeId)
                    )
                ).execute()

                val achievements = response.data?.searchAchievements?.mapNotNull {
                    it?.let {
                        Achievement(
                            achievementId = it.AchievementId,
                            achievementName = it.AchievementName,
                            about = it.About,
                            imageUrl = it.Image,
                        )
                    }
                }

                liveData.postValue(achievements ?: emptyList())
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(emptyList())
            }
        }
        return liveData
    }

    override fun getUserGameByUserGameId(userGameId: String): LiveData<UserGame?> {
        val liveData = MutableLiveData<UserGame?>()

        Log.d("UserGameRepo", "Called getUserGameByUserGameId with ID: $userGameId")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("UserGameRepo", "Starting Apollo query for userGameId: $userGameId")

                val response = apolloClient.query(
                    GetUserGameByUserGameIdQuery(Optional.Present(userGameId))
                ).execute()

                Log.d("UserGameRepo", "Apollo query completed. Response data: ${response.data}")

                val userGame = response.data?.getUserGameByUserGameId?.let {
                    Log.d("UserGameRepo", "Parsing UserGame: ID=${it.UserGameId}, AvgCompletion=${it.AverageCompletion}")

                    UserGame(
                        userGameId = it.UserGameId,
                        averageCompletion = it.AverageCompletion?.toFloat(),
                        game = it.Game?.let { g ->
                            Log.d("UserGameRepo", "Parsing Game: ID=${g.GameId}, Name=${g.GameName}, Genre=${g.Genre}")

                            Game(
                                gameId = g.GameId,
                                gameName = g.GameName,
                                genre = g.Genre,
                                imageUrl = g.Image,
                                publisher = g.Publisher,
                                developer = g.Developer,
                                createdBy = g.CreatedBy,
                                console = g.Console,
                                about = g.About,
                                achievements = g.Achievements?.mapNotNull { a ->
                                    Log.d("UserGameRepo", "Parsing Achievement: ID=${a?.AchievementId}, Name=${a?.AchievementName}")
                                    Achievement(
                                        achievementId = a?.AchievementId,
                                        about = a?.About,
                                        achievementName = a?.AchievementName,
                                        imageUrl = a?.Image,
                                        totalCollectable = a?.TotalCollectable,
                                        userAchievements = a?.UserAchievements?.map { ua ->
                                            Log.d("UserGameRepo", "Parsing UserAchievement: ID=${ua?.UserAchievementId}, Collected=${ua?.TotalCollected}")
                                            UserAchievement(
                                                userAchievementId = ua?.UserAchievementId,
                                                totalCollected = ua?.TotalCollected
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    )
                }

                Log.d("UserGameRepo", "Posting UserGame to LiveData: $userGame")
                liveData.postValue(userGame)

            } catch (e: Exception) {
                Log.e("UserGameRepo", "Exception during Apollo query or parsing", e)
                liveData.postValue(null)
            }
        }

        return liveData
    }

    override fun getUserAchievementByUserAchievementId(userAchievementId: String): LiveData<UserAchievement?> {
        val liveData = MutableLiveData<UserAchievement?>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.query(
                    GetUserAchievementByUserAchievementIdQuery(Optional.Present(userAchievementId))
                ).execute()

                val userAchievement =
                    response.data?.getUserAchievementByUserAchievementId?.let { ua ->
                        UserAchievement(
                            userAchievementId = ua.UserAchievementId,
                            totalCollected = ua.TotalCollected,
                            unlockDate = ua.UnlockDate.toString(),
                            clear = ua.Clear,
                            achievement = ua.Achievement?.let { a ->
                                Achievement(
                                    imageUrl = a.Image,
                                    about = a.About,
                                    achievementId = a.AchievementId,
                                    achievementName = a.AchievementName,
                                    totalCollectable = a.TotalCollectable
                                )

                            },
                            user = ua.User?.let { u ->
                                User(
                                    userId = u.UserId
                                )
                            }
                        )
                    }

                liveData.postValue(userAchievement)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(null)
            }
        }
        return liveData
    }

    override fun getAllUserChallengeInvites(userId: String): LiveData<List<ChallengeInvite>?> {
        val liveData = MutableLiveData<List<ChallengeInvite>>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("ChallengeInviteService", "Fetching challenge invites for userId: $userId")

                val response = apolloClient.query(
                    GetAllUserChallengeInvitesQuery(
                        userId = Optional.Present(userId)
                    )
                ).execute()

                Log.d("ChallengeInviteService", "Apollo response data: ${response.data}")
                Log.d("ChallengeInviteService", "Apollo response errors: ${response.errors}")

                val invites = response.data?.getAllUserChallengeInvites?.mapNotNull { invite ->
                    invite?.let {
                        Log.d(
                            "ChallengeInviteService",
                            "Processing invite with ID: ${it.ChallengeInviteId}"
                        )
                        ChallengeInvite(
                            challengeInviteId = it.ChallengeInviteId,
                            state = it.State,
                            isRequest = it.IsRequest,
                            date = it.Date.toString(),
                            challenge = it.Challenge?.let { c ->
                                Challenge(
                                    challengeId = c.ChallengeId,
                                    challengeName = c.ChallengeName
                                )
                            },
                            user = it.User?.let { u ->
                                User(
                                    userId = u.UserId,
                                    userName = u.UserName
                                )
                            }
                        )
                    }
                } ?: emptyList()

                Log.d("ChallengeInviteService", "Mapped ${invites.size} invites")
                liveData.postValue(invites)
            } catch (e: Exception) {
                Log.e("ChallengeInviteService", "Exception while fetching invites", e)
                liveData.postValue(emptyList())
            }
        }
        return liveData
    }

    override fun getUserUserAchievements(userId: String): LiveData<List<UserAchievement>?> {
        val liveData = MutableLiveData<List<UserAchievement>>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("UserAchievementService", "Fetching achievements for userId: $userId")

                val response = apolloClient.query(
                    GetUserUserAchievementsQuery(userId = Optional.Present(userId))
                ).execute()

                Log.d("UserAchievementService", "Apollo response data: ${response.data}")
                Log.d("UserAchievementService", "Apollo response errors: ${response.errors}")

                val achievements = response.data?.getUserUserAchievements?.mapNotNull { ua ->
                    ua?.let {
                        Log.d(
                            "UserAchievementService",
                            "Processing UserAchievementId: ${it.UserAchievementId}"
                        )
                        UserAchievement(
                            userAchievementId = it.UserAchievementId,
                            clear = it.Clear,
                            totalCollected = it.TotalCollected,
                            unlockDate = it.UnlockDate.toString(),
                            achievement = it.Achievement?.let { a ->
                                Achievement(
                                    achievementId = a.AchievementId,
                                    achievementName = a.AchievementName,
                                    about = a.About,
                                    imageUrl = a.Image
                                )
                            },
                        )
                    }
                } ?: emptyList()

                Log.d("UserAchievementService", "Mapped ${achievements.size} achievements")
                liveData.postValue(achievements)

            } catch (e: Exception) {
                Log.e("UserAchievementService", "Exception while fetching user achievements", e)
                liveData.postValue(emptyList())
            }
        }

        return liveData
    }


    override fun getAllChallengeInvitesByChallengeId(challengeId: String): LiveData<List<ChallengeInvite>?> {
        val liveData = MutableLiveData<List<ChallengeInvite>>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("ChallengeInviteService", "Fetching invites for challengeId: $challengeId")

                val response = apolloClient.query(
                    GetAllChallengeInvitesByChallengeIdQuery(
                        challengeId = Optional.Present(challengeId)
                    )
                ).execute()

                Log.d("ChallengeInviteService", "Apollo response data: ${response.data}")
                Log.d("ChallengeInviteService", "Apollo response errors: ${response.errors}")

                val invites =
                    response.data?.getAllChallengeInvitesByChallengeId?.mapNotNull { invite ->
                        invite?.let {
                            Log.d(
                                "ChallengeInviteService",
                                "Processing invite ID: ${it.ChallengeInviteId}"
                            )
                            ChallengeInvite(
                                challengeInviteId = it.ChallengeInviteId,
                                state = it.State,
                                isRequest = it.IsRequest,
                                date = it.Date.toString(),
                                challenge = it.Challenge?.let { c ->
                                    Challenge(
                                        challengeId = c.ChallengeId,
                                        challengeName = c.ChallengeName,
                                        createdBy = c.CreatedBy
                                    )
                                },
                                user = it.User?.let { u ->
                                    User(
                                        userId = u.UserId,
                                        userName = u.UserName
                                    )
                                }
                            )
                        }
                    } ?: emptyList()

                Log.d("ChallengeInviteService", "Mapped ${invites.size} invites for challenge")
                liveData.postValue(invites)
            } catch (e: Exception) {
                Log.e("ChallengeInviteService", "Exception while fetching challenge invites", e)
                liveData.postValue(emptyList())
            }
        }
        return liveData
    }

    override fun getAllJoinableChallenges(userId: String): LiveData<List<Challenge>?> {
        val liveData = MutableLiveData<List<Challenge>?>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.query(
                    GetAllJoinableChallengesQuery(
                        userId = Optional.Present(userId)
                    )
                ).execute()

                val challenges = response.data?.getAllJoinableChallenges?.mapNotNull { c ->
                    c?.let {
                        Challenge(
                            challengeId = it.ChallengeId,
                            challengeName = it.ChallengeName,
                            imageUrl = it.Image
                        )
                    }
                }

                liveData.postValue(challenges ?: emptyList())
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(emptyList())
            }
        }
        return liveData
    }

    /*override fun getAllJoinedChallenges(userId: String): LiveData<List<Challenge>?> {
        val liveData = MutableLiveData<List<Challenge>?>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.query(
                    GetAllJoinedChallengesQuery(
                        userId = Optional.Present(userId)
                    )
                ).execute()

                val challenges = response.data?.getAllJoinedChallenges?.mapNotNull { c ->
                    c?.let {
                        Challenge(
                            challengeId = it.ChallengeId,
                            challengeName = it.ChallengeName,
                            imageUrl = it.Image
                        )
                    }
                }

                liveData.postValue(challenges ?: emptyList())
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(emptyList())
            }
        }
        return liveData
    }*/

    override fun getChallengeByChallengeId(challengeId: String): LiveData<Challenge?> {
        val liveData = MutableLiveData<Challenge?>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.query(
                    GetChallengeByChallengeIdQuery(
                        challengeId = Optional.Present(challengeId)
                    )
                ).execute()

                val challenge = response.data?.getChallengeByChallengeId?.let { c ->
                    Challenge(
                        challengeId = c.ChallengeId,
                        challengeName = c.ChallengeName,
                        createdBy = c.CreatedBy,
                        endDate = c.EndDate.toString(),
                        imageUrl = c.Image,
                        startDate = c.StartDate.toString(),
                        type = c.Type,
                        userChallenges = c.UserChallenges?.mapNotNull { uc ->
                            uc?.let {
                                val user = it.User
                                if (user != null) {
                                    UserChallenge(
                                        userChallengeId = it.UserChallengeId,
                                        user = User(
                                            userId = user.UserId,
                                            userName = user.UserName,
                                            imageUrl = user.Image
                                        )
                                    )
                                } else null
                            }
                        } ?: emptyList(),
                        challengeAchievements = c.ChallengeAchievements?.mapNotNull { ca ->
                            ca?.let {
                                ChallengeAchievement(
                                    challengeAchievementId = it.ChallengeAchievementId,
                                    achievement = it.Achievement?.let { a ->
                                        Achievement(
                                            about = a.About,
                                            achievementId = a.AchievementId,
                                            achievementName = a.AchievementName,
                                            imageUrl = a.Image,
                                            totalCollectable = a.TotalCollectable
                                        )
                                    }
                                )
                            }
                        }
                    )
                }

                liveData.postValue(challenge)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(null)
            }
        }
        return liveData
    }

    override fun searchChallenges(
        challengeName: String?,
        type: String?
    ): LiveData<List<Challenge>> {
        val liveData = MutableLiveData<List<Challenge>>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.query(
                    SearchChallengesQuery(
                        challengeName = if (challengeName != null) Optional.Present(challengeName) else Optional.Absent,
                        type = if (type != null) Optional.Present(type) else Optional.Absent
                    )
                ).execute()

                val challenges = response.data?.searchChallenges?.mapNotNull { c ->
                    c?.let {
                        Challenge(
                            challengeId = it.ChallengeId,
                            challengeName = it.ChallengeName,
                            imageUrl = it.Image
                        )
                    }
                }

                liveData.postValue(challenges ?: emptyList())
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(emptyList())
            }
        }
        return liveData
    }

    override fun getUserChallengeByUserChallengeId(userChallengeId: String): LiveData<DetailedUserChallenge?> {
        val TAG = "getUserChallenge"
        val liveData = MutableLiveData<DetailedUserChallenge?>()

        // Check if client is null
        Log.d(TAG, "apolloClient instance: $apolloClient")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d(TAG, "Starting query for userChallengeId: $userChallengeId")

                val response = apolloClient.query(
                    GetUserChallengeByUserChallengeIdQuery(
                        userChallengeId = Optional.Present(userChallengeId))
                ).execute()

                Log.d(TAG, "Received response from Apollo: $response")

                val uc = response.data?.getUserChallengeByUserChallengeId

                if (uc == null) {
                    Log.w(TAG, "No data returned for userChallengeId: $userChallengeId")
                }

                val detailedUserChallenge = uc?.let { data ->
                    Log.d(TAG, "Mapping response to DetailedUserChallenge")
                    DetailedUserChallenge(
                        userChallenge = data.UserChallenge?.let { ucData ->
                            Log.d(TAG, "Mapping UserChallenge")
                            UserChallenge(
                                averageCompletion = ucData.AverageCompletion?.toFloat(),
                                userChallengeId = ucData.UserChallengeId,
                                isLeader = ucData.IsLeader ?: false,
                                challenge = ucData.Challenge?.let { c ->
                                    Log.d(TAG, "Mapping Challenge")
                                    Challenge(
                                        challengeId = c.ChallengeId,
                                        challengeName = c.ChallengeName,
                                        createdBy = c.CreatedBy,
                                        endDate = c.EndDate?.toString() ?: "",
                                        imageUrl = c.Image,
                                        startDate = c.StartDate?.toString() ?: "",
                                        type = c.Type ?: ""
                                    )
                                }
                            )
                        },
                        userChallengeAchievements = data.UserChallengeAchievements?.mapNotNull { uca ->
                            uca?.let {
                                Log.d(TAG, "Mapping UserChallengeAchievement")
                                UserChallengeAchievement(
                                    userChallengeAchievementId = it.UserChallengeAchievementId,
                                    totalCollected = it.TotalCollected,
                                    achievement = it.Achievement?.let { a ->
                                        Log.d(TAG, "Mapping Achievement")
                                        Achievement(
                                            achievementId = a.AchievementId,
                                            achievementName = a.AchievementName,
                                            imageUrl = a.Image,
                                            about = a.About,
                                            totalCollectable = a.TotalCollectable
                                        )
                                    }
                                )
                            }
                        } ?: emptyList()
                    )
                }

                Log.d(TAG, "Posting value to LiveData")
                liveData.postValue(detailedUserChallenge)

            } catch (e: Exception) {
                Log.e(TAG, "Error fetching user challenge: ${e.message}", e)
                liveData.postValue(null)
            }
        }

        return liveData
    }

    override fun getUserChallengeAchievementByUserChallengeAchievementId(userChallengeAchievementId: String): LiveData<UserChallengeAchievement?> {
        val liveData = MutableLiveData<UserChallengeAchievement?>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.query(
                    GetUserChallengeAchievementByUserChallengeAchievementIdQuery(
                        Optional.Present(
                            userChallengeAchievementId
                        )
                    )
                ).execute()

                val userChallengeAchievement =
                    response.data?.getUserChallengeAchievementByUserChallengeAchievementId?.let { uca ->
                        UserChallengeAchievement(
                            userChallengeAchievementId = uca.UserChallengeAchievementId,
                            clear = uca.Clear,
                            totalCollected = uca.TotalCollected,
                            unlockDate = uca.UnlockDate.toString(),
                            achievement = uca.Achievement?.let { a ->
                                Achievement(
                                    about = a.About,
                                    achievementId = a.AchievementId,
                                    achievementName = a.AchievementName,
                                    imageUrl = a.Image,
                                    totalCollectable = a.TotalCollectable
                                )
                            },
                            user = uca.User?.let { u ->
                                User(
                                    userId = u.UserId
                                )
                            }
                        )
                    }

                liveData.postValue(userChallengeAchievement)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(null)
            }
        }
        return liveData
    }


    override fun getChallengeAchievementByChallengeAchievementId(challengeAchievementId: String): LiveData<ChallengeAchievement?> {
        val liveData = MutableLiveData<ChallengeAchievement?>()

        Log.d("ChallengeRepo", "Called getChallengeAchievementByChallengeAchievementId with ID: $challengeAchievementId")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("ChallengeRepo", "Starting Apollo query for challengeAchievementId: $challengeAchievementId")

                val response = apolloClient.query(
                    GetChallengeAchievementByChallengeAchievementIdQuery(
                        Optional.Present(challengeAchievementId)
                    )
                ).execute()

                Log.d("ChallengeRepo", "Apollo query completed. Response data: ${response.data}")

                val challengeAchievement =
                    response.data?.getChallengeAchievementByChallengeAchievementId?.let { ca ->
                        Log.d("ChallengeRepo", "Parsing ChallengeAchievement: ID=${ca.ChallengeAchievementId}")

                        ChallengeAchievement(
                            challengeAchievementId = ca.ChallengeAchievementId,
                            achievement = ca.Achievement?.let { a ->
                                Log.d(
                                    "ChallengeRepo",
                                    "Parsing Achievement: ID=${a.AchievementId}, Name=${a.AchievementName}, TotalCollectable=${a.TotalCollectable}"
                                )

                                Achievement(
                                    achievementId = a.AchievementId,
                                    achievementName = a.AchievementName,
                                    about = a.About,
                                    imageUrl = a.Image,
                                    totalCollectable = a.TotalCollectable
                                )
                            }
                        )
                    }

                Log.d("ChallengeRepo", "Posting ChallengeAchievement to LiveData: $challengeAchievement")
                liveData.postValue(challengeAchievement)

            } catch (e: Exception) {
                Log.e("ChallengeRepo", "Exception during Apollo query or parsing", e)
                liveData.postValue(null)
            }
        }

        return liveData
    }


    override fun getAllFriendInvites(userId: String): LiveData<List<Friend>> {
        val liveData = MutableLiveData<List<Friend>>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.query(
                    GetAllFriendInvitesQuery(Optional.Present(userId))
                ).execute()

                val friends = response.data?.getAllFriendInvites?.mapNotNull { fi ->
                    fi?.let {
                        Friend(
                            friendId = it.FriendId,
                            date = it.Date.toString(),
                            state = it.State,
                            userReceiver = it.UserReceiver?.let { receiver ->
                                User(
                                    userId = receiver.UserId,
                                    userName = receiver.UserName
                                )
                            },
                            userSender = it.UserSender?.let { sender ->
                                User(
                                    userId = sender.UserId,
                                    userName = sender.UserName
                                )
                            }
                        )
                    }
                }

                liveData.postValue(friends ?: emptyList())
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(emptyList())
            }
        }
        return liveData
    }

    override fun searchFriends(userId: String, userName: String?): LiveData<List<Friend>?> {
        val liveData = MutableLiveData<List<Friend>?>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.query(
                    SearchFriendsQuery(
                        userId = Optional.Present(userId),
                        userName = if (userName != null) Optional.Present(userName) else Optional.Absent
                    )
                ).execute()

                val friends = response.data?.searchFriends?.mapNotNull { f ->
                    f?.let {
                        Friend(
                            friendId = it.FriendId,
                            userReceiver = it.UserReceiver?.let { receiver ->
                                User(
                                    userId = receiver.UserId,
                                    userName = receiver.UserName,
                                    imageUrl = receiver.Image
                                )
                            },
                            userSender = it.UserSender?.let { sender ->
                                User(
                                    userId = sender.UserId,
                                    userName = sender.UserName,
                                    imageUrl = sender.Image
                                )
                            }
                        )
                    }
                }

                liveData.postValue(friends ?: emptyList())
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(emptyList())
            }
        }
        return liveData
    }

    // Mutations
    override fun createUser(
        userName: String?,
        email: String?,
        biography: String?,
        image: String?
    ): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    CreateUserMutation(
                        userName = Optional.Present(userName),
                        email = Optional.Present(email),
                        biography = Optional.Present(biography),
                        image = Optional.Present(image)
                    )
                ).execute()

                val result = response.data?.createUser
                val message = Message(
                    successfull = result?.successfull ?: false,
                    message = result?.message ?: "Unknown error"
                )
                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(successfull = false, message = "Error creating user"))
            }
        }
        return liveData
    }

    override fun deleteUser(userId: String?): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    DeleteUserMutation(
                        userId = if (userId != null) Optional.Present(userId) else Optional.Absent
                    )
                ).execute()

                val message = response.data?.deleteUser?.let {
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }

    override fun updateUser(
        userId: String?,
        newUserName: String?,
        newBiography: String?,
        newImage: String?
    ): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    UpdateUserMutation(
                        userId = if (userId != null) Optional.Present(userId) else Optional.Absent,
                        newUserName = if (newUserName != null) Optional.Present(newUserName) else Optional.Absent,
                        newBiography = if (newBiography != null) Optional.Present(newBiography) else Optional.Absent,
                        newImage = if (newImage != null) Optional.Present(newImage) else Optional.Absent
                    )
                ).execute()

                val message = response.data?.updateUser?.let {
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }

    override fun logIn(email: String): LiveData<User?> {
        val liveData = MutableLiveData<User?>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response =
                    apolloClient.mutation(LogInMutation(Optional.Present(email))).execute()
                val result = response.data?.logIn

                val user = result?.let {
                    User(
                        userId = it.UserId,
                        userName = it.UserName,
                        email = it.Email,
                        biography = it.Biography,
                        imageUrl = it.Image,
                        memberSince = it.MemberSince.toString(),
                        lastLogin = it.LastLogin.toString(),
                        totalPoints = it.TotalPoints,
                        averageCompletion = it.AverageCompletion,
                        userGames = it.UserGames?.mapNotNull { ug ->
                            ug?.let { userGame ->
                                val avgCompletion = when (val ac = userGame.AverageCompletion) {
                                    is Double -> ac.toFloat()
                                    else -> null
                                }

                                UserGame(
                                    userGameId = userGame.UserGameId,
                                    averageCompletion = avgCompletion,
                                    game = userGame.Game?.let { g ->
                                        Game(
                                            gameId = g.GameId,
                                            gameName = g.GameName,
                                            console = g.Console,
                                            imageUrl = g.Image
                                        )
                                    }
                                )
                            }
                        }
                    )
                }

                liveData.postValue(user)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(null)
            }
        }
        return liveData
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
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    CreateGameMutation(
                        gameName = Optional.Present(gameName),
                        about = Optional.Present(about),
                        console = Optional.Present(console),
                        developer = Optional.Present(developer),
                        publisher = Optional.Present(publisher),
                        genre = Optional.Present(genre),
                        createdBy = Optional.Present(createdBy),
                        releaseDate = Optional.Present(releaseDate),
                        image = Optional.Present(image)
                    )
                ).execute()

                val message = response.data?.createGame?.let {
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }

    override fun deleteGame(gameId: String): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    DeleteGameMutation(
                        gameId = Optional.Present(gameId)
                    )
                ).execute()

                val message = response.data?.deleteGame?.let {
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
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
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    UpdateGameMutation(
                        gameId = Optional.Present(gameId),
                        newGameName = Optional.Present(newGameName),
                        newAbout = Optional.Present(newAbout),
                        newConsole = Optional.Present(newConsole),
                        newDeveloper = Optional.Present(newDeveloper),
                        newPublisher = Optional.Present(newPublisher),
                        newGenre = Optional.Present(newGenre),
                        newReleaseDate = Optional.Present(newReleaseDate),
                        newImage = Optional.Present(newImage)
                    )
                ).execute()

                val message = response.data?.updateGame?.let {
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }

    override fun addGameToUser(userId: String, gameId: String): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    AddGameMutation(
                        userId = Optional.Present(userId),
                        gameId = Optional.Present(gameId)
                    )
                ).execute()

                val message = response.data?.addGame?.let {
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }

    override fun removeGameFromUser(userId: String, gameId: String): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    RemoveGameMutation(
                        userId = Optional.Present(userId),
                        gameId = Optional.Present(gameId)
                    )
                ).execute()

                val message = response.data?.removeGame?.let {
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }

    override fun createAchievement(
        achievementName: String,
        about: String,
        totalCollectable: Int,
        createdBy: String,
        image: String,
        gameId: String
    ): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    CreateAchievementMutation(
                        achievementName = Optional.Present(achievementName),
                        about = Optional.Present(about),
                        totalCollectable = Optional.Present(totalCollectable),
                        createdBy = Optional.Present(createdBy),
                        image = Optional.Present(image),
                        gameId = Optional.Present(gameId)
                    )
                ).execute()

                val message = response.data?.createAchievement?.let {
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }

    override fun deleteAchievement(achievementId: String): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    DeleteAchievementMutation(
                        achievementId = Optional.Present(achievementId)
                    )
                ).execute()

                val message = response.data?.deleteAchievement?.let {
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }

    override fun updateAchievement(
        achievementId: String,
        newAchievementName: String,
        newAbout: String,
        newTotalCollectable: Int,
        newImage: String
    ): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    UpdateAchievementMutation(
                        achievementId = Optional.Present(achievementId),
                        newAchievementName = Optional.Present(newAchievementName),
                        newAbout = Optional.Present(newAbout),
                        newTotalCollectable = Optional.Present(newTotalCollectable),
                        newImage = Optional.Present(newImage)
                    )
                ).execute()

                val message = response.data?.updateAchievement?.let {
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }


    override fun lockOrUnlockAchievement(
        userId: String,
        userAchievementId: String
    ): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d(
                    "AchievementService",
                    "Attempting to lock/unlock achievement for userId: $userId, achievementId: $userAchievementId"
                )

                val response = apolloClient.mutation(
                    LockUnlockAchievementMutation(
                        userId = Optional.Present(userId),
                        userAchievementId = Optional.Present(userAchievementId)
                    )
                ).execute()

                Log.d("AchievementService", "Apollo response data: ${response.data}")
                Log.d("AchievementService", "Apollo response errors: ${response.errors}")

                val message = response.data?.lockUnlockAchievement?.let {
                    Log.d(
                        "AchievementService",
                        "Received message: ${it.message}, successful: ${it.successfull}"
                    )
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                Log.e("AchievementService", "Exception during mutation", e)
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }

    override fun lockOrUnlockChallengeAchievement(
        userId: String,
        userChallengeAchievementId: String
    ): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d(
                    "AchievementService",
                    "Attempting to lock/unlock achievement for userId: $userId, achievementId: $userChallengeAchievementId"
                )

                val response = apolloClient.mutation(
                    LockUnlockChallengeAchievementMutation(
                        userId = Optional.Present(userId),
                        userChallengeAchievementId = Optional.Present(userChallengeAchievementId)
                    )
                ).execute()

                Log.d("AchievementService", "Apollo response data: ${response.data}")
                Log.d("AchievementService", "Apollo response errors: ${response.errors}")

                val message = response.data?.lockUnlockChallengeAchievement?.let {
                    Log.d(
                        "AchievementService",
                        "Received message: ${it.message}, successful: ${it.successfull}"
                    )
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                Log.e("AchievementService", "Exception during mutation", e)
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }


    override fun createChallengeAchievement(
        challengeId: String,
        achievementId: String
    ): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    CreateChallengeAchievementMutation(
                        challengeId = Optional.Present(challengeId),
                        achievementId = Optional.Present(achievementId)
                    )
                ).execute()

                val message = response.data?.createChallengeAchievement?.let {
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }

    override fun deleteChallengeAchievement(challengeAchievementId: String): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    DeleteChallengeAchievementMutation(
                        challengeAchievementId = Optional.Present(challengeAchievementId)
                    )
                ).execute()

                val message = response.data?.deleteChallengeAchievement?.let {
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }

    override fun toggleChallengeAchievementClearState(
        userId: String,
        userChallengeAchievementId: String
    ): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    LockUnlockChallengeAchievementMutation(
                        userId = Optional.Present(userId),
                        userChallengeAchievementId = Optional.Present(userChallengeAchievementId)
                    )
                ).execute()

                val message = response.data?.lockUnlockChallengeAchievement?.let {
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }

    override fun inviteUserToChallenge(
        userId: String,
        challengeId: String,
        isRequest: Boolean
    ): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    InviteUserToChallengeMutation(
                        userId = Optional.Present(userId),
                        challengeId = Optional.Present(challengeId),
                        isRequest = Optional.Present(isRequest)
                    )
                ).execute()

                val message = response.data?.inviteUserToChallenge?.let {
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }

    override fun acceptChallengeInvite(
        challengeInviteId: String,
        userId: String
    ): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    AcceptChallengeInviteMutation(
                        challengeInviteId = Optional.Present(challengeInviteId),
                        userId = Optional.Present(userId)
                    )
                ).execute()

                val message = response.data?.acceptChallengeInvite?.let {
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }

    override fun rejectChallengeInvite(
        challengeInviteId: String,
        userId: String
    ): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    RejectChallengeInviteMutation(
                        challengeInviteId = Optional.Present(challengeInviteId),
                        userId = Optional.Present(userId)
                    )
                ).execute()

                val message = response.data?.rejectChallengeInvite?.let {
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }

    override fun deleteChallengeInvite(challengeInviteId: String): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    DeleteChallengeInviteMutation(
                        challengeInviteId = Optional.Present(challengeInviteId)
                    )
                ).execute()

                val message = response.data?.deleteChallengeInvite?.let {
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }

    override fun createChallenge(
        userId: String,
        challengeName: String,
        type: String,
        image: String
    ): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    CreateChallengeMutation(
                        userId = Optional.Present(userId),
                        challengeName = Optional.Present(challengeName),
                        type = Optional.Present(type),
                        image = Optional.Present(image)
                    )
                ).execute()

                val message = response.data?.createChallenge?.let {
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }

    override fun updateChallenge(
        challengeId: String,
        newChallengeName: String,
        newType: String,
        newImage: String
    ): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    UpdateChallengeMutation(
                        challengeId = Optional.Present(challengeId),
                        newChallengeName = Optional.Present(newChallengeName),
                        newType = Optional.Present(newType),
                        newImage = Optional.Present(newImage)
                    )
                ).execute()

                val message = response.data?.updateChallenge?.let {
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }

    override fun deleteChallenge(challengeId: String): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    DeleteChallengeMutation(
                        challengeId = Optional.Present(challengeId)
                    )
                ).execute()

                val message = response.data?.deleteChallenge?.let {
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }

    override fun startChallenge(userId: String, challengeId: String): LiveData<Message> {
        val TAG = "StartChallenge"
        val liveData = MutableLiveData<Message>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d(TAG, "Starting StartChallengeMutation for userId: $userId, challengeId: $challengeId")

                val response = apolloClient.mutation(
                    StartChallengeMutation(
                        userId = Optional.Present(userId),
                        challengeId = Optional.Present(challengeId)
                    )
                ).execute()

                Log.d(TAG, "Received response from Apollo: $response")

                val message = response.data?.startChallenge?.let {
                    Log.d(TAG, "Challenge started successfully with message: ${it.message}, success: ${it.successfull}")
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: run {
                    Log.w(TAG, "startChallenge returned null from server.")
                    Message(message = "Unknown error", successfull = false)
                }

                liveData.postValue(message)
            } catch (e: Exception) {
                Log.e(TAG, "Error during startChallenge: ${e.message}", e)
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }

        return liveData
    }

    override fun endChallenge(challengeId: String): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    EndChallengeMutation(
                        challengeId = Optional.Present(challengeId)
                    )
                ).execute()

                val message = response.data?.endChallenge?.let {
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }

    override fun inviteFriend(senderId: String, receiverId: String): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    InviteFriendMutation(
                        senderId = Optional.Present(senderId),
                        receiverId = Optional.Present(receiverId)
                    )
                ).execute()

                val message = response.data?.inviteFriend?.let {
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }

    override fun acceptFriendInvite(friendId: String): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    AcceptFriendInviteMutation(
                        friendId = Optional.Present(friendId)
                    )
                ).execute()

                val message = response.data?.acceptFriendInvite?.let {
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }

    override fun rejectFriendInvite(friendId: String): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    RejectFriendInviteMutation(
                        friendId = Optional.Present(friendId)
                    )
                ).execute()

                val message = response.data?.rejectFriendInvite?.let {
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }

    override fun deleteFriendInvite(friendId: String): LiveData<Message> {
        val liveData = MutableLiveData<Message>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(
                    DeleteFriendInviteMutation(
                        friendId = Optional.Present(friendId)
                    )
                ).execute()

                val message = response.data?.deleteFriendInvite?.let {
                    Message(
                        message = it.message,
                        successfull = it.successfull
                    )
                } ?: Message(message = "Unknown error", successfull = false)

                liveData.postValue(message)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Message(message = "Request failed", successfull = false))
            }
        }
        return liveData
    }

    override fun uploadImage(imageUri: Uri, context: Context): LiveData<String?> {
        val result = MutableLiveData<String?>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val bytes =
                    context.contentResolver.openInputStream(imageUri)?.use { it.readBytes() }

                bytes?.let {
                    val requestBody = it.toRequestBody("image/*".toMediaTypeOrNull())
                    val body = MultipartBody.Part.createFormData("file", "upload.jpg", requestBody)

                    val response = imageApiService.uploadImage(body)

                    val fullPath = response.filePath
                    result.postValue(fullPath)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                result.postValue(null)
            }
        }

        return result
    }
}
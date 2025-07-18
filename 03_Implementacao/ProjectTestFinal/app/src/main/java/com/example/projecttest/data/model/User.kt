package com.example.projecttest.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var userId: String? = "",
    var userName: String? = "",
    var email: String? = "",
    var imageUrl: String? = "",
    var averageCompletion: Int? = 0,
    var totalPoints: Int? = 0,
    var lastLogin: String? = "",
    var memberSince: String? = "",
    var biography: String? = "",
    var userGames: List<UserGame>? = emptyList(),
    var userAchievements: List<UserAchievement>? = emptyList(),
    var userChallengeAchievements: List<UserChallengeAchievement>? = emptyList(),
    var userChallenges: List<UserChallenge>? = emptyList(),
    var challengeInvites: List<ChallengeInvite>? = emptyList(),
    var friendSenders: List<Friend>? = emptyList(),
    var friendReceivers: List<Friend>? = emptyList()
) : Parcelable
/*data class User(
    var id: Int,
    var userName: String,
    var email: String,
    @DrawableRes val imageUrl: Int,
    var siteRank: Int,
    var averageCompletion: Int,
    var points: Int,
    var lastLogin: String,
    var memberSince: String,
    var biography: String
) : Parcelable*/
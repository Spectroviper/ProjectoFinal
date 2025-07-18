package com.example.projecttest

import com.example.projecttest.data.model.ChallengeInvite
import com.example.projecttest.data.model.Friend
import com.example.projecttest.data.model.User
import com.example.projecttest.data.model.UserAchievement
import com.example.projecttest.data.model.UserChallenge
import com.example.projecttest.data.model.UserChallengeAchievement
import com.example.projecttest.data.model.UserGame

object MyId{
    var user: User? = null
    var id: String = "1"
    var userName: String = "teste"
    var email: String = "teste"
    var imageUrl: String = "teste"
    var averageCompletion: Int = 0
    var totalPoints: Int = 0
    var lastLogin: String = "teste"
    var memberSince: String = "teste"
    var biography: String = "teste"
    var userGames: List<UserGame>? = mutableListOf()
    var userAchievements: List<UserAchievement>? = mutableListOf()
    var userChallengeAchievements: List<UserChallengeAchievement>? = mutableListOf()
    var userChallenges: List<UserChallenge>? = mutableListOf()
    var challengeInvites: List<ChallengeInvite>? = mutableListOf()
    var friendSenders: List<Friend>? = mutableListOf()
    var friendReceivers: List<Friend>? = mutableListOf()
}
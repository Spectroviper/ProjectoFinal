package com.example.projecttest.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Challenge (
    var challengeId: String? = "",
    var challengeName: String? = "",
    var imageUrl: String? = "",
    var type: String? = "endfirst",
    var isStarted: Boolean? = false,
    var startDate: String? = "",
    var endDate: String? = "",
    var createdBy: String? = "",
    var userChallenges: List<UserChallenge>? = emptyList(),
    var challengeAchievements: List<ChallengeAchievement>? = emptyList(),
    var challengeInvites: List<ChallengeInvite>? = emptyList()

    ): Parcelable
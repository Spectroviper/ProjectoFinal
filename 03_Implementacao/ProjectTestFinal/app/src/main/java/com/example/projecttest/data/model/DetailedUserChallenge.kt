package com.example.projecttest.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailedUserChallenge(
    var userChallenge: UserChallenge? = null,
    var userChallengeAchievements: List<UserChallengeAchievement?> = emptyList()
): Parcelable

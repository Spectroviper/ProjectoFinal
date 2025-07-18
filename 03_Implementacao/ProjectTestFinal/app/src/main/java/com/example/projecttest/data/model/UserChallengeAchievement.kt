package com.example.projecttest.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserChallengeAchievement (
    var userChallengeAchievementId: String? = "",
    var achievement: Achievement? = null,
    var clear: Boolean? = false,
    var unlockDate: String? = "",
    var totalCollected: Int? = 0,
    var user: User? = null,
):  Parcelable
package com.example.projecttest.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChallengeAchievement (
    var challengeAchievementId: String? = "",
    var achievement: Achievement?

    ): Parcelable
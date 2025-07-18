package com.example.projecttest.data.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize
import java.net.URL

@Parcelize
data class Achievement(
    var achievementId: String? = "",
    var achievementName: String? = "",
    var gameId: String? = "",
    var imageUrl: String? = "",
    var createdBy: String? = "",
    var totalCollectable: Int? = 0,
    var about: String? = "",
    var challengeAchievements: List<ChallengeAchievement>? = emptyList(),
    var userAchievements: List<UserAchievement>? = emptyList()
) : Parcelable

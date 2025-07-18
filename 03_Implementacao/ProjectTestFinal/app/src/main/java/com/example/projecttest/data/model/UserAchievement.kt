package com.example.projecttest.data.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize
import java.net.URL

@Parcelize
data class UserAchievement(
    var userAchievementId: String? = "",
    var clear: Boolean? = false,
    var unlockDate: String? = "",
    var totalCollected: Int? = 0,
    var achievement: Achievement? = null,
    var user: User? = null
) : Parcelable

package com.example.project.data.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserAchievement(
    var id: Int,
    var userId: Int,
    var achievementId: Int,
    var clear: Boolean,
    var unlockDate: String,
    @DrawableRes val unlockImage: Int,
    var totalCollected: String,
) : Parcelable

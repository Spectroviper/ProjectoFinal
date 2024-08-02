package com.example.project.data.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Achievement(
    var id: Int,
    var achievementName: String,
    var gameId: Int,
    @DrawableRes val imageUrl: Int,
    var points: Int,
    var totalCollectable: Int,
    var about: String?
) : Parcelable

package com.example.project.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
data class UserGame(
    var userId: Int,
    var gameId: Int,
    var lastPlayedGame: Boolean
) : Parcelable

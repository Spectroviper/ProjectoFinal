package com.example.projecttest.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
data class UserGame(
    var userGameId: String? = "",
    var userId: String? = "",
    var gameId: String? = "",
    var averageCompletion: Float? = 0f,
    var game: Game?
) : Parcelable

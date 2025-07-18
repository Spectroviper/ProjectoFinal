package com.example.projecttest.data.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize
import java.net.URL

@Parcelize
data class Game(
    var gameId: String? = "",
    var gameName: String? = "",
    var imageUrl: String? = "",
    var console: String? = "",
    var developer: String? = "",
    var publisher: String? = "",
    var genre: String? = "",
    var releaseDate: String? = "",
    var about: String? = "",
    var createdBy: String? = "",
    var userGames: List<UserGame>? = emptyList(),
    var achievements: List<Achievement>? = emptyList()
) : Parcelable

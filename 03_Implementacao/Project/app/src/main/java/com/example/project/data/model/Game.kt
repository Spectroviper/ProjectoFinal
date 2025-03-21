package com.example.project.data.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Game(
    var id: String,
    var gameName: String,
    @DrawableRes val imageUrl: String,
    var console: String,
    var developer: String,
    var publisher: String,
    var genre: String,
    var releaseDate: String,
    var about: String
) : Parcelable

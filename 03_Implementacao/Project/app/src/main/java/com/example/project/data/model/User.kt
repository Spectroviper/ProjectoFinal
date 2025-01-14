package com.example.project.data.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: String,
    var userName: String,
    var email: String,
    val imageUrl: String,
    var siteRank: Int,
    var averageCompletion: Int,
    var points: Int,
    var lastLogin: String,
    var memberSince: String,
    var biography: String
) : Parcelable
/*data class User(
    var id: Int,
    var userName: String,
    var email: String,
    @DrawableRes val imageUrl: Int,
    var siteRank: Int,
    var averageCompletion: Int,
    var points: Int,
    var lastLogin: String,
    var memberSince: String,
    var biography: String
) : Parcelable*/
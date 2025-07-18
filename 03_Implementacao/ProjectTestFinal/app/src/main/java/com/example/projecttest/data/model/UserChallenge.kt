package com.example.projecttest.data.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserChallenge (
    var userChallengeId: String? = "",
    var challenge: Challenge? = null,
    var user: User? = null,
    var isLeader: Boolean? = false,
    var averageCompletion: Float? = 0f,

    ): Parcelable
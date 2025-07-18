package com.example.projecttest.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChallengeInvite (
    var challengeInviteId: String? = "",
    var state: String? = "",
    var date: String? = "",
    var user: User? = null,
    var isRequest: Boolean? = false,
    var userId: String = "",
    var challenge: Challenge?,


): Parcelable
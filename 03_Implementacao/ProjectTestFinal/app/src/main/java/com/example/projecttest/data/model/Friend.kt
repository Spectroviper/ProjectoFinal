package com.example.projecttest.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Friend(
    var friendId: String? = "",
    var userSender: User? = null,
    var userReceiver: User? = null,
    var state: String? = "",
    var date: String? = "",
) : Parcelable
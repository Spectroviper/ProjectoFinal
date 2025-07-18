package com.example.projecttest.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(
    var successfull: Boolean? = false,
    var message: String? = "",
) : Parcelable
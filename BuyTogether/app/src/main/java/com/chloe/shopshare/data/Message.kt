package com.chloe.shopshare.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Message(
    var id: String = "",
    var time: Long = 0,
    val talkerId: String = "",
    val message: String?= null,
    val image: String?= null,
    val check: Boolean = false
): Parcelable
package com.chloe.shopshare.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatRoom(
    var id: String = "",
    val talker: List<String> = listOf()
): Parcelable
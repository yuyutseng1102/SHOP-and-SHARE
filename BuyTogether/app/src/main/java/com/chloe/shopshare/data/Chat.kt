package com.chloe.shopshare.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Chat(
    var chatRoom: ChatRoom? = null,
    val friendProfile: User? = null,
    var message: List<Message>? = null
): Parcelable
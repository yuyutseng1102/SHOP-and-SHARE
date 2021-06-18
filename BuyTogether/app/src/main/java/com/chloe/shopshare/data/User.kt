package com.chloe.shopshare.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val provider: String = "",
    val id: String = "",
    val name: String = "",
    val email: String?= null,
    val photo: String?= null,
    val like: List<String>? = null
): Parcelable {
}


@Parcelize
data class ChatRoomKey(
    val myId: String = "",
    val friendId: String = "",
    val chatRoomId: String = ""
): Parcelable





//@Parcelize
//data class ChatRoom(
//    var id: String = "",
//    val talker: List<String> = listOf(),
//    val lastMessage: String = "",
//    val time: Long = 0L
//): Parcelable{
//    var friendInfo : User? = null
//}

package com.chloe.buytogether.data

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class User(
    val userId: Long = 0L,
    val googleId: Long = 0L,
    val name: String = "",
    val subscribe: List<Long>,
    val notify:List<Notify>
): Parcelable {
}

@Parcelize
data class Notify(
    val notifyId:Long,
    val id: Long,
    val notifyTime: Long= Calendar.getInstance().timeInMillis,
    val notifyType: Int,
    val content: String
): Parcelable {

    @IgnoredOnParcel
    var isCheck: Boolean = false
}
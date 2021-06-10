package com.chloe.shopshare.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val userId: Long = 0L,
    val googleId: Long = 0L,
    val name: String = "",
    val subscribe: List<Long>,
    val notify:List<Notify>
): Parcelable {
}


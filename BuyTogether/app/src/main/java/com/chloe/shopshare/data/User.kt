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
    val subscribe: List<Long>? = null
): Parcelable {
}




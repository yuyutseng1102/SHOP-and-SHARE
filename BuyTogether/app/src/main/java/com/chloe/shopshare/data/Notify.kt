package com.chloe.shopshare.data

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Notify(
    val id: String,
    val shopId: String,
    val time: Long,
    val type: Int,
    val content: String
): Parcelable {

    @IgnoredOnParcel
    var isCheck: Boolean = false
}
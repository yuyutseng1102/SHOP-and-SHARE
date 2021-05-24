package com.chloe.shopshare.data

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Notify(
    val notifyId:Long,
    val id: Long,
    val notifyTime: Long,
    val notifyType: Int,
    val content: String
): Parcelable {

    @IgnoredOnParcel
    var isCheck: Boolean = false
}
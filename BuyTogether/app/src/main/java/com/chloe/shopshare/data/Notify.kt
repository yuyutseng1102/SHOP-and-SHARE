package com.chloe.shopshare.data

import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
@IgnoreExtraProperties
@Parcelize
data class Notify(
    val id: String,
    val shopId: String,
    val time: Long,
    val type: Int,
    val content: String
): Parcelable {
    @field:JvmField
    @IgnoredOnParcel
    var isCheck: Boolean = false
}
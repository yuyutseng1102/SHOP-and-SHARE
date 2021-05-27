package com.chloe.shopshare.data

import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
@IgnoreExtraProperties
@Parcelize
data class Notify(
    var id: String = "",
    val shopId: String = "",
    val orderId: String? = null,
    var time: Long = 0L,
    val type: Int = 10,
    val content: String? = null
): Parcelable {
    @field:JvmField
    @IgnoredOnParcel
    var isCheck: Boolean = false
}
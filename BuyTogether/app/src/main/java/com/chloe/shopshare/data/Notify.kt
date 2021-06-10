package com.chloe.shopshare.data

import android.os.Message
import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
@IgnoreExtraProperties
@Parcelize
data class Notify(
    var id: String = "",
    val shopId: String = "",
    var orderId: String? = null,
    val requestId: String? = null,
    var time: Long = 0L,
    val type: Int = 10,
    val title: String = "",
    val content: String =  "",
    var message: String? = null
): Parcelable {
    @field:JvmField
    @IgnoredOnParcel
    var isCheck: Boolean = false
}
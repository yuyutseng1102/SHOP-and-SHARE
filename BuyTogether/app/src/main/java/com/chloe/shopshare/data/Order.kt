package com.chloe.shopshare.data

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    val id: String = "",
    val time: Long? = null,
    val userId: String = "",
    val product:List<Product> = listOf(),
    val price: Int = 0,
    val phone:String = "",
    val delivery: Int = 0,
    val address: String = "",
    val note: String? = null,
    var paymentStatus: Int = 0
): Parcelable {
    @IgnoredOnParcel
    var isCheck: Boolean = false
}
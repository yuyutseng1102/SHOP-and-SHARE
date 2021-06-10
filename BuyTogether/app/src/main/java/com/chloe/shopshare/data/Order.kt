package com.chloe.shopshare.data

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    var id: String = "",
    var time: Long? = null,
    val userId: String = "",
    val product:List<Product> = listOf(),
    val price: Int = 0,
    val name: String = "",
    val phone:String = "",
    val delivery: Int = 0,
    val address: String = "",
    val note: String? = null,
    var paymentStatus: Int = 0
): Parcelable {
    @IgnoredOnParcel
    var isCheck: Boolean = false
}


@Parcelize
data class MyOrder(
    var shop: Shop,
    var order: Order
): Parcelable {
}

@Parcelize
data class MyOrderDetailKey(
    var shopId: String = "",
    var orderId: String = ""
): Parcelable {
}



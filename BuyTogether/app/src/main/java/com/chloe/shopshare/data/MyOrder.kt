package com.chloe.shopshare.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyOrder(
    var shop: Shop,
    var order: Order
): Parcelable {
}

@Parcelize
data class Cart(
    var shop: Shop,
    var products: List<Product>
): Parcelable {
}
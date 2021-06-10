package com.chloe.shopshare.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val title: String = "",
    var quantity: Int? = null
) : Parcelable
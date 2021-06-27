package com.chloe.shopshare.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Track(
    var shopId: String = "",
    var orderId: String = ""
): Parcelable
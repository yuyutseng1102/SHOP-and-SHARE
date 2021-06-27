package com.chloe.shopshare.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Filter(
    val category : Int? = null,
    val country: Int? = null
): Parcelable
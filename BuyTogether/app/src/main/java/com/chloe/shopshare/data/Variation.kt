package com.chloe.shopshare.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Variation(
    val value: List<String>,
    val isStandard: Boolean,
    val content: String
) : Parcelable
package com.chloe.shopshare.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Condition(
    val type: Int?,
    val deadLine: Long?,
    val value: Int?,
    val content: String?
) : Parcelable
package com.chloe.shopshare.data

import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class Request(
    var id: String = "",
    val userId: String = "",
    var time: Long? = null,
    val mainImage: String="",
    val image: List<String> = listOf(""),
    val title: String = "",
    val description:String = "",
    val category: Int = 0,
    val country: Int = 0,
    val source: String = "",
    var host: String? = null,
    var shopId: String? = null,
    var member:List<String>? = null
): Parcelable
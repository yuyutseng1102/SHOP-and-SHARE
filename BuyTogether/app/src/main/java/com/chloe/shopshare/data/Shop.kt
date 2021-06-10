package com.chloe.shopshare.data

import android.os.Parcelable
import com.chloe.shopshare.ext.toDisplayFormat
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class Shop(
    var id: String = "",
    val userId: String = "",
    var time: Long? = null,
    val type: Int? = null,
    val mainImage: String="",
    val image: List<String> = listOf(""),
    val title: String = "",
    val description:String = "",
    val category: Int = 0,
    val country: Int = 0,
    val source: String = "",
    @field:JvmField
    val isStandard: Boolean = false,
    var option:List<String> = listOf(""),
    var deliveryMethod: List<Int> = listOf(),
    val conditionType: Int? = null,
    val deadLine: Long? =null,
    val condition: Int? = null,
    var status: Int = 0,
    var order:List<Order?>? = null
):Parcelable{
    var member : Int = 0
}



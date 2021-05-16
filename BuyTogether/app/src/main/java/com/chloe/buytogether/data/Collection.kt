package com.chloe.buytogether.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
data class Collections(
        val id: Long = 0L,
        val userId: Long = 0L,
        val time: Long= Calendar.getInstance().timeInMillis,
        val method: String="",
        val mainImage: String="",
        val image: List<String> = listOf(""),
        val title: String = "",
        val description:String = "",
        val category: String = "",
        val country: String = "",
        val source: String = "",
        val isCustom: Boolean = true,
        val option:List<String> = listOf(""),
        val deliveryMethod: String = "",
        val conditionType: Int? = null,
        val deadLine : Long? =null ,
        val condition: String? = null,
        val status: Int = 0,
        val order:List<Order>? = null
):Parcelable{
    val memberToDisplay : String
        get()= "已跟團${order?.size}人"
    val followToDisplay : String
        get()= "有興趣${order?.size}人"

}

@Parcelize
data class Order(
    val orderId: Long = 0L,
    val time: Long= Calendar.getInstance().timeInMillis,
    val userId:Long = 0L,
    val product:List<Product>,
    val price: Int = 0,
    val phone:String = "",
    val delivery: String = "",
    val note: String? = null,
    val paymentStatus: Boolean = false
):Parcelable


@Parcelize
data class Product(
    val productName: String = "",
    val quantity: Int = 0
) : Parcelable


@Parcelize
data class MockCollections(
    val mainImage: String,
    val title: String,
    val condition:String,
    val memberSize:Int
):Parcelable{
    val memberToDisplay : String
        get()= "已跟團${memberSize}人"
    val followToDisplay : String
        get()= "有興趣${memberSize}人"

}
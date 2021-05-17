package com.chloe.buytogether.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Collections(
    val id: Long = 0L,
    val userId: Long = 0L,
    val time: Long,
    val method: String,
    val mainImage: String,
    val image: List<String>,
    val title: String = "",
    val description:String,
    val category: String,
    val country: String,
    val web: String,
    val isCustom: Boolean,
    val option:List<String>,
    val deliveryMethod: String,
    val condition:String,
    val status: String,
    val order:List<Order>
):Parcelable{
    val memberToDisplay : String
        get()= "已跟團${order.size}人"
    val followToDisplay : String
        get()= "有興趣${order.size}人"

}

@Parcelize
data class Order(
    val orderId: Long,
    val time: Long,
    val userId:Long,
    val product:List<Product>,
    val price: Int,
    val phone:String,
    val delivery: String,
    val note: String,
    val paymentStatus: Boolean
):Parcelable


@Parcelize
data class Product(
    val productName: String,
    val quantity: Int
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
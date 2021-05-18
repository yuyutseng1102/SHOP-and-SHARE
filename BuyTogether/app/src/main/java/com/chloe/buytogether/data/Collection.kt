package com.chloe.buytogether.data

import android.graphics.Insets.add
import android.os.Parcelable
import com.chloe.buytogether.ext.toDisplayFormat
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
        val isStandard: Boolean = false,
        val option:List<String> = listOf(""),
        val deliveryMethod: String = "",
        val conditionType: Int? = null,
        val deadLine : Long? =null ,
        val condition: Int? = null,
        val status: Int = 0,
        val order:List<Order>? = null
):Parcelable{
    val memberToDisplay : String
        get()= "已跟團${order?.size}人"
    val followToDisplay : String
        get()= "有興趣${order?.size}人"
    val deadLineToDisplay : String?
        get()= "預計${deadLine?.toDisplayFormat()}收團"
    val conditionToDisplay : String?
        get()=
            when (conditionType){
                0-> "滿額NT$${condition}成團"
                1-> "徵滿${condition}份成團"
                2-> "徵滿${condition}人成團"
                else -> ""
            }

}

@Parcelize
data class Order(
    val orderId: Long = 0L,
    val orderTime: Long= Calendar.getInstance().timeInMillis,
    val userId:Long = 0L,
    val product:List<Product>,
    val price: Int = 0,
    val phone:String = "",
    val delivery: String = "",
    val note: String? = null,
    val paymentStatus: Int = 0
):Parcelable


@Parcelize
data class Product(
    val productTitle: String = "",
    val quantity: Int = 0
) : Parcelable

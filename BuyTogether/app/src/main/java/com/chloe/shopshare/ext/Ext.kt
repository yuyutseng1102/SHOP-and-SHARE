package com.chloe.shopshare.ext

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Parcelable
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.util.*

fun Long.toDisplayFormat(): String {
    return SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN).format(this)
}




//fun addShop() {
//    val shop = FirebaseFirestore.getInstance().collection("shop")
//    val document = shop.document()
//    val data = hashMapOf(
//        "id" to document.id,
//        "userId" to 1999999,
//        "time" to Calendar.getInstance().timeInMillis,
//        "method" to 1,
//        "mainImage" to "",
//        "image" to,
//
//        "author" to hashMapOf(
//            "email" to "wayne@school.appworks.tw",
//            "id" to "waynechen323",
//            "name" to "AKA小安老師"
//        ),
//        "title" to "IU「亂穿」竟美出新境界！笑稱自己品味奇怪　網笑：靠顏值撐住女神氣場",
//        "content" to "南韓歌手IU（李知恩）無論在歌唱方面或是近期的戲劇作品都有亮眼的成績，但俗話說人無完美、美玉微瑕，曾再跟工作人員的互動影片中坦言自己品味很奇怪，近日在IG上分享了宛如「媽媽們青春時代的玉女歌手」超復古穿搭造型，卻意外美出新境界。",
//        "createdTime" to Calendar.getInstance().timeInMillis,
//        "id" to document.id,
//        "tag" to "Beauty"
//    )
//    document.set(data)
//}
//
//@Parcelize
//data class Shop(
//    val id: Long = 0L,
//    val userId: Long = 0L,
//    val time: Long= java.util.Calendar.getInstance().timeInMillis,
//    val method: Int,
//    val mainImage: String="",
//    val image: List<String> = listOf(""),
//    val title: String = "",
//    val description:String = "",
//    val category: Int = 0,
//    val country: Int = 0,
//    val source: String = "",
//    val isStandard: Boolean = false,
//    val option:List<String> = listOf(""),
//    val deliveryMethod: List<Int> = listOf(),
//    val conditionType: Int? = null,
//    val deadLine : Long? =null,
//    val condition: Int? = null,
//    var status: Int = 0,
//    var order:List<Order?>? = null
//): Parcelable {
//    val memberToDisplay : String
//        get()= "已跟團${order?.size}人"
//    val followToDisplay : String
//        get()= "有興趣${order?.size}人"
//    val deadLineToDisplay : String?
//        get()= "預計${deadLine?.toDisplayFormat()}收團"
//    val conditionToDisplay : String?
//        get()=
//            when (conditionType){
//                0-> "滿額NT$${condition}成團"
//                1-> "徵滿${condition}份成團"
//                2-> "徵滿${condition}人成團"
//                else -> ""
//            }
//
//}
//
//@Parcelize
//data class Order(
//    val orderId: Long = 0L,
//    val orderTime: Long= java.util.Calendar.getInstance().timeInMillis,
//    val userId:Long = 0L,
//    val product:List<Product>,
//    val price: Int = 0,
//    val phone:String = "",
//    val delivery: Int = 0,
//    val address: String = "",
//    val note: String? = null,
//    var paymentStatus: Int = 0
//): Parcelable {
//    @IgnoredOnParcel
//    var isCheck: Boolean = false
//}
//
//
//@Parcelize
//data class Product(
//    val productTitle: String = "",
//    var quantity: Int? = null
//) : Parcelable

package com.chloe.shopshare.ext

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.util.Log
import com.chloe.shopshare.data.Order
import com.chloe.shopshare.data.Product
import com.chloe.shopshare.myhost.OrderStatusType
import com.chloe.shopshare.notify.NotifyType
import java.util.*

fun Long.toDisplayThisYearDateFormat(): String {
    return SimpleDateFormat("MM/dd", Locale.TAIWAN).format(this)
}

fun Long.toDisplayDateFormat(): String {
    return SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN).format(this)
}

fun Long.toDisplayTimeFormat(): String {
    return SimpleDateFormat("HH:mm", Locale.TAIWAN).format(this)
}

fun Long.toDisplayDateTimeFormat(): String {
    return SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.TAIWAN).format(this)
}
fun Long.toDisplayThisYearDateTimeFormat(): String {
    return SimpleDateFormat("MM/dd HH:mm", Locale.TAIWAN).format(this)
}

fun Long.toDisplayDateWeekFormat(): String {

    val date = this.toDisplayDateFormat()
    val calendar = Calendar.getInstance()
    calendar.time = Date(this)
    val week = getWeek(calendar.get(Calendar.DAY_OF_WEEK))

    return "$date $week"
}

@SuppressLint("SimpleDateFormat")
fun Long.isShopExpiredToday(): Boolean {

    this.let {
        val dayFormat = SimpleDateFormat("yyyy-MM-dd")
        val todayDate = dayFormat.format(Date(System.currentTimeMillis()))
        val targetDate = dayFormat.format(Date(this))
        val today = dayFormat.parse(todayDate).time
        val target = dayFormat.parse(targetDate).time
        return when (today - target) {
            0L -> true
            else -> false
        }
    }
}

@SuppressLint("SimpleDateFormat")
fun Long.getDay(): String {

    val secondsInMilli = 1000L
    val minutesInMilli = secondsInMilli * 60
    val hoursInMilli = minutesInMilli * 60
    val daysInMilli = hoursInMilli * 24
    val mothsInMilli = daysInMilli * 30
    val yearInMilli = mothsInMilli * 12

    val DAY = daysInMilli
    this.let {
        val dayFormat = SimpleDateFormat("yyyy-MM-dd")
        val todayDate = dayFormat.format(Date(System.currentTimeMillis()))
        val targetDate = dayFormat.format(Date(this))
        val today = dayFormat.parse(todayDate).time
        val target = dayFormat.parse(targetDate).time
        val dayBetween = today - target
        return when {
            dayBetween == 0L -> this.toDisplayTimeFormat()
            dayBetween == DAY -> "昨天 ${this.toDisplayTimeFormat()}"
            dayBetween in (DAY + 1) until yearInMilli -> this.toDisplayThisYearDateTimeFormat()
            else -> this.toDisplayDateTimeFormat()
        }
    }
}




@SuppressLint("SimpleDateFormat")
fun Long.getDayWeek(): String {

    val secondsInMilli = 1000L
    val minutesInMilli = secondsInMilli * 60
    val hoursInMilli = minutesInMilli * 60
    val daysInMilli = hoursInMilli * 24
    val mothsInMilli = daysInMilli * 30
    val yearInMilli = mothsInMilli * 12

    val DAY = daysInMilli
    this.let {
        val dayFormat = SimpleDateFormat("yyyy-MM-dd")
        val todayDate = dayFormat.format(Date(System.currentTimeMillis()))
        val targetDate = dayFormat.format(Date(this))
        val today = dayFormat.parse(todayDate).time
        val target = dayFormat.parse(targetDate).time
        val dayBetween = today - target
        return when {
            dayBetween == 0L -> this.toDisplayTimeFormat()
            dayBetween == DAY -> "昨天"
            dayBetween < 7*DAY &&  dayBetween > DAY -> {
                val calendar = Calendar.getInstance()
                calendar.time = Date(this)
                getWeek(calendar.get(Calendar.DAY_OF_WEEK))
            }
            dayBetween < yearInMilli && dayBetween >= 7*DAY -> this.toDisplayThisYearDateFormat()
            else -> this.toDisplayDateFormat()
        }
    }
}

fun getWeek(week: Int): String {
    var weekToString = ""
    weekToString =
    when (week) {
        Calendar.MONDAY -> "星期一"
        Calendar.TUESDAY -> "星期二"
        Calendar.WEDNESDAY -> "星期三"
        Calendar.THURSDAY -> "星期四"
        Calendar.FRIDAY -> "星期五"
        Calendar.SATURDAY -> "星期六"
        Calendar.SUNDAY -> "星期日"
        else -> ""
    }
    return weekToString
}

fun NotifyType.toDisplayNotifyContent(shopTitle:String): String {
    return when(this){
        NotifyType.STATUS_CHANGE_TO_GATHER_SUCCESS -> "您參與的團購 : $shopTitle 已經成團囉 ! 快去看看團購詳情吧 ! "
        NotifyType.STATUS_CHANGE_TO_ORDER_SUCCESS -> "您參與的團購 : $shopTitle 最新進度為 ${OrderStatusType.ORDER_SUCCESS.title} 喔 ! "
        NotifyType.STATUS_CHANGE_TO_SHOP_SHIPMENT -> "您參與的團購 : $shopTitle 最新進度為 ${OrderStatusType.SHOP_SHIPMENT.title} 喔 ! "
        NotifyType.STATUS_CHANGE_TO_SHIPMENT_SUCCESS -> "您參與的團購 : $shopTitle 最新進度為 ${OrderStatusType.SHIPMENT_SUCCESS.title} 喔 ! "
        NotifyType.STATUS_CHANGE_TO_PACKAGING -> "您參與的團購 : $shopTitle 最新進度為 ${OrderStatusType.PACKAGING .title} 喔 ! "
        NotifyType.STATUS_CHANGE_TO_SHIPMENT -> "您參與的團購 : $shopTitle 商品已經寄出囉 ! "
        NotifyType.STATUS_CHANGE_TO_FINISH -> "您參與的團購 : $shopTitle 已經順利結束囉 ! 再逛逛其他團購吧 ! "
        NotifyType.ORDER_FAIL -> "您查看的團購 : $shopTitle 經主購審核後 , 未能符合其團員資格 , 別灰心 ! 再逛逛其他團購吧 ! "
        NotifyType.ORDER_INCREASE -> "您的團購 : $shopTitle 剛剛有人+1囉 ! "
        NotifyType.PRICE_REACH_CONDITION -> "您的團購 : $shopTitle 總金額已達OOO , 快去團購管理確認吧 ! "
        NotifyType.QUANTITY_REACH_CONDITION -> "您的團購 : $shopTitle 總件數已達OOO , 快去團購管理確認吧 ! "
        NotifyType.MEMBER_REACH_CONDITION -> "您的團購 : $shopTitle 跟團人數已達OOO , 快去團購管理確認吧 ! "
        NotifyType.REACH_DEADLINE -> "您的團購 : $shopTitle 已達預定成團日囉 , 快去團購管理確認吧 ! "
        NotifyType.REQUEST_SUCCESS_REQUESTER -> "您的徵團文 : $shopTitle 已經有人開團囉 ! 快去看看吧 ! "
        NotifyType.REQUEST_SUCCESS_MEMBER -> "您有興趣的徵團文 : $shopTitle 已經有人開團囉 ! 快去看看吧 ! "
        NotifyType.SYSTEM_NOTIFY -> ""
    }

}

fun NotifyType.toDisplayNotifyMessage(order: Order): String {

    Log.d("Chloe","getProductList(order.product) = ${order.product}")
    return when(this){
        NotifyType.ORDER_INCREASE -> "訂單編號 : ${order.id}\n" +
                "商品明細 : ${getProductList(order.product)}\n" +
                "訂單金額 : NT\$${order.price}"
        NotifyType.ORDER_FAIL ->  "訂單編號 : ${order.id}\n" +
                "商品明細 : ${getProductList(order.product)}\n" +
                "訂單金額 : NT\$${order.price}"

        else -> ""
    }
}

fun getProductList(product:List<Product>): String{
    Log.d("Chloe","product:List<Product> = ${product}")
    var content: String =""
    for(item in product){
        content += "${item.title} X ${item.quantity},"
    }
    Log.d("Notify","content = $content")
    content.replaceFirst(".$","")
    Log.d("Notify","product in message = $content")
    return content
}





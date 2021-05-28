package com.chloe.shopshare.ext

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Parcelable
import android.util.Log
import com.chloe.shopshare.data.Order
import com.chloe.shopshare.data.Product
import com.chloe.shopshare.notify.NotifyType
import com.chloe.shopshare.shop.OrderStatusType
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.util.*

fun Long.toDisplayFormat(): String {
    return SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN).format(this)
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
        NotifyType.SOON_TO_END -> "您追蹤的團購 : $shopTitle 即將成團囉 ! 趕快把握機會+1吧 ! "
        NotifyType.SYSTEM_NOTIFY -> ""
    }
}

fun NotifyType.toDisplayNotifyMessage(order: Order): String {
    return when(this){
        NotifyType.ORDER_INCREASE -> "訂單編號 : ${order.id}\n" +
                "商品明細 : ${getProductList(order.product)}\n" +
                "訂單金額 : NT\$${order.price}\n"
        else -> ""
    }
}

fun getProductList(product:List<Product>): String{
    val content: String =""
    for(item in product){
        content + "${item.title}X${item.quantity},"
    }
    content.replaceFirst(".$","")
    Log.d("Notify","product in message = $content")
    return content
}





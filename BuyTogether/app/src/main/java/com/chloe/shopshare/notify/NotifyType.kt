package com.chloe.shopshare.notify

import com.chloe.shopshare.R
import com.chloe.shopshare.util.Util

enum class NotifyType(type:Int, title: String) {
    BY_STATUS_CHANGE(10, Util.getString(R.string.by_status_change)),
    BY_ORDER_FAIL(11,Util.getString(R.string.by_order_fail)),
    BY_PAYMENT_NOTIFY(12,Util.getString(R.string.by_payment_notify)),
    BY_MEMBER_INCREASE(20,Util.getString(R.string.by_member_increase)),
    BY_REACH_CONDITION(21,Util.getString(R.string.by_reach_condition)),
    BY_SOON_TO_END(30,Util.getString(R.string.by_soon_to_end))
}


package com.chloe.shopshare.notify

import com.chloe.shopshare.R
import com.chloe.shopshare.util.Util

enum class NotifyType(type:Int, title: String) {

    STATUS_CHANGE_TO_GATHER_SUCCESS(1001, Util.getString(R.string.status_change_to_gather_success)),
    STATUS_CHANGE_TO_ORDER_SUCCESS(1002,Util.getString(R.string.status_change)),
    STATUS_CHANGE_TO_SHOP_SHIPMENT(1003,Util.getString(R.string.status_change)),
    STATUS_CHANGE_TO_SHIPMENT_SUCCESS(1004,Util.getString(R.string.status_change)),
    STATUS_CHANGE_TO_PACKAGING(1005,Util.getString(R.string.status_change)),
    STATUS_CHANGE_TO_SHIPMENT(1006,Util.getString(R.string.status_change_to_shipment)),
    STATUS_CHANGE_TO_FINISH(1007,Util.getString(R.string.status_change_to_finish)),
    ORDER_FAIL(1101,Util.getString(R.string.order_fail)),
    ORDER_INCREASE(2001,Util.getString(R.string.order_increase)),
    PRICE_REACH_CONDITION(2101,Util.getString(R.string.reach_condition)),
    QUANTITY_REACH_CONDITION(2102,Util.getString(R.string.reach_condition)),
    MEMBER_REACH_CONDITION(2103,Util.getString(R.string.reach_condition)),
    REACH_DEADLINE(2104,Util.getString(R.string.reach_deadline)),
    SOON_TO_END(3001,Util.getString(R.string.soon_to_end))

}


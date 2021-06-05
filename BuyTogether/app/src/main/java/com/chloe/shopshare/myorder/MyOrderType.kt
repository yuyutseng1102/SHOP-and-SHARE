package com.chloe.shopshare.myorder

import com.chloe.shopshare.R
import com.chloe.shopshare.myhost.OrderStatusType
import com.chloe.shopshare.util.Util

enum class MyOrderType(val position: Int, val title: String, val status: List<Int>) {
    OPENING_ORDER(0, Util.getString(R.string.opening_order), listOf(OrderStatusType.GATHERING.status)),
    PROCESS_ORDER(1, Util.getString(R.string.process), listOf(OrderStatusType.GATHER_SUCCESS.status,OrderStatusType.ORDER_SUCCESS.status,OrderStatusType.SHOP_SHIPMENT.status)),
    SHIPMENT_ORDER(2, Util.getString(R.string.shipment_way), listOf(OrderStatusType.SHIPMENT_SUCCESS.status, OrderStatusType.PACKAGING.status)),
    FINISHED_ORDER(3, Util.getString(R.string.finished_request), listOf(OrderStatusType.SHIPMENT.status,OrderStatusType.FINISH.status))
}
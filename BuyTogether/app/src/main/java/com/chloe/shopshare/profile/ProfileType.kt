package com.chloe.shopshare.profile

import com.chloe.shopshare.R
import com.chloe.shopshare.myhost.OrderStatusType
import com.chloe.shopshare.util.Util

enum class MyHostShortType(val position: Int, val title: String, val status: List<Int>) {
    OPENING_SHOP(0, Util.getString(R.string.opening_order), listOf(OrderStatusType.GATHERING.status)),
    ONGOING_SHOP(1, "進行中", listOf(OrderStatusType.GATHER_SUCCESS.status, OrderStatusType.ORDER_SUCCESS.status, OrderStatusType.SHOP_SHIPMENT.status,OrderStatusType.SHIPMENT_SUCCESS.status, OrderStatusType.PACKAGING.status))
}

enum class MyOrderShortType(val position: Int, val title: String, val status: List<Int>) {
    OPENING_ORDER(0, Util.getString(R.string.opening_order), listOf(OrderStatusType.GATHERING.status)),
    ONGOING_ORDER(1, "進行中", listOf(OrderStatusType.GATHER_SUCCESS.status,OrderStatusType.ORDER_SUCCESS.status,OrderStatusType.SHOP_SHIPMENT.status,OrderStatusType.SHIPMENT_SUCCESS.status, OrderStatusType.PACKAGING.status))
}
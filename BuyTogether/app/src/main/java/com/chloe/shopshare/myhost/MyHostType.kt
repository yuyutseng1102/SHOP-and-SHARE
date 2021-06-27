package com.chloe.shopshare.myhost

import com.chloe.shopshare.R
import com.chloe.shopshare.util.Util.getString

enum class OrderStatusType(val status: Int, val title: String, val shortTitle: String) {
    GATHERING(0, getString(R.string.gathering), getString(R.string.gathering)),
    GATHER_SUCCESS(1, getString(R.string.gather_success), getString(R.string.process)),
    ORDER_SUCCESS(2, getString(R.string.order_success), getString(R.string.process)),
    SHOP_SHIPMENT(3, getString(R.string.shop_shipment), getString(R.string.process)),
    SHIPMENT_SUCCESS(4, getString(R.string.shipment_success), getString(R.string.shipment_way)),
    PACKAGING(5, getString(R.string.packaging), getString(R.string.shipment_way)),
    SHIPMENT(6, getString(R.string.shipment), getString(R.string.already_ship)),
    FINISH(7, getString(R.string.finish), getString(R.string.finish)),
}

enum class PaymentStatusType(val paymentStatus: Int, val title: String) {
    PENDING(0, getString(R.string.pending)),
    PAYMENT_PENDING(1, getString(R.string.payment_pending)),
    PAYMENT_CONFIRM(2, getString(R.string.payment_confirm)),
    REFUND_PENDING(3, getString(R.string.refund_pending)),
    REFUND_CONFIRM(4, getString(R.string.refund_confirm)),
    BALANCE_PENDING(5, getString(R.string.balance_pending)),
    BALANCE_CONFIRM(6, getString(R.string.balance_confirm))
}

enum class MyHostType(val position: Int, val title: String, val status: List<Int>) {
    ALL_SHOP(0, getString(R.string.all_request), listOf()),
    OPENING_SHOP(1, getString(R.string.opening_order), listOf(OrderStatusType.GATHERING.status)),
    PROCESS_SHOP(2, getString(R.string.process), listOf(
            OrderStatusType.GATHER_SUCCESS.status,
            OrderStatusType.ORDER_SUCCESS.status,
            OrderStatusType.SHOP_SHIPMENT.status)),
    SHIPMENT_SHOP(3, getString(R.string.shipment_way), listOf(OrderStatusType.SHIPMENT_SUCCESS.status, OrderStatusType.PACKAGING.status)),
    FINISH_SHOP(4, getString(R.string.finished_request), listOf(OrderStatusType.SHIPMENT.status, OrderStatusType.FINISH.status))
}


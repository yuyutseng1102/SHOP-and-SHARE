package com.chloe.shopshare.collection

import com.chloe.shopshare.R
import com.chloe.shopshare.util.Util.getString

enum class OrderStatusType(val status: Int,val title:String)  {
    GATHERING(0,getString(R.string.gathering)),
    GATHER_SUCCESS(1,getString(R.string.gather_success)),
    ORDER_SUCCESS(2,getString(R.string.order_success)),
    SHOP_SHIPMENT(3,getString(R.string.shop_shipment)),
    SHIPMENT_SUCCESS(4,getString(R.string.shipment_success)),
    PACKAGING(5,getString(R.string.packaging)),
    SHIPMENT(6,getString(R.string.shipment)),
    FINISH(7,getString(R.string.finish)),
}

enum class PaymentStatusType(val paymentStatus: Int,val title:String)  {
    PENDING(0,getString(R.string.pending)),
    PAYMENT_PENDING(1,getString(R.string.payment_pending)),
    PAYMENT_CONFIRM(2,getString(R.string.payment_confirm)),
    REFUND_PENDING(3,getString(R.string.refund_pending)),
    REFUND_CONFIRM(4,getString(R.string.refund_confirm)),
    BALANCE_PENDING(5,getString(R.string.balance_pending)),
    BALANCE_CONFIRM(6,getString(R.string.balance_confirm))
}


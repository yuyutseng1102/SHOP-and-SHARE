package com.chloe.shopshare.ext

import androidx.fragment.app.Fragment
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.data.MyOrderDetailKey
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.Product
import com.chloe.shopshare.data.Request
import com.chloe.shopshare.factory.*
import com.chloe.shopshare.myhost.MyHostType
import com.chloe.shopshare.myorder.MyOrderType
import com.chloe.shopshare.myrequest.MyRequestType

/**
 *
 * Extension functions for Fragment.
 */
fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as MyApplication).repository
    return ViewModelFactory(repository)
}


fun Fragment.getVmFactory(option: List<String>?,isStandard:Boolean): OptionViewModelFactory {
    val repository = (requireContext().applicationContext as MyApplication).repository
    return OptionViewModelFactory(repository, option, isStandard)
}

fun Fragment.getVmFactory(collection:Shop, product: List<Product>): ParticipateViewModelFactory {
    val repository = (requireContext().applicationContext as MyApplication).repository
    return ParticipateViewModelFactory(repository, collection,product)
}

fun Fragment.getVmFactory(shopId: String): ShopViewModelFactory {
    val repository = (requireContext().applicationContext as MyApplication).repository
    return ShopViewModelFactory(repository, shopId)
}

    fun Fragment.getVmFactory(request: Request?): HostViewModelFactory {
        val repository = (requireContext().applicationContext as MyApplication).repository
        return HostViewModelFactory(repository, request)
}


fun Fragment.getVmFactory(orderDetail: MyOrderDetailKey): OrderDetailViewModelFactory {
    val repository = (requireContext().applicationContext as MyApplication).repository
    return OrderDetailViewModelFactory(repository, orderDetail)
}

fun Fragment.getVmFactory(myRequestType: MyRequestType): MyRequestViewModelFactory {
    val repository = (requireContext().applicationContext as MyApplication).repository
    return MyRequestViewModelFactory(repository, myRequestType)
}

fun Fragment.getVmFactory(myOrderType: MyOrderType): MyOrderViewModelFactory {
    val repository = (requireContext().applicationContext as MyApplication).repository
    return MyOrderViewModelFactory(repository, myOrderType)
}

fun Fragment.getVmFactory(myHostType: MyHostType): MyHostViewModelFactory {
    val repository = (requireContext().applicationContext as MyApplication).repository
    return MyHostViewModelFactory(repository, myHostType)
}



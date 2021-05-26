package com.chloe.shopshare.ext

import android.icu.text.SimpleDateFormat
import androidx.fragment.app.Fragment
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.Product
import com.chloe.shopshare.factory.*
import java.util.*

/**
 *
 * Extension functions for Fragment.
 */
fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as MyApplication).repository
    return ViewModelFactory(repository)
}

fun Fragment.getVmFactory(id: String): IdViewModelFactory {
    val repository = (requireContext().applicationContext as MyApplication).repository
    return IdViewModelFactory(repository,id)
}

fun Fragment.getVmFactory(option: List<String>?,isStandard:Boolean): OptionViewModelFactory {
    val repository = (requireContext().applicationContext as MyApplication).repository
    return OptionViewModelFactory(repository, option, isStandard)
}

fun Fragment.getVmFactory(collection:Shop, product: List<Product>): ParticipateViewModelFactory {
    val repository = (requireContext().applicationContext as MyApplication).repository
    return ParticipateViewModelFactory(repository, collection,product)
}

fun Fragment.getVmFactory(shop:Shop): ShopViewModelFactory {
    val repository = (requireContext().applicationContext as MyApplication).repository
    return ShopViewModelFactory(repository, shop)
}



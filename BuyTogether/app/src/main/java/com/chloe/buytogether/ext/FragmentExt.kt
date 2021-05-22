package com.chloe.buytogether.ext

import android.icu.text.SimpleDateFormat
import androidx.fragment.app.Fragment
import com.chloe.buytogether.MyApplication
import com.chloe.buytogether.data.Collections
import com.chloe.buytogether.data.Product
import com.chloe.buytogether.detail.OptionSelector
import com.chloe.buytogether.factory.CollectViewModelFactory
import com.chloe.buytogether.factory.OptionViewModelFactory
import com.chloe.buytogether.factory.ParticipateViewModelFactory
import com.chloe.buytogether.factory.ViewModelFactory
import java.util.*

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

fun Fragment.getVmFactory(collection:Collections,product: List<Product>): ParticipateViewModelFactory {
    val repository = (requireContext().applicationContext as MyApplication).repository
    return ParticipateViewModelFactory(repository, collection,product)
}

fun Fragment.getVmFactory(collection:Collections): CollectViewModelFactory {
    val repository = (requireContext().applicationContext as MyApplication).repository
    return CollectViewModelFactory(repository, collection)
}


fun Long.toDisplayFormat(): String {
    return SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN).format(this)
}

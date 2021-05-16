package com.chloe.buytogether.ext

import android.icu.text.SimpleDateFormat
import androidx.fragment.app.Fragment
import com.chloe.buytogether.MyApplication
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

fun Long.toDisplayFormat(): String {
    return SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN).format(this)
}

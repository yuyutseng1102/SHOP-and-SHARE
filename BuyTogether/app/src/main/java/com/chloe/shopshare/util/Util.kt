package com.chloe.shopshare.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.chloe.shopshare.MyApplication

object Util {

    fun isInternetConnected(): Boolean {
        val cm = MyApplication.instance
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    fun getString(resourceId: Int): String {
        return MyApplication.instance.getString(resourceId)
    }

    fun getColor(resourceId: Int): Int {
        return MyApplication.instance.getColor(resourceId)
    }
}
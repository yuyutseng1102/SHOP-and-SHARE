package com.chloe.shopshare

import android.app.Application
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.util.ServiceLocator
import kotlin.properties.Delegates

class MyApplication: Application() {

    val repository: Repository
        get() = ServiceLocator.provideRepository(this)

    companion object {
        var instance: MyApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}

package com.chloe.buytogether

import android.app.Application
import com.chloe.buytogether.data.source.Repository
import com.chloe.buytogether.util.ServiceLocator
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

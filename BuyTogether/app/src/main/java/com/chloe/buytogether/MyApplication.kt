package com.chloe.buytogether

import android.app.Application
import kotlin.properties.Delegates

class MyApplication: Application() {

    // Depends on the flavor,
//    val stylishRepository: StylishRepository
//        get() = ServiceLocator.provideTasksRepository(this)

    companion object {
        var instance: MyApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}

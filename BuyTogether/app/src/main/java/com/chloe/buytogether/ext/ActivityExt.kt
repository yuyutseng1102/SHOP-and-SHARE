package com.chloe.buytogether.ext

import android.app.Activity
import android.view.Gravity
import android.widget.Toast
import com.chloe.buytogether.MyApplication
import com.chloe.buytogether.factory.ViewModelFactory

fun Activity.getVmFactory(): ViewModelFactory {
    val repository = (applicationContext as MyApplication).repository
    return ViewModelFactory(repository)
}

fun Activity?.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.CENTER, 0, 0)
        show()
    }
}
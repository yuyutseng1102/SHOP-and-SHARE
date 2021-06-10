package com.chloe.shopshare.util

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.data.User

object UserManager {

    private const val USER_DATA = "user_data"
    private const val USER_ID = "user_id"

    private val _user = MutableLiveData<User?>()

    val user: LiveData<User?>
        get() = _user

    var userId: String? = null
        get() = MyApplication.instance
            .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
            .getString(USER_ID, null)
        set(value) {
            field = when (value) {
                null -> {
                    MyApplication.instance
                        .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                        .remove(USER_ID)
                        .apply()
                    null
                }
                else -> {
                    MyApplication.instance
                        .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                        .putString(USER_ID, value)
                        .apply()
                    value
                }
            }
        }

    /**
     * It can be use to check login status directly
     */
    val isLoggedIn: Boolean
        get() = userId != null

    /**
     * Clear the [userId] and the [user]/[_user] data
     */
    fun clear() {
        userId = null
        _user.value = null
    }


}


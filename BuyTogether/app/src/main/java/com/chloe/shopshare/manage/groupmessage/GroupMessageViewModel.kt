package com.chloe.shopshare.manage.groupmessage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.Notify
import com.chloe.shopshare.data.Result
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class GroupMessageViewModel(private val repository: Repository): ViewModel() {


    val messageContent = MutableLiveData<String>()
    val isClickable = MutableLiveData<Boolean>()
    val shopStatus = MutableLiveData<Int>()


    init {
        isClickable.value = false
    }

    fun checkContent(){
        isClickable.value = !messageContent.value.isNullOrEmpty()
        Log.d("Chloe","messageContent = ${messageContent.value}")
        Log.d("Chloe","isClickable = ${isClickable.value}")
    }

}
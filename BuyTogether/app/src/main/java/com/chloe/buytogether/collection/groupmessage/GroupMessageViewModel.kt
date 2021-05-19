package com.chloe.buytogether.collection.groupmessage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.buytogether.data.source.Repository

class GroupMessageViewModel(private val repository: Repository): ViewModel() {
    val messageContent = MutableLiveData<String>()

    val isClickable = MutableLiveData<Boolean>()
    val status = MutableLiveData<Int>()

    init {

        isClickable.value = false
    }

    fun checkContent(){
        isClickable.value = !messageContent.value.isNullOrEmpty()
        Log.d("Chloe","messageContent = ${messageContent.value}")
        Log.d("Chloe","isClickable = ${isClickable.value}")
    }
}
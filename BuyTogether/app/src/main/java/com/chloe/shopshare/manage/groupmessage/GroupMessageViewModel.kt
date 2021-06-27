package com.chloe.shopshare.manage.groupmessage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GroupMessageViewModel : ViewModel() {


    val messageContent = MutableLiveData<String>()
    val isClickable = MutableLiveData<Boolean>()
    val shopStatus = MutableLiveData<Int>()


    init {
        isClickable.value = false
    }

    fun checkContent() {
        isClickable.value = !messageContent.value.isNullOrEmpty()
    }

}
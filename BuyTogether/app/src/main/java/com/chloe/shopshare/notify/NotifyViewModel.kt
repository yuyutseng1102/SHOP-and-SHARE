package com.chloe.shopshare.notify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.data.Notify
import com.chloe.shopshare.data.source.Repository
import java.util.*

class NotifyViewModel(private val repository: Repository) : ViewModel() {

    private val _notify = MutableLiveData<List<Notify>>()
    val notify: LiveData<List<Notify>>
        get() = _notify

    val isChecked = MutableLiveData<Boolean>()

    //mock data
    val notifyId = "55556666"
    val id = "1038573"
    val notifyTime: Long = Calendar.getInstance().timeInMillis
    val notifyType: Int = 10
    val content: String = "預計匯款時間：5/20 24：00前，錢收滿後馬上開通"

    fun addMockData(){
        val mockList : MutableList<Notify> = mutableListOf()
        mockList.add(Notify(notifyId,id,notifyTime,notifyType,content))
        mockList.add(Notify(notifyId,id,notifyTime,notifyType,content))
        mockList.add(Notify(notifyId,id,notifyTime,notifyType,content))
        mockList.add(Notify(notifyId,id,notifyTime,notifyType,content))
        mockList.add(Notify(notifyId,id,notifyTime,notifyType,content))
        _notify.value = mockList
    }

}

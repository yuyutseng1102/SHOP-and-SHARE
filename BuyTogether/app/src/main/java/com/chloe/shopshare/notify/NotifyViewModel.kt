package com.chloe.shopshare.notify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.data.Notify
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.network.LoadApiStatus
import com.chloe.shopshare.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*

class NotifyViewModel(private val repository: Repository) : ViewModel() {

    private var _notify = MutableLiveData<List<Notify>>()
    val notify: LiveData<List<Notify>>
        get() = _notify

    private val _defaultContent = MutableLiveData<String>()
    val defaultContent: LiveData<String>
        get() = _defaultContent

    val isChecked = MutableLiveData<Boolean>()

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // status for the loading icon of swl
    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        val userId = UserManager.userId
        userId?.let {
            getLiveNotify(it)
        }
    }



    private fun getLiveNotify(userId: String) {
        _notify = repository.getLiveNotify(userId)
        _status.value = LoadApiStatus.DONE
        _refreshStatus.value = false
    }




//    //mock data
//    val notifyId = "55556666"
//    val shopId = "1038573"
//    val orderId = "1038573"
//    val notifyTime: Long = Calendar.getInstance().timeInMillis
//    val notifyType: Int = 10
//    val content: String = "預計匯款時間：5/20 24：00前，錢收滿後馬上開通"
//
//    fun addMockData(){
//        val mockList : MutableList<Notify> = mutableListOf()
//        mockList.add(Notify(notifyId,shopId,orderId,notifyTime,notifyType,content))
//        mockList.add(Notify(notifyId,shopId,orderId,notifyTime,notifyType,content))
//        mockList.add(Notify(notifyId,shopId,orderId,notifyTime,notifyType,content))
//        mockList.add(Notify(notifyId,shopId,orderId,notifyTime,notifyType,content))
//        mockList.add(Notify(notifyId,shopId,orderId,notifyTime,notifyType,content))
//        _notify.value = mockList
//    }

}

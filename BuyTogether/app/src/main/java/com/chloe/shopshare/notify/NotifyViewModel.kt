package com.chloe.shopshare.notify

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
import com.chloe.shopshare.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class NotifyViewModel(private val repository: Repository) : ViewModel() {

    private val _notify = MutableLiveData<List<Notify>>()
    val notify: LiveData<List<Notify>>
        get() = _notify

    private val _getNotifyDone = MutableLiveData<Boolean>()
    val getNotifyDone: LiveData<Boolean>
        get() = _getNotifyDone

    val isChecked = MutableLiveData<Boolean>()
    lateinit var userId : String

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

    private val _visible = MutableLiveData<Boolean>()
    val visible: LiveData<Boolean>
        get() = _visible

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        UserManager.userId?.let {
            userId = it
        }
        getMyNotify(userId)

    }

    private fun getMyNotify(userId : String) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.getMyNotify(userId)
            _notify.value = when (result) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                else -> {
                    _error.value = MyApplication.instance.getString(R.string.result_fail)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
            if (_notify.value.isNullOrEmpty()){
                _visible.value = true
            }else{
                _visible.value = null
            }
            _getNotifyDone.value = true
            _refreshStatus.value = false
        }
    }

    fun updateNotifyChecked(userId : String, notifyList: List<Notify>) {

        val notifyUnchecked = mutableListOf<Notify>()
        for (totalNotify in notifyList){
            if (!totalNotify.isChecked){
                notifyUnchecked.add(totalNotify)
            }
        }

        val totalCount = notifyUnchecked.size
        var count = 0

        for (notify in notifyUnchecked) {

            coroutineScope.launch {
                _status.value = LoadApiStatus.LOADING
                val result = repository.updateNotifyChecked(userId, notify.id)
                when (result) {
                    is Result.Success -> {
                        _error.value = null
                        _status.value = LoadApiStatus.DONE
                        Log.d("Notify", "updateNotifyChecked is Success")
                        count++
                        result.data
                    }
                    is Result.Fail -> {
                        _error.value = result.error
                        _status.value = LoadApiStatus.ERROR
                        count++
                        null
                    }
                    is Result.Error -> {
                        _error.value = result.exception.toString()
                        _status.value = LoadApiStatus.ERROR
                        count++
                        null
                    }
                    else -> {
                        _error.value = MyApplication.instance.getString(R.string.result_fail)
                        _status.value = LoadApiStatus.ERROR
                        count++
                        null
                    }
                }
                if (count == totalCount){
                    _refreshStatus.value = false
                }
            }
        }
    }

    fun deleteNotify(userId : String, notify: Notify) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.deleteNotify(userId, notify)
            when (result) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                    refresh()
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                else -> {
                    _error.value = MyApplication.instance.getString(R.string.result_fail)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }

            _refreshStatus.value = false
        }
    }

    fun onGetNotifyDone(){
        _getNotifyDone.value = null
    }

    fun refresh() {
        if (status.value != LoadApiStatus.LOADING) {
            getMyNotify(userId)
        }
    }



//
//    private fun getLiveNotify(userId: String) {
//        _notify = repository.getLiveNotify(userId)
//        _status.value = LoadApiStatus.DONE
//        _refreshStatus.value = false
//    }
//


}

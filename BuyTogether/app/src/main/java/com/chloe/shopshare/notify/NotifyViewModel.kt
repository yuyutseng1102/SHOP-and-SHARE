package com.chloe.shopshare.notify

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

class NotifyViewModel(private val repository: Repository) : ViewModel() {

    private val _notify = MutableLiveData<List<Notify>>()
    val notify: LiveData<List<Notify>>
        get() = _notify

    private val _getNotifyDone = MutableLiveData<Boolean>()
    val getNotifyDone: LiveData<Boolean>
        get() = _getNotifyDone

    val isChecked = MutableLiveData<Boolean>()
    lateinit var userId: String

    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    private val _refreshStatus = MutableLiveData<Boolean>()
    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    private val _visible = MutableLiveData<Boolean>()
    val visible: LiveData<Boolean>
        get() = _visible

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        UserManager.userId?.let {
            userId = it
            getMyNotify(userId)
        }
    }

    private fun getMyNotify(userId: String) {

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
            _visible.value =
                when (_notify.value.isNullOrEmpty()) {
                    true -> true
                    else -> null
                }
            _getNotifyDone.value = true
            _refreshStatus.value = false
        }
    }

    fun updateNotifyChecked(userId: String, notifyList: List<Notify>) {

        val notifyUnchecked = mutableListOf<Notify>()
        for (totalNotify in notifyList) {
            if (!totalNotify.isChecked) {
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
                        count++
                    }
                    is Result.Fail -> {
                        _error.value = result.error
                        _status.value = LoadApiStatus.ERROR
                        count++
                    }
                    is Result.Error -> {
                        _error.value = result.exception.toString()
                        _status.value = LoadApiStatus.ERROR
                        count++
                    }
                    else -> {
                        _error.value = MyApplication.instance.getString(R.string.result_fail)
                        _status.value = LoadApiStatus.ERROR
                        count++
                    }
                }
                if (count == totalCount) {
                    _refreshStatus.value = false
                }
            }
        }
    }

    fun deleteNotify(userId: String, notify: Notify) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            when (val result = repository.deleteNotify(userId, notify)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    refresh()
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _error.value = MyApplication.instance.getString(R.string.result_fail)
                    _status.value = LoadApiStatus.ERROR
                }
            }
            _refreshStatus.value = false
        }
    }

    fun onGetNotifyDone() {
        _getNotifyDone.value = null
    }

    fun refresh() {
        if (status.value != LoadApiStatus.LOADING) {
            getMyNotify(userId)
        }
    }

}

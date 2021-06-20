package com.chloe.shopshare.myrequest.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.Request
import com.chloe.shopshare.data.Result
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.myrequest.MyRequestType
import com.chloe.shopshare.network.LoadApiStatus
import com.chloe.shopshare.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyRequestListViewModel(

    private val repository: Repository,
    private val myRequestType: MyRequestType

) : ViewModel() {
    private val _request = MutableLiveData<List<Request>>()
    val request: LiveData<List<Request>>
        get() = _request

    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    private val _isRequestEmpty = MutableLiveData<Boolean>()
    val isRequestEmpty: LiveData<Boolean>
        get() = _isRequestEmpty

    private val _refreshStatus = MutableLiveData<Boolean>()
    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    private val _navigateToDetail = MutableLiveData<String?>()
    val navigateToDetail: LiveData<String?>
        get() = _navigateToDetail

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    lateinit var userId: String

    init {
        _isRequestEmpty.value = false
        UserManager.userId?.let {
            userId = it
            getRequest(myRequestType)
        }
    }

    fun navigateToDetail(request: Request) {
        _navigateToDetail.value = request.id
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }


    private fun getRequest(type: MyRequestType) {
        when (type) {
            MyRequestType.ALL_REQUEST -> getAllRequests(userId)
            MyRequestType.ONGOING_REQUEST -> getOngoingRequests(userId)
            MyRequestType.FINISHED_REQUEST -> getFinishedRequests(userId)
        }
    }


    private fun getAllRequests(userId: String) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.getMyRequest(userId)
            _request.value = when (result) {
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
            _isRequestEmpty.value = _request.value.isNullOrEmpty()
            _refreshStatus.value = false
        }
    }

    private fun getOngoingRequests(userId: String) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.getMyOngoingRequest(userId)
            _request.value = when (result) {
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
            _isRequestEmpty.value = _request.value.isNullOrEmpty()
            _refreshStatus.value = false
        }
    }

    private fun getFinishedRequests(userId: String) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.getMyFinishedRequest(userId)
            _request.value = when (result) {
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
            _isRequestEmpty.value = _request.value.isNullOrEmpty()
            _refreshStatus.value = false
        }
    }

    fun refresh() {
        if (status.value != LoadApiStatus.LOADING) {
            getRequest(myRequestType)
        }
    }


}
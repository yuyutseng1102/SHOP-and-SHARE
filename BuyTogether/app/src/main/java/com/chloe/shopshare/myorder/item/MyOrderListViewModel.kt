package com.chloe.shopshare.myorder.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.*
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.myorder.MyOrderType
import com.chloe.shopshare.network.LoadApiStatus
import com.chloe.shopshare.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyOrderListViewModel(private val repository: Repository,private val myOrderType: MyOrderType): ViewModel() {

    private var _shop = MutableLiveData<Shop>()
    val shop: LiveData<Shop>
        get() = _shop

    private val _order = MutableLiveData<List<Order>>()
    val order: LiveData<List<Order>>
        get() = _order

    private val _detailList = MutableLiveData<List<MyOrder>>()
    val detailList: LiveData<List<MyOrder>>
        get() = _detailList

    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    private val _visible = MutableLiveData<Boolean>()
    val visible: LiveData<Boolean>
        get() = _visible

    private val _refreshStatus = MutableLiveData<Boolean>()
    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    private val _navigateToOrderDetail = MutableLiveData<Track?>()
    val navigateToOrderDetail: LiveData<Track?>
        get() = _navigateToOrderDetail

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    lateinit var userId : String

    init {
        _visible.value = false
        UserManager.userId?.let {
            userId = it
            getOrders(myOrderType)
        }
    }

    private fun getOrders(type: MyOrderType) {
        getMyOrder(userId, type.status)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun navigateToOrderDetail(key: Track) {
        _navigateToOrderDetail.value = key
    }

    fun onOrderDetailNavigated() {
        _navigateToOrderDetail.value = null
    }

    private fun getMyOrder(userId : String, status:List<Int>) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.getMyOrder(userId,status)

            _detailList.value =
            when (result) {
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
            _visible.value = _detailList.value.isNullOrEmpty()
            _refreshStatus.value = false
        }
    }


    fun refresh() {
        if (status.value != LoadApiStatus.LOADING) {
            getOrders(myOrderType)
        }
    }
}
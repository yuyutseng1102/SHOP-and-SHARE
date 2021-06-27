package com.chloe.shopshare.track

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.Order
import com.chloe.shopshare.data.Result
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.Track
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TrackViewModel(private val repository: Repository, private val args: Track) :
    ViewModel() {

    private val _shopId = MutableLiveData<String>().apply {
        value = args.shopId
    }
    val shopId: LiveData<String>
        get() = _shopId

    private val _orderId = MutableLiveData<String>().apply {
        value = args.orderId
    }
    val orderId: LiveData<String>
        get() = _orderId

    private var _shop = MutableLiveData<Shop>()
    val shop: LiveData<Shop>
        get() = _shop

    private val _order = MutableLiveData<Order>()
    val order: LiveData<Order>
        get() = _order

    private val _navigateToDetail = MutableLiveData<String?>()
    val navigateToDetail: LiveData<String?>
        get() = _navigateToDetail

    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        _shopId.value?.let { shop ->
            getShop(shop)
            _orderId.value?.let { order ->
                getOrder(shop, order)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun navigateToDetail(shopId: String) {
        _navigateToDetail.value = shopId
    }

    fun onDetailNavigate() {
        _navigateToDetail.value = null
    }

    private fun getShop(id: String) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.getDetailShop(id)

            _shop.value = when (result) {
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
        }
    }

    private fun getOrder(shopId: String, orderId: String) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.getDetailOrder(shopId, orderId)

            _order.value = when (result) {
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
        }
    }

}
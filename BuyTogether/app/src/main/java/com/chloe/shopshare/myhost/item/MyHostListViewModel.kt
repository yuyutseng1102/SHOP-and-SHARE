package com.chloe.shopshare.myhost.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.Result
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.myhost.MyHostType
import com.chloe.shopshare.network.LoadApiStatus
import com.chloe.shopshare.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyHostListViewModel(private val repository: Repository, private val myHostType: MyHostType) :
    ViewModel() {

    private val _shop = MutableLiveData<List<Shop>>()
    val shop: LiveData<List<Shop>>
        get() = _shop

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

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    lateinit var userId: String

    init {
        _visible.value = false
        UserManager.userId?.let {
            userId = it
            getShops(myHostType)
        }
    }

//    private fun getShop() {
//        when (myHostType) {
//            MyHostType.ALL_SHOP -> getMyShop(userId)
//            MyHostType.OPENING_SHOP -> getMyShopByStatus(userId, MyHostType.OPENING_SHOP.status)
//            MyHostType.PROCESS_SHOP -> getMyShopByStatus(userId, MyHostType.PROCESS_SHOP.status)
//            MyHostType.SHIPMENT_SHOP -> getMyShopByStatus(userId, MyHostType.SHIPMENT_SHOP.status)
//            MyHostType.FINISH_SHOP -> getMyShopByStatus(userId, MyHostType.FINISH_SHOP.status)
//        }
//    }

    private fun getShops(myHostType: MyHostType) {
        when (val type = MyHostType.values()[myHostType.position]) {
            MyHostType.ALL_SHOP -> getAllShops(userId)
            else -> getShopsByStatus(userId, type.status)
        }
    }

    private val _navigateToManage = MutableLiveData<String?>()
    val navigateToManage: LiveData<String?>
        get() = _navigateToManage

    fun navigateToManage(shopId: String) {
        _navigateToManage.value = shopId
    }

    fun onManageNavigated() {
        _navigateToManage.value = null
    }

    private fun getAllShops(userId: String) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.getMyShop(userId)
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
            _visible.value = _shop.value.isNullOrEmpty()
            _refreshStatus.value = false
        }
    }

    private fun getShopsByStatus(userId: String, status: List<Int>) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.getMyShopByStatus(userId, status)
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
            _visible.value = _shop.value.isNullOrEmpty()
            _refreshStatus.value = false
        }
    }


    fun refresh() {
        if (status.value != LoadApiStatus.LOADING) {
            getShops(myHostType)
        }
    }
}
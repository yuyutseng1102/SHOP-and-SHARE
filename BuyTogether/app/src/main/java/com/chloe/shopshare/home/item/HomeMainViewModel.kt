package com.chloe.shopshare.home.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.Result
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.host.CategoryType
import com.chloe.shopshare.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeMainViewModel(private val repository: Repository) :ViewModel() {

    private val _shopLinearTop = MutableLiveData<List<Shop>>()
    val shopLinearTop: LiveData<List<Shop>>
        get() = _shopLinearTop

    private val _shopLinearBottom = MutableLiveData<List<Shop>>()
    val shopLinearBottom: LiveData<List<Shop>>
        get() = _shopLinearBottom

    private val _shopGrid = MutableLiveData<List<Shop>>()
    val shopGrid: LiveData<List<Shop>>
        get() = _shopGrid

    private val _navigateToDetail = MutableLiveData<String?>()

    val navigateToDetail: LiveData<String?>
        get() = _navigateToDetail


    fun navigateToDetail(shop:Shop){
        _navigateToDetail.value = shop.id
    }

    fun onDetailNavigated(){
        _navigateToDetail.value = null
    }

    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    private val _refreshStatus = MutableLiveData<Boolean>()
    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    val categoryTypeToDisplayTop = CategoryType.WOMAN
    val categoryTypeToDisplayBottom = CategoryType.FOOD

    init {
        getHotShopByTypeTop(categoryTypeToDisplayTop.category)
        getHotShopByTypeBottom(categoryTypeToDisplayBottom.category)
        getNewShop()
    }

    private fun getHotShopByTypeTop(category: Int) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getHotShopByType(category)

            _shopLinearTop.value =
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
            _refreshStatus.value = false
        }
    }

    private fun getHotShopByTypeBottom(category: Int) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getHotShopByType(category)

            _shopLinearBottom.value =
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
            _refreshStatus.value = false
        }
    }


    private fun getNewShop() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getNewShop()

            _shopGrid.value = when (result) {
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
            _refreshStatus.value = false
        }
    }

    fun refresh() {
        if (status.value != LoadApiStatus.LOADING) {
            getHotShopByTypeTop(categoryTypeToDisplayTop.category)
            getHotShopByTypeBottom(categoryTypeToDisplayBottom.category)
            getNewShop()
        }
    }

}

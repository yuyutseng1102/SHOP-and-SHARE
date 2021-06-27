package com.chloe.shopshare.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.Result
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.network.LoadApiStatus
import com.chloe.shopshare.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ResultViewModel(
    private val repository: Repository,
    private val categoryArgs: Int,
    private val countryArgs: Int
) : ViewModel() {

    private val _category = MutableLiveData<Int>().apply {
        value = categoryArgs
    }
    val category: LiveData<Int>
        get() = _category

    private val _country = MutableLiveData<Int>().apply {
        value = countryArgs
    }
    val country: LiveData<Int>
        get() = _country

    private val _shop = MutableLiveData<List<Shop>>()
    val shop: LiveData<List<Shop>>
        get() = _shop

    private val _orderSize = MutableLiveData<Int>()
    val orderSize: LiveData<Int>
        get() = _orderSize

    private val _shopLikedList = MutableLiveData<List<String>>()
    val shopLikedList: LiveData<List<String>>
        get() = _shopLikedList

    private val _successGetLikeList = MutableLiveData<Boolean?>()
    val successGetLikeList: LiveData<Boolean?>
        get() = _successGetLikeList

    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    private val _refreshProfile = MutableLiveData<Boolean?>()
    val refreshProfile: LiveData<Boolean?>
        get() = _refreshProfile

    private val _refreshStatus = MutableLiveData<Boolean>()
    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _navigateToDetail = MutableLiveData<String?>()
    val navigateToDetail: LiveData<String?>
        get() = _navigateToDetail

    fun navigateToDetail(shop: Shop) {
        _navigateToDetail.value = shop.id
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }

    lateinit var userId: String

    init {
        UserManager.userId?.let {
            userId = it
        }
        filterToShop()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun filterToShop() {
        _category.value?.let { category ->
            _country.value?.let { country ->
                when {
                    category == 0 -> getShopByCountry(country)
                    country == 0 -> getShopByCategory(category)
                    category != 0 && country != 0 -> getShopByCategoryAndCountry(category, country)
                }
            }
        }
    }

    fun getLikeList() {
        getUserProfile(userId)
    }

    fun onLikeListGet() {
        _successGetLikeList.value = null
    }


    private fun getShopByCategory(category: Int) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getShopByCategory(category)

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
            _refreshStatus.value = false
        }
    }

    private fun getShopByCountry(country: Int) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.getShopByCountry(country)
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
            _refreshStatus.value = false
        }
    }

    private fun getShopByCategoryAndCountry(category: Int, country: Int) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.getShopByCategoryAndCountry(category, country)
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
            _refreshStatus.value = false
        }
    }

    private fun getUserProfile(userId: String) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.getUserProfile(userId)
            _shopLikedList.value = when (result) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    _successGetLikeList.value = true
                    result.data.like
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    _successGetLikeList.value = null
                    null
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    _successGetLikeList.value = null
                    null
                }
                else -> {
                    _error.value = MyApplication.instance.getString(R.string.result_fail)
                    _status.value = LoadApiStatus.ERROR
                    _successGetLikeList.value = null
                    null
                }
            }
            _refreshStatus.value = false
        }
    }


    fun addShopLiked(userId: String, shopId: String) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            when (val result = repository.addShopLiked(userId, shopId)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    _refreshProfile.value = true
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    _refreshProfile.value = null
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    _refreshProfile.value = null
                }
                else -> {
                    _error.value = MyApplication.instance.getString(R.string.result_fail)
                    _status.value = LoadApiStatus.ERROR
                    _refreshProfile.value = null
                }
            }
        }
    }

    fun removeShopLiked(userId: String, shopId: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.removeShopLiked(userId, shopId)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                    _refreshProfile.value = true
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    _refreshProfile.value = null
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    _refreshProfile.value = null
                }
                else -> {
                    _error.value = MyApplication.instance.getString(R.string.result_fail)
                    _status.value = LoadApiStatus.ERROR
                    _refreshProfile.value = null
                }
            }
            _refreshProfile.value = false
        }
    }


    fun refresh() {
        if (status.value != LoadApiStatus.LOADING) {
            filterToShop()
        }
    }

    fun refreshProfile() {
        getUserProfile(userId)
        _refreshProfile.value = null
    }

}

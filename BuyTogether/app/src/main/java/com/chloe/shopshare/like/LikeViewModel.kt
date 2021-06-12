package com.chloe.shopshare.like

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.Request
import com.chloe.shopshare.data.Result
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.network.LoadApiStatus
import com.chloe.shopshare.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LikeViewModel(private val repository: Repository): ViewModel() {

    private val _shop = MutableLiveData<List<Shop>>()
    val shop: LiveData<List<Shop>>
        get() = _shop

    private val _likeList = MutableLiveData<List<String>>()
    val likeList: LiveData<List<String>>
        get() = _likeList

    private val _isListNotEmpty = MutableLiveData<Boolean>()
    val isListNotEmpty: LiveData<Boolean>
        get() = _isListNotEmpty

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String?>()

    val error: LiveData<String?>
        get() = _error


    private val _successGetShop = MutableLiveData<Boolean?>()
    val successGetShop: LiveData<Boolean?>
        get() = _successGetShop

    private val _navigateToDetail = MutableLiveData<String>()

    val navigateToDetail: LiveData<String>
        get() = _navigateToDetail


    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    lateinit var userId : String

    init {
        Log.d("LikeTag","UserManager.userId = ${UserManager.userId}")
        UserManager.userId?.let {
            userId = it
            getLikeList(userId)
        }
    }


    fun navigateToDetail(shop: Shop){
        _navigateToDetail.value = shop.id
    }

    fun onDetailNavigated(){
        _navigateToDetail.value = null
    }


    fun getShopLikedDetail(){
        Log.d("LikeTag","getShopLiked = ${_likeList.value}")

        getShopLiked(_likeList.value?: listOf())
    }


    fun onShopLikedDetailGet(){
        _successGetShop.value = null
    }

    private fun getLikeList(userId: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getUserProfile(userId)

            _likeList.value = when (result) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data.like
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

            _isListNotEmpty.value = !_likeList.value.isNullOrEmpty()
        }

    }

    private fun getShopLiked(shopIdList : List<String>) {
        Log.d("LikeTag","shopIdList = ${shopIdList}")
        val totalCount = shopIdList.size
        var count = 0
        val shopList = mutableListOf<Shop>()

        var shop : Shop?

        for(shopId in shopIdList) {
            Log.d("LikeTag","shopId = ${shopId}")
            coroutineScope.launch {
                _status.value = LoadApiStatus.LOADING
                val result = repository.getDetailShop(shopId)
                shop = when (result) {
                    is Result.Success -> {
                        _error.value = null
                        _status.value = LoadApiStatus.DONE
                        count++
                        Log.d("LikeTag","inside count  = ${count}")
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
                Log.d("LikeTag","count  = ${count}")
                shop?.let {
                    shopList.add(it)
                }

                if (count == totalCount){
                    _shop.value = shopList
                    Log.d("LikeTag", "count == totalCount , shop = ${_shop.value}")
                    _successGetShop.value = true
                }

            }
        }
    }


    }
//    fun getShopLikedDetail(){
//        Log.d("LikeTag","getShopLiked = ${_likeList.value}")
//
//        getShopLiked(_likeList.value?: listOf())
//    }

//    private fun getShopLiked(shopIdList : List<String>) {
//        Log.d("LikeTag","shopIdList = ${shopIdList}")
//        val totalCount = shopIdList.size
//        var count = 0
//        val shopList = mutableListOf<Shop>()
//
//        var shop : Shop?
//
//        for(shopId in shopIdList) {
//            Log.d("LikeTag","shopId = ${shopId}")
//            coroutineScope.launch {
//                _status.value = LoadApiStatus.LOADING
//                val result = repository.getDetailShop(shopId)
//                shop = when (result) {
//                    is Result.Success -> {
//                        _error.value = null
//                        _status.value = LoadApiStatus.DONE
//                        count++
//                        Log.d("LikeTag","inside count  = ${count}")
//                        result.data
//                    }
//                    is Result.Fail -> {
//                        _error.value = result.error
//                        _status.value = LoadApiStatus.ERROR
//                        count++
//                        null
//                    }
//                    is Result.Error -> {
//                        _error.value = result.exception.toString()
//                        _status.value = LoadApiStatus.ERROR
//                        count++
//                        null
//                    }
//                    else -> {
//                        _error.value = MyApplication.instance.getString(R.string.result_fail)
//                        _status.value = LoadApiStatus.ERROR
//                        count++
//                        null
//                    }
//                }
//                Log.d("LikeTag","count  = ${count}")
//                shop?.let {
//                    shopList.add(it)
//                }
//
//                if (count == totalCount){
//                    _shop.value = shopList
//                    Log.d("LikeTag", "count == totalCount , shop = ${_shop.value}")
//                    _successGetShop.value = true
//                }
//
//            }
//        }
//    }

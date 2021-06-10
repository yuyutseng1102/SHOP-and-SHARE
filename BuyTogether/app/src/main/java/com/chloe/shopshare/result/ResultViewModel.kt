package com.chloe.shopshare.result

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.Result
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.home.SortMethod
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
):ViewModel() {

    private val _category = MutableLiveData<Int>().apply {
        Log.d("Chloe", "_category = $categoryArgs")
        value = categoryArgs
    }
    val category: LiveData<Int>
        get() = _category

    private val _country = MutableLiveData<Int>().apply {
        Log.d("Chloe", "_country = $countryArgs")
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
    val shopLikedList : LiveData<List<String>>
        get() = _shopLikedList

    private val _successGetLikeList = MutableLiveData<Boolean?>()
    val successGetLikeList: LiveData<Boolean?>
        get() = _successGetLikeList


    val displayOpeningShop = MutableLiveData<Boolean>()

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // status for the loading icon of swl
    private val _refreshProfile = MutableLiveData<Boolean>()
    val refreshProfile: LiveData<Boolean>
        get() = _refreshProfile

    // status for the loading icon of swl
    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)



    private val _navigateToDetail = MutableLiveData<String>()

    val navigateToDetail: LiveData<String>
        get() = _navigateToDetail


    fun navigateToDetail(shop: Shop){
        _navigateToDetail.value = shop.id
    }

    fun onDetailNavigated(){
        _navigateToDetail.value = null
    }

    lateinit var userId : String

    init {
        Log.d("LikeTag","UserManager.userId = ${UserManager.userId}")
        UserManager.userId?.let {
            userId = it
        }

        filterToShop()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun filterToShop(){
        if (_category.value!=null && _country.value!=null) {
            if (_category.value == 0) {
                getShopByCountry(_country.value!!)
            } else if (_country.value == 0) {
                getShopByCategory(_category.value!!)
            } else if (_country.value != 0 && _category.value != 0) {
                getShopByCategoryAndCountry(_category.value!!, _country.value!!)
            }
        }
    }

    fun getLikeList(){
        getUserProfile(userId)
    }

    fun onLikeListGet(){
        _successGetLikeList.value = null
    }


    private fun getShopByCategory(category:Int) {

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

    private fun getShopByCountry(country:Int) {

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

    private fun getShopByCategoryAndCountry(category:Int,country:Int) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getShopByCategoryAndCountry(category,country)

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



    fun addShopLiked(userId:String, shopId:String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.addShopLiked(userId, shopId)

            when (result) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    _refreshProfile.value = true
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

    fun removeShopLiked(userId: String, shopId: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.removeShopLiked(userId , shopId)

            when (result) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                    _refreshProfile.value = true
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
            _refreshProfile.value = false
        }
    }


    fun refresh() {
        if (status.value != LoadApiStatus.LOADING) {
            filterToShop()
        }
    }

    fun refreshProfile(){
        getUserProfile(userId)
        _refreshProfile.value = null
    }




//    //排序方式
//    val selectedSortMethodPosition = MutableLiveData<Int>()
//    val sortMethod: LiveData<SortMethod> = Transformations.map(selectedSortMethodPosition) {
//        SortMethod.values()[it]
//    }


}

package com.chloe.shopshare.home.item


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

class HomeHostViewModel(private val repository: Repository) : ViewModel() {

    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>>
        get() = _shops

    private val _orderSize = MutableLiveData<Int>()
    val orderSize: LiveData<Int>
        get() = _orderSize

    private val _likes = MutableLiveData<List<String>>()
    val likes: LiveData<List<String>>
        get() = _likes

    private val _getLikesDone = MutableLiveData<Boolean?>()
    val getLikesDone: LiveData<Boolean?>
        get() = _getLikesDone

    val filterOpeningShops = MutableLiveData<Boolean?>()

    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _refreshProfile = MutableLiveData<Boolean>()
    val refreshProfile: LiveData<Boolean>
        get() = _refreshProfile

    private val _refreshStatus = MutableLiveData<Boolean>()
    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    private val _navigateToDetail = MutableLiveData<String>()
    val navigateToDetail: LiveData<String>
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
        filterOpeningShops.value = true
        getShops()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getLikes() {
        getProfile(userId)
    }

    fun onLikesGetDone() {
        _getLikesDone.value = null
    }

    fun getShops() {
        when (filterOpeningShops.value) {
            true -> getOpeningShops()
            else -> getAllShops()
        }
    }

    private fun getAllShops() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getAllShop()

            _shops.value = when (result) {
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

    private fun getOpeningShops() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getAllOpeningShop()

            _shops.value = when (result) {
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

    private fun getProfile(userId: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getUserProfile(userId)

            _likes.value = when (result) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    _getLikesDone.value = true
                    result.data.like
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    _getLikesDone.value = null
                    null
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    _getLikesDone.value = null
                    null
                }
                else -> {
                    _error.value = MyApplication.instance.getString(R.string.result_fail)
                    _status.value = LoadApiStatus.ERROR
                    _getLikesDone.value = null
                    null
                }
            }
            _refreshStatus.value = false
        }
    }


    fun likeShop(userId: String, shopId: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING
            val result = repository.addShopLiked(userId, shopId)

            _refreshProfile.value =
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
        }
    }

    fun dislikeShop(userId: String, shopId: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING
            val result = repository.removeShopLiked(userId, shopId)

            _refreshProfile.value =
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
        }
    }


    fun refresh() {
        if (status.value != LoadApiStatus.LOADING) {
            getShops()
        }
    }

    fun refreshProfile() {
        getProfile(userId)
        _refreshProfile.value = null
    }


    val selectedSortMethodPosition = MutableLiveData<Int>()
    val sortMethod: LiveData<SortMethod> = Transformations.map(selectedSortMethodPosition) {
        SortMethod.values()[it]
    }

}

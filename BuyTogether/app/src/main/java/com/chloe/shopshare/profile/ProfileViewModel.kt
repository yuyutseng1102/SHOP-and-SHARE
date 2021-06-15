package com.chloe.shopshare.profile

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.*
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.ext.isShopExpiredToday
import com.chloe.shopshare.login.LoginViewModel
import com.chloe.shopshare.network.LoadApiStatus
import com.chloe.shopshare.util.UserManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class ProfileViewModel(private val repository: Repository): ViewModel() {

    private val _user = MutableLiveData<User>()
    val user : LiveData<User>
        get() = _user

    private val _shop = MutableLiveData<List<Shop>>()
    val shop : LiveData<List<Shop>>
        get() = _shop

    private val _shopExpired = MutableLiveData<List<Shop>>()
    val shopExpired : LiveData<List<Shop>>
        get() = _shopExpired


    private val _detailList = MutableLiveData<List<MyOrder>>()
    val detailList: LiveData<List<MyOrder>>
        get() = _detailList


    private var _notify = MutableLiveData<List<Notify>>()
    val notify: LiveData<List<Notify>>
        get() = _notify




    // Handle leave login
    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _visible = MutableLiveData<Boolean>()

    val visible: LiveData<Boolean>
        get() = _visible
    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _noExpired = MutableLiveData<Boolean>()

    val noExpired: LiveData<Boolean>
        get() = _noExpired


    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String?>()

    val error: LiveData<String?>
        get() = _error

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    var auth= FirebaseAuth.getInstance()
    val todayDate = System.currentTimeMillis()

    init {
        if (_user.value == null) {
            UserManager.userId?.let {
                getUserProfile(it)
                getMyOpeningHost()
                getMyOpeningOrder()
                getLiveNewNotify(it)
                shopExpiredReminder(it,MyHostShortType.OPENING_SHOP.status)

            }
        }


    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    fun getMyOpeningHost(){
        UserManager.userId?.let {
            Log.d("Profile","init getMyOpeningHost")
            getMyShopByStatus(it,MyHostShortType.OPENING_SHOP.status)
        }
    }

    fun getMyOngoingHost(){
        UserManager.userId?.let {
            getMyShopByStatus(it,MyHostShortType.ONGOING_SHOP.status)
        }
    }

    fun getMyOpeningOrder(){
        UserManager.userId?.let {
            Log.d("Profile","init getMyOpeningHost")
            getMyOrder(it,MyOrderShortType.OPENING_ORDER.status)
        }
    }

    fun getMyOngoingOrder(){
        UserManager.userId?.let {
            getMyOrder(it,MyOrderShortType.ONGOING_ORDER.status)
        }
    }

    private val _navigateToManage = MutableLiveData<String>()
    val navigateToManage : LiveData<String>
        get() = _navigateToManage

    fun navigateToManage(shop: Shop){
        _navigateToManage.value = shop.id
    }

    fun onManageNavigate(){
        _navigateToManage.value = null
    }

    private val _navigateToOrderDetail = MutableLiveData<MyOrderDetailKey>()

    val navigateToOrderDetail: LiveData<MyOrderDetailKey>
        get() = _navigateToOrderDetail

    fun navigateToOrderDetail(key: MyOrderDetailKey) {
        _navigateToOrderDetail.value = key
    }

    fun onOrderDetailNavigated() {
        _navigateToOrderDetail.value = null
    }





    private fun getUserProfile(userId: String) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.getUserProfile(userId)
            // It will return Result object after Deferred flow
            _user.value =
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

    private fun shopExpiredReminder(userId : String, status: List<Int>) {

        var shopList: List<Shop>

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.getMyShopByStatus(userId,status)
            shopList = when (result) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    listOf()
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    listOf()
                }
                else -> {
                    _error.value = MyApplication.instance.getString(R.string.result_fail)
                    _status.value = LoadApiStatus.ERROR
                    listOf()
                }
            }
            val shopExpiredList = mutableListOf<Shop>()
            for (shop in shopList){
                shop.deadLine?.let {
                    if(it.isShopExpiredToday()){
                        shopExpiredList.add(shop)
                    }
                }
            }
            if (shopExpiredList.isNullOrEmpty()){
                Log.d("Profile","no shop expired")
                _noExpired.value = true
            }else{
                Log.d("Profile","shop expired today = ${shopExpiredList}")
                _noExpired.value = false
                _shopExpired.value = shopExpiredList
            }
        }
    }


    private fun getMyShopByStatus(userId : String, status: List<Int>) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.getMyShopByStatus(userId,status)
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
            Log.d("Shop","shop is ${_shop.value}")

            _visible.value = _shop.value.isNullOrEmpty()
            Log.d("Shop","visible is ${_visible.value}")
        }
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
        }
    }

    private fun getLiveNewNotify(userId: String) {
        _notify = repository.getLiveNewNotify(userId)
    }




    fun logout(){
        auth.signOut()
        Log.d("Login","currentUser = ${auth.currentUser}")
    }
}
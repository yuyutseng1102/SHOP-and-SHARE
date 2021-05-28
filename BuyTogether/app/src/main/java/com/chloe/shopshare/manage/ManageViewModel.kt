package com.chloe.shopshare.manage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.Notify
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.Order
import com.chloe.shopshare.data.Result
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.ext.toDisplayNotifyContent
import com.chloe.shopshare.ext.toDisplayNotifyMessage
import com.chloe.shopshare.network.LoadApiStatus
import com.chloe.shopshare.notify.NotifyType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ManageViewModel(
        private val repository: Repository,
        private val arguments: String
):ViewModel() {

    private val _shopId = MutableLiveData<String>().apply {
        Log.d("Chloe", "_shopId = $arguments")
        value = arguments
    }
    val shopId: LiveData<String>
        get() = _shopId

    private var _shop = MutableLiveData<Shop>()
    val shop: LiveData<Shop>
        get() = _shop

    private val _order = MutableLiveData<List<Order?>>()
    val order: LiveData<List<Order?>>
        get() = _order

    private val _member = MutableLiveData<Order?>()
    val member : LiveData<Order?>
        get() = _member

    private val _isChecked = MutableLiveData<Boolean>()
    val isChecked: LiveData<Boolean>
        get() = _isChecked

    val messageContent = MutableLiveData<String>()

    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // status for the loading icon of swl
    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    init {
        Log.i("Chloe", "Detail")

        _shopId.value?.let {
            Log.i("Order", "shopId = ${_shopId.value}")
            getDetailShop(it)
            getOrderOfShop(it)
            }
        }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun refresh() {
            if (status.value != LoadApiStatus.LOADING) {
                _shop.value?.let {
                    getDetailShop(it.id)
                    getOrderOfShop(it.id)
                }
            }
    }


    var checkedMemberPosition = MutableLiveData<Int?>()
    private val deleteList: MutableList<Order>? = mutableListOf()



    //打勾的是即將要刪除的團員
    fun checkAgain(member: Order, position: Int){
        checkedMemberPosition.value = position
        Log.d("checkChloe","selectMember=$member, position=$position")
        _member.value = member
        if (_member.value!=null) {
            if (_member.value!!.isCheck) {
                deleteList?.add(_member.value!!)
            }else if (!_member.value!!.isCheck){
                deleteList?.remove(_member.value!!)
            }
            Log.d("checkChloe","deleteList=${deleteList}")
        }
    }

    fun deleteMember() {
        shop.value?.let {
            if(deleteList!=null){
                for(i in deleteList){
                    deleteOrder(it.id, i)
                }
            }
        }
    }


    //點選確定後
    //把剩下的所有團員 狀態改為待付款

    fun readyCollect(){
        _shop.value?.let{
            updateOrderStatus(it.id,1)
            updateShopStatus(it.id, 1)
            expectStatus.value = 0
        }
    }

    //按鈕改變團購狀態

    var expectStatus = MutableLiveData<Int>()


    fun clickStatus(status:Int){
        expectStatus.value = status
        Log.d("Chloe","i want to change the status to $expectStatus")
    }

    fun updateStatus(){
        _shop.value?.let{
            updateShopStatus(it.id, expectStatus.value!!)
            expectStatus.value = 0
        }

        Log.d("Chloe","after update, now status is $${_shop.value?.status}")
    }

    fun editShopNotify(){
        var notify: Notify? = null
        _shop.value?.let {
            val notifyType : NotifyType =
                when (it.status){
                    1 -> NotifyType.STATUS_CHANGE_TO_GATHER_SUCCESS
                    2 -> NotifyType.STATUS_CHANGE_TO_ORDER_SUCCESS
                    3 -> NotifyType.STATUS_CHANGE_TO_SHOP_SHIPMENT
                    4 -> NotifyType.STATUS_CHANGE_TO_SHIPMENT_SUCCESS
                    5 -> NotifyType.STATUS_CHANGE_TO_PACKAGING
                    6 -> NotifyType.STATUS_CHANGE_TO_SHIPMENT
                    7 -> NotifyType.STATUS_CHANGE_TO_FINISH
                    else -> NotifyType.STATUS_CHANGE_TO_GATHER_SUCCESS
                }
            if (!messageContent.value.isNullOrEmpty()){
                notify = Notify(
                    shopId = it.id,
                    type = notifyType.type,
                    title = notifyType.title,
                    content = notifyType.toDisplayNotifyContent(_shop.value!!.title),
                    message = messageContent.value
                )
            }else if (messageContent.value.isNullOrEmpty() && (it.status == 1 || it.status == 6 || it.status == 7)) {
                notify = Notify(
                    shopId = it.id,
                    type = notifyType.type,
                    title = notifyType.title,
                    content = notifyType.toDisplayNotifyContent(_shop.value!!.title)
                )
            }else{
                notify = null
            }
        }

        notify?.let {
            postShopNotifyToMember(it)
        }
    }


    private fun getDetailShop(shopId: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getDetailShop(shopId)

            _shop.value = when (result) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    Log.i("Order", "shop result = ${result.data}")
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

    private fun getOrderOfShop(shopId : String) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.getOrderOfShop(shopId)
            Log.i("Order", "orderResult = $result")
            _order.value = when (result) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    Log.i("Order", "orderResult = ${result.data}")
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

    private fun deleteOrder(shopId: String, order: Order) {

        if (_order.value == null) {
            _error.value = "who r u?"
            _refreshStatus.value = false
            return
        }

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.deleteOrder(shopId,order)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    refresh()
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _error.value = MyApplication.instance.getString(R.string.result_fail)
                    _status.value = LoadApiStatus.ERROR
                }
            }
            _refreshStatus.value = false
        }
    }

    private fun updateShopStatus(shopId: String, shopStatus: Int) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.updateShopStatus(shopId,shopStatus)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    refresh()
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _error.value = MyApplication.instance.getString(R.string.result_fail)
                    _status.value = LoadApiStatus.ERROR
                }
            }
            _refreshStatus.value = false
        }
    }

    private fun updateOrderStatus(shopId: String,paymentStatus: Int) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.updateOrderStatus(shopId,paymentStatus)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    refresh()
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _error.value = MyApplication.instance.getString(R.string.result_fail)
                    _status.value = LoadApiStatus.ERROR
                }
            }
            _refreshStatus.value = false
        }
    }

    private fun postShopNotifyToMember(notify: Notify) {
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            when (val result = repository.postShopNotifyToMember(notify)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE

                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _error.value = MyApplication.instance.getString(R.string.result_fail)
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
    }

}


//fun readyCollect(){
//        if (_order.value!=null){
//                for (i in _order.value!!){
//                    i?.paymentStatus = 1
//                }
//        }
//            Log.d("Chloe","after delete, order value is ${_order.value}")
//            //更新collection
//            _shop.value?.order =_order.value
//            Log.d("Chloe","after update, now collection is ${_shop.value}")
//        //更新collection的status
//
//    _shop.value?.let{
//        updateOrderStatus(it.id,1)
//        updateShopStatus(it.id, 1)
//        expectStatus.value = 0
//    }
//}
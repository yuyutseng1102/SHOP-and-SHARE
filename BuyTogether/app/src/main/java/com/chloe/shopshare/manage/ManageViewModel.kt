package com.chloe.shopshare.manage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.*
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.ext.toDisplayNotifyContent
import com.chloe.shopshare.network.LoadApiStatus
import com.chloe.shopshare.notify.NotifyType
import com.chloe.shopshare.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ManageViewModel(private val repository: Repository, private val arguments: String) :
    ViewModel() {

    private val _shopId = MutableLiveData<String>().apply {
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
    val member: LiveData<Order?>
        get() = _member

    private val _isChecked = MutableLiveData<Boolean>()
    val isChecked: LiveData<Boolean>
        get() = _isChecked

    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess: LiveData<Boolean>
        get() = _deleteSuccess

    private val _successDecreaseOrder = MutableLiveData<Boolean>()
    val successDecreaseOrder: LiveData<Boolean>
        get() = _successDecreaseOrder

    val messageContent = MutableLiveData<String?>()

    private val _chatRoom = MutableLiveData<ChatRoom>()
    val chatRoom: LiveData<ChatRoom>
        get() = _chatRoom

    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    private val _refreshStatus = MutableLiveData<Boolean>()
    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    private val _navigateToDetail = MutableLiveData<String?>()
    val navigateToDetail: LiveData<String?>
        get() = _navigateToDetail

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var checkedMemberPosition = MutableLiveData<Int?>()
    var deleteList = MutableLiveData<List<Order>>()

    lateinit var myId: String


    init {
        _shopId.value?.let {
            getDetailShop(it)
            getOrderOfShop(it)
        }

        UserManager.userId?.let { myId = it }
        deleteList.value = listOf()

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

    fun navigateToDetail(shopId: String) {
        _navigateToDetail.value = shopId
    }

    fun onDetailNavigate() {
        _navigateToDetail.value = null
    }

    private val _navigateToChatRoom = MutableLiveData<ChatRoom>()
    val navigateToChatRoom: LiveData<ChatRoom>
        get() = _navigateToChatRoom

    fun checkAgain(member: Order, position: Int) {
        checkedMemberPosition.value = position
        Log.d("checkChloe", "selectMember=$member, position=$position")
        _member.value = member
        val list = deleteList.value?.toMutableList() ?: mutableListOf()
        if (_member.value != null) {
            if (_member.value!!.isCheck) {
                list.add(_member.value!!)
            } else if (!_member.value!!.isCheck) {
                list.remove(_member.value!!)
            }
            Log.d("checkChloe", "deleteList=${list}")
            deleteList.value = list
        }
    }

    fun deleteMember() {
        _shop.value?.let { shop ->
            deleteList.value?.let { list ->
                for (item in list) {
                    deleteOrder(shop.id, item)
                }
            }
        }
    }

    fun onFailNotifySend() {
        _deleteSuccess.value = null
        deleteList.value = null
    }

    fun readyCollect() {
        _shop.value?.let {
            updateOrderStatus(it.id)
            updateShopStatus(it.id, 1)
            expectStatus.value = 0
        }
    }

    var expectStatus = MutableLiveData<Int>()
    fun clickStatus(status: Int) {
        expectStatus.value = status
        Log.d("Chloe", "i want to change the status to $expectStatus")
    }

    fun updateStatus() {
        _shop.value?.let {
            updateShopStatus(it.id, expectStatus.value!!)
            expectStatus.value = 0
        }
        Log.d("Chloe", "after update, now status is $${_shop.value?.status}")
    }

    fun editShopNotify() {
        var notify: Notify? = null
        _shop.value?.let {
            val notifyType: NotifyType =
                when (it.status) {
                    1 -> NotifyType.STATUS_CHANGE_TO_GATHER_SUCCESS
                    2 -> NotifyType.STATUS_CHANGE_TO_ORDER_SUCCESS
                    3 -> NotifyType.STATUS_CHANGE_TO_SHOP_SHIPMENT
                    4 -> NotifyType.STATUS_CHANGE_TO_SHIPMENT_SUCCESS
                    5 -> NotifyType.STATUS_CHANGE_TO_PACKAGING
                    6 -> NotifyType.STATUS_CHANGE_TO_SHIPMENT
                    7 -> NotifyType.STATUS_CHANGE_TO_FINISH
                    else -> NotifyType.STATUS_CHANGE_TO_GATHER_SUCCESS
                }
            if (!messageContent.value.isNullOrEmpty()) {
                notify = Notify(
                    shopId = it.id,
                    type = notifyType.type,
                    title = notifyType.title,
                    content = notifyType.toDisplayNotifyContent(_shop.value!!.title),
                    message = messageContent.value
                )
            } else if (messageContent.value.isNullOrEmpty() && (it.status == 1 || it.status == 6 || it.status == 7)) {
                notify = Notify(
                    shopId = it.id,
                    type = notifyType.type,
                    title = notifyType.title,
                    content = notifyType.toDisplayNotifyContent(_shop.value!!.title)
                )
            } else {
                notify = null
            }
        }

        notify?.let {
            postShopNotifyToMember(it)
        }
    }

    fun editOrderNotify(orderList: List<Order>) {
        var notify: Notify? = null
        _shop.value?.let {
            notify = Notify(
                shopId = it.id,
                type = NotifyType.ORDER_FAIL.type,
                title = NotifyType.ORDER_FAIL.title,
                content = NotifyType.ORDER_FAIL.toDisplayNotifyContent(it.title)
            )
        }

        notify?.let {
            postOrderNotifyToMember(orderList, it)
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


    private fun getOrderOfShop(shopId: String) {

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
            val result = repository.deleteOrder(shopId, order)
            _deleteSuccess.value =
                when (result) {
                    is Result.Success -> {
                        _error.value = null
                        _status.value = LoadApiStatus.DONE
                        refresh()
                        true
                    }
                    is Result.Fail -> {
                        _error.value = result.error
                        _status.value = LoadApiStatus.ERROR
                        false
                    }
                    is Result.Error -> {
                        _error.value = result.exception.toString()
                        _status.value = LoadApiStatus.ERROR
                        false
                    }
                    else -> {
                        _error.value = MyApplication.instance.getString(R.string.result_fail)
                        _status.value = LoadApiStatus.ERROR
                        false
                    }
                }
            _refreshStatus.value = false
        }
    }

    fun onSuccessDeleteOrder() {
        _deleteSuccess.value = null
    }

    fun decreaseOrderSize(shopId: String, orderSize: Int) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING
            val result = repository.decreaseOrderSize(shopId, orderSize)
            _successDecreaseOrder.value =
                when (result) {
                    is Result.Success -> {
                        _error.value = null
                        _status.value = LoadApiStatus.DONE
                        true

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

    fun onSuccessDecreaseOrder() {
        _successDecreaseOrder.value = null
    }


    private fun updateShopStatus(shopId: String, shopStatus: Int) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.updateShopStatus(shopId, shopStatus)) {
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

    private fun updateOrderStatus(shopId: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.updateOrderStatus(shopId, 1)) {
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

    private fun postOrderNotifyToMember(orderList: List<Order>, notify: Notify) {
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            when (val result = repository.postOrderNotifyToMember(orderList, notify)) {
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

    fun getChatRoom(myId: String, friendId: String) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.getChatRoom(myId, friendId)
            _chatRoom.value =
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
            _chatRoom.value?.let {
                _navigateToChatRoom.value = it
            }
        }
    }

    fun onChatRoomNavigated() {
        _navigateToChatRoom.value = null
    }

}

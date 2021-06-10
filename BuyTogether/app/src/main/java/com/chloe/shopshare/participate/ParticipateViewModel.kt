package com.chloe.shopshare.participate

import android.util.Log
import android.widget.RadioButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.*
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.ext.toDisplayNotifyContent
import com.chloe.shopshare.ext.toDisplayNotifyMessage
import com.chloe.shopshare.host.DeliveryMethod
import com.chloe.shopshare.network.LoadApiStatus
import com.chloe.shopshare.notify.NotifyType
import com.chloe.shopshare.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ParticipateViewModel(
    private val repository: Repository,
    private val argsShop: Shop,
    private val argsProduct: List<Product>
) : ViewModel() {

    private val _shop = MutableLiveData<Shop>().apply {
        value = argsShop
    }
    val shop: LiveData<Shop>
        get() = _shop

    private val _product = MutableLiveData<List<Product>>().apply {
        value = argsProduct
    }
    val product: LiveData<List<Product>>
        get() = _product

    private val _successNumber = MutableLiveData<String>()
    val successNumber: LiveData<String>
        get() = _successNumber

    private val _successIncreaseOrder = MutableLiveData<Boolean>()
    val successIncreaseOrder: LiveData<Boolean>
        get() = _successIncreaseOrder

    private val _successToNotify = MutableLiveData<Boolean>()
    val successToNotify: LiveData<Boolean>
        get() = _successToNotify

    private val _navigateToSuccess = MutableLiveData<Boolean>()
    val navigateToSuccess: LiveData<Boolean>
        get() = _navigateToSuccess

    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status


    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _leave = MutableLiveData<Boolean>()
    val leave: LiveData<Boolean>
        get() = _leave


    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    //order data


    lateinit var userId: String
    val name = MutableLiveData<String>()
    val price = MutableLiveData<Int>()
    val phone = MutableLiveData<String>()
    private val _delivery = MutableLiveData<Int>()
    val delivery: LiveData<Int>
        get() = _delivery
    val address = MutableLiveData<String>()
    val note = MutableLiveData<String>()
    var paymentStatus = 0
    val increaseEnable: Boolean = true
    private val _isInvalid = MutableLiveData<Int>()
    val isInvalid: LiveData<Int>
        get() = _isInvalid


    init {
        UserManager.userId?.let { userId = it }
        price.value = 0
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    fun readyToPost() {
        //紅字訊息提醒
        _isInvalid.value =
            when {
                _product.value.isNullOrEmpty() -> INVALID_FORMAT_PRODUCT_EMPTY
                delivery.value == null -> INVALID_FORMAT_PRODUCT_EMPTY
                price.value == 0 || price.value == null -> INVALID_FORMAT_DELIVERY_EMPTY
                name.value.isNullOrEmpty() -> INVALID_FORMAT_PRICE_EMPTY
                phone.value.isNullOrEmpty() -> INVALID_FORMAT_NAME_EMPTY
                address.value.isNullOrEmpty() -> INVALID_FORMAT_PHONE_EMPTY
                else -> null
            }

//        //是否有東西尚未輸入
//        if (_isInvalid.value != null){
//            _status.value = LoadApiStatus.LOADING
//            Log.d("Chloe","The input is invalid, the value is ${_isInvalid.value}")
//        }else{
//            _status.value = LoadApiStatus.DONE
//        }

        Log.d("Chloe", "The _product.value is valid ${_product.value}")
        Log.d("Chloe", "The delivery.value is valid${delivery.value}")
        Log.d("Chloe", "The price.value is valid${price.value}")
        Log.d("Chloe", "The name.value is valid${name.value}")
        Log.d("Chloe", "The phone.value is valid${phone.value}")
        Log.d("Chloe", "The address.value is valid${address.value}")
    }

    var order = Order()
    fun sendOrder() {
        order = Order(
            userId = userId,
            product = _product.value!!,
            price = price.value!!,
            name = name.value!!,
            phone = phone.value!!,
            delivery = delivery.value!!,
            address = address.value!!,
            note = note.value ?: "",
            paymentStatus = paymentStatus
        )

        _shop.value?.let {
            postOrder(it.id, order)
        }
    }
    fun editNotify(){
        val notify = Notify(
            shopId = _shop.value!!.id,
            type = NotifyType.ORDER_INCREASE.type,
            title = NotifyType.ORDER_INCREASE.title,
            content = NotifyType.ORDER_INCREASE.toDisplayNotifyContent(_shop.value!!.title),
            message = NotifyType.ORDER_INCREASE.toDisplayNotifyMessage(order)
        )

        _shop.value?.let {
            postNotifyToHost(it.userId, notify)
        }
    }



    fun removeProduct(product: Product) {
        val productList = _product.value?.toMutableList()
        productList?.remove(product)
        _product.value = productList ?: mutableListOf()
    }


    fun increaseQuantity(product: Product) {
        product.quantity = product.quantity?.plus(1)
        Log.d("Chloe", "on increase!Product is $product")
        updateProduct(product)
    }

    fun decreaseQuantity(product: Product) {
        product.quantity = product.quantity?.minus(1)
        Log.d("Chloe", "on decrease!Product is $product")
        updateProduct(product)
    }

    private fun updateProduct(productItem: Product) {
        if (_product.value != null) {
            for (item in _product.value!!) {
                if (item.title == productItem.title) {
                    item.quantity = productItem.quantity
                }
            }
            _product.value = _product.value
            Log.d(
                "Chloe",
                "new product is updated to ${_product.value},product is ${product.value}"
            )
        }
    }

    fun displayDelivery(delivery: Int): String {
        for (type in DeliveryMethod.values()) {
            if (type.delivery == delivery) {
                return type.title
            }
        }
        return ""
    }
    fun selectDelivery(radioButton: RadioButton) {

        for (type in DeliveryMethod.values()) {
            if (type.title == radioButton.text) {
                _delivery.value = type.delivery
            }
        }
        _delivery.value = _delivery.value
    }

    fun navigateToSuccess() {
        _navigateToSuccess.value = true
    }

    fun onSuccessNavigated() {
        _navigateToSuccess.value = null
    }






    private fun postOrder(shopId: String, order: Order) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING
            val result = repository.postOrder(shopId, order)
            _successNumber.value =
            when (result) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    leave(true)
                    result.data.number

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

    fun increaseOrderSize(shopId: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING
            val result = repository.increaseOrderSize(shopId)
            _successIncreaseOrder.value =
                when (result) {
                    is Result.Success -> {
                        _error.value = null
                        _status.value = LoadApiStatus.DONE
                        leave(true)
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


    private fun postNotifyToHost(hostId: String, notify: Notify) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING
            val result = repository.postNotifyToHost(hostId, notify)
            _successToNotify.value =
            when (result) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    leave(true)
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



    fun leave(needRefresh: Boolean = false) {
        _leave.value = needRefresh
    }


    companion object {

        const val INVALID_FORMAT_PRODUCT_EMPTY = 0x11
        const val INVALID_FORMAT_DELIVERY_EMPTY = 0x12
        const val INVALID_FORMAT_PRICE_EMPTY = 0x13
        const val INVALID_FORMAT_NAME_EMPTY = 0x14
        const val INVALID_FORMAT_PHONE_EMPTY = 0x15
        const val INVALID_FORMAT_ADDRESS_EMPTY = 0x16

    }

}



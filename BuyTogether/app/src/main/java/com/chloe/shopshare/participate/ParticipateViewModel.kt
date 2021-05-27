package com.chloe.shopshare.participate

import android.util.Log
import android.widget.RadioButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.Order
import com.chloe.shopshare.data.Product
import com.chloe.shopshare.data.Result
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.host.DeliveryMethod
import com.chloe.shopshare.network.LoadApiStatus
import com.chloe.shopshare.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class ParticipateViewModel(
    private val repository: Repository,
    private val argsShop: Shop,
    private val argsProduct: List<Product>
) :ViewModel() {

    private val _shop = MutableLiveData<Shop>().apply {
        value = argsShop
    }

    val shop: LiveData<Shop>
        get() = _shop

    private val _product= MutableLiveData<List<Product>>().apply {
        value = argsProduct
    }
    val product: LiveData<List<Product>>
        get() = _product

    private val _order = MutableLiveData<Order>()
    val order: LiveData<Order>
        get() = _order

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun postOrder(shopId: String, order: Order) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.postOrder(shopId, order)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    leave(true)
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

    //order data
    val orderId = "19479203"
    val orderTime: Long= Calendar.getInstance().timeInMillis
    val userId = UserManager.userId
    val name = MutableLiveData<String>()
    val price = MutableLiveData<Int>()
    val phone = MutableLiveData<String>()
    private val _delivery = MutableLiveData<Int>()
    val delivery : LiveData<Int>
        get() = _delivery
    val address = MutableLiveData<String>()
    val note = MutableLiveData<String>()
    var paymentStatus = 0


    init {
        price.value = 0
    }




    val isEnable = MutableLiveData<Boolean>()
    val increaseEnable: Boolean = true


    fun removeProduct(product: Product) {
        val productList = _product.value?.toMutableList()
        productList?.remove(product)
        _product.value = productList?: mutableListOf()
    }

    private fun updateProduct(productItem: Product) {
        if(_product.value!= null){
            for (item in _product.value!!){
                if (item.title == productItem.title){
                    item.quantity = productItem.quantity
                }
            }
            _product.value = _product.value
            Log.d("Chloe","new product is updated to ${_product.value},product is ${product.value}")
        }
    }




    fun increaseQuantity(product: Product) {
        product.quantity = product.quantity?.plus(1)
        Log.d("Chloe","on increase!Product is $product")
        updateProduct(product)
    }

    fun decreaseQuantity(product: Product) {
        product.quantity = product.quantity?.minus(1)
        Log.d("Chloe","on decrease!Product is $product")
        updateProduct(product)
    }

    fun isEnable(){
        isEnable.value = !_product.value.isNullOrEmpty()
    }


    //select delivery method

//    fun addRadio(radioGroup: RadioGroup,radioButton: RadioButton){
//        for (i in collection.value?.deliveryMethod!!.indices) {
//            // assign an automatically generated id to the radio button
//            radioButton.id = View.generateViewId()
//            radioButton.text = collection.value?.deliveryMethod!![i].toString()
//            // add radio button to the radio group
//            radioGroup.addView(radioGroup)
//        }
//    }



    fun displayDelivery(delivery:Int):String{
            for (type in DeliveryMethod.values()) {
                if (type.delivery == delivery) {
                    return type.title
                }
            }
            return ""
        }

    fun selectDelivery(radioButton: RadioButton){

        for (type in DeliveryMethod.values()) {
            if (type.title == radioButton.text) {
                _delivery.value = type.delivery
            }
        }
        _delivery.value = _delivery.value
    }

    private val _isInvalid = MutableLiveData<Int>()
    val isInvalid: LiveData<Int>
        get() =  _isInvalid


    fun readyToPost() {
        //紅字訊息提醒
        _isInvalid.value =
                when {
                    _product.value.isNullOrEmpty()-> INVALID_FORMAT_PRODUCT_EMPTY
                    delivery.value == null -> INVALID_FORMAT_DELIVERY_EMPTY
                    price.value == 0 || price.value == null -> INVALID_FORMAT_PRICE_EMPTY
                    name.value.isNullOrEmpty() -> INVALID_FORMAT_NAME_EMPTY
                    phone.value.isNullOrEmpty() -> INVALID_FORMAT_PHONE_EMPTY
                    address.value.isNullOrEmpty() -> INVALID_FORMAT_ADDRESS_EMPTY
                    else -> null
                }

        //是否有東西尚未輸入
        if (_isInvalid.value != null){
            _status.value = LoadApiStatus.LOADING
            Log.d("Chloe","The input is invalid, the value is ${_isInvalid.value}")
        }else{
            _status.value = LoadApiStatus.DONE
        }

        Log.d("Chloe","The _product.value is valid ${_product.value}")
        Log.d("Chloe","The delivery.value is valid${delivery.value}")
        Log.d("Chloe","The price.value is valid${price.value}")
        Log.d("Chloe","The name.value is valid${name.value}")
        Log.d("Chloe","The phone.value is valid${phone.value}")
        Log.d("Chloe","The address.value is valid${address.value}")
    }
    var orderList : MutableList<Order?> = mutableListOf()
    fun sendOrder(){
        if (! _shop.value?.order.isNullOrEmpty()){
            orderList = _shop.value?.order!!.toMutableList()
        }

        _order.value = Order(

                userId = userId!!,
                product = _product.value!!,
                price = price.value!!,
                phone = phone.value!!,
                delivery = delivery.value!!,
                address = address.value!!,
                note = note.value?:"",
                paymentStatus = paymentStatus
        )

        orderList.add( _order.value)
        _shop.value?.order = orderList
    }

    fun leave(needRefresh: Boolean = false) {
        _leave.value = needRefresh
    }


    companion object {

        const val INVALID_FORMAT_PRODUCT_EMPTY       = 0x11
        const val INVALID_FORMAT_DELIVERY_EMPTY      = 0x12
        const val INVALID_FORMAT_PRICE_EMPTY         = 0x13
        const val INVALID_FORMAT_NAME_EMPTY          = 0x14
        const val INVALID_FORMAT_PHONE_EMPTY         = 0x15
        const val INVALID_FORMAT_ADDRESS_EMPTY       = 0x16

    }

}



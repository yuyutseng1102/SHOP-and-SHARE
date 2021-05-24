package com.chloe.shopshare.participate

import android.util.Log
import android.widget.RadioButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.data.Collections
import com.chloe.shopshare.data.Order
import com.chloe.shopshare.data.Product
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.host.DeliveryMethod
import com.chloe.shopshare.network.LoadApiStatus
import java.util.*

class ParticipateViewModel(
    private val repository: Repository,
    private val argsCollection: Collections,
    private val argsProduct: List<Product>
) :ViewModel() {

    private val _collection = MutableLiveData<Collections>().apply {
        value = argsCollection
    }

    val collection: LiveData<Collections>
        get() = _collection

    private val _product= MutableLiveData<List<Product>>().apply {
        value = argsProduct
    }
    val product: LiveData<List<Product>>
        get() = _product

    private val _order = MutableLiveData<Order>()
    val order: LiveData<Order>
        get() = _order

    //order data
    val orderId: Long = 19479203
    val orderTime: Long= Calendar.getInstance().timeInMillis
    val userId:Long = 193798
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
                if (item.productTitle == productItem.productTitle){
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


    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status

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
        if (! _collection.value?.order.isNullOrEmpty()){
            orderList = _collection.value?.order!!.toMutableList()
        }

        _order.value = Order(
                orderId = orderId,
                orderTime = orderTime,
                userId = userId,
                product = _product.value!!,
                price = price.value!!,
                phone = phone.value!!,
                delivery = delivery.value!!,
                address = address.value!!,
                note = note.value?:"",
                paymentStatus = paymentStatus
        )

        orderList.add( _order.value)
        _collection.value?.order = orderList
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



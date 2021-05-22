package com.chloe.buytogether.participate

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.buytogether.data.Collections
import com.chloe.buytogether.data.Order
import com.chloe.buytogether.data.Product
import com.chloe.buytogether.data.source.Repository
import kotlinx.coroutines.launch
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
    val delivery = MutableLiveData<Int>()
    val address = MutableLiveData<String>()
    val note = MutableLiveData<String>()
    var paymentStatus = 0



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


}



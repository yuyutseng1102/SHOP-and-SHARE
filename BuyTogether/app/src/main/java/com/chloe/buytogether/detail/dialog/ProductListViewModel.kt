package com.chloe.buytogether.detail.dialog

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.buytogether.data.Collections
import com.chloe.buytogether.data.Order
import com.chloe.buytogether.data.Product
import com.chloe.buytogether.data.source.Repository
import kotlinx.coroutines.launch

class ProductListViewModel(private val repository: Repository,
                           private val argsCollection: Collections,
                           private val argsProduct: List<Product>
): ViewModel() {

    private val _collection = MutableLiveData<Collections>().apply {
        value = argsCollection
    }

    val collection: LiveData<Collections>
        get() = _collection

    private val _product= MutableLiveData<List<Product>>()
    val product: LiveData<List<Product>>
        get() = _product

    val isEnable = MutableLiveData<Boolean?>()
    val increaseEnable: Boolean = true

    init {
        _product.value = argsProduct
    }


    // 關閉時回傳給detail頁面
    private val _navigateToDetails = MutableLiveData<List<Product>>()
    val navigateToDetails: LiveData<List<Product>>
        get() = _navigateToDetails

    fun navigateToDetails(product:List<Product>) {
        _navigateToDetails.value = product
    }

    fun onDetailsNavigated() {
        _navigateToDetails.value = null
    }


    // 要傳給確定跟團頁面的
    private val _navigateToParticipate = MutableLiveData<List<Product>>()
    val navigateToParticipate: LiveData<List<Product>>
        get() = _navigateToParticipate

    fun navigateToParticipate(product:List<Product>) {
        _navigateToParticipate.value = product
    }

    fun onParticipateNavigated() {
        _navigateToParticipate.value = null
    }



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
                    } }
            _product.value = _product.value
            Log.d("Chloe","new product is updated to ${_product.value},product is ${product.value}")
            }
        }




    fun increaseQuantity(product: Product) {
        product.quantity = product.quantity?.plus(1)
        Log.d("Chloe","on increase!Product is ${product}")
        updateProduct(product)
    }

    fun decreaseQuantity(product: Product) {

        product.quantity = product.quantity?.minus(1)
        Log.d("Chloe","on decrease!Product is ${product}")
        updateProduct(product)
    }

    fun isEnable(){
        isEnable.value = !_product.value.isNullOrEmpty()
    }
}


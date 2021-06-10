package com.chloe.shopshare.detail.dialog

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.Product
import com.chloe.shopshare.data.source.Repository

class ProductListViewModel(private val repository: Repository,
                           private val argsShop: Shop,
                           private val argsProduct: List<Product>
): ViewModel() {

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

    val isEnable = MutableLiveData<Boolean?>()
    val increaseEnable: Boolean = true




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
        Log.d("Chloe","new product is remove to ${_product.value},product List is ${productList}")
    }

    private fun updateProduct(productItem: Product) {
        if(_product.value!= null){
            for (item in _product.value!!){
                    if (item.title == productItem.title){
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


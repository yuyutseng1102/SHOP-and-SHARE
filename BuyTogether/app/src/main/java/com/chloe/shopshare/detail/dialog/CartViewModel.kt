package com.chloe.shopshare.detail.dialog

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.data.Cart
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.Product
import com.chloe.shopshare.data.source.Repository

class CartViewModel(private val repository: Repository, private val args: Cart): ViewModel() {

    private val _shop = MutableLiveData<Shop>().apply {
        value = args.shop
    }

    val shop: LiveData<Shop>
        get() = _shop

    private val _products= MutableLiveData<List<Product>>().apply {
        value = args.products
    }
    val products: LiveData<List<Product>>
        get() = _products

    val isEnable = MutableLiveData<Boolean?>()
    val increaseEnable: Boolean = true


    private val _navigateToDetails = MutableLiveData<List<Product>>()
    val navigateToDetails: LiveData<List<Product>>
        get() = _navigateToDetails

    fun navigateToDetails(product:List<Product>) {
        _navigateToDetails.value = product
    }

    fun onDetailsNavigated() {
        _navigateToDetails.value = null
    }


    private val _navigateToOrder = MutableLiveData<Cart>()
    val navigateToOrder: LiveData<Cart>
        get() = _navigateToOrder

    fun navigateToOrder(shop: Shop, products: List<Product>) {
        _navigateToOrder.value = Cart(shop, products)
    }

    fun onOrderNavigated() {
        _navigateToOrder.value = null
    }



    fun removeProduct(product: Product) {
        val productList = _products.value?.toMutableList()
        productList?.remove(product)
        _products.value = productList?: mutableListOf()
        Log.d("Chloe","new product is remove to ${_products.value},product List is ${productList}")
    }

    private fun updateProduct(productItem: Product) {
        if(_products.value!= null){
            for (item in _products.value!!){
                    if (item.title == productItem.title){
                        item.quantity = productItem.quantity
                    } }
            _products.value = _products.value
            Log.d("Chloe","new product is updated to ${_products.value},product is ${products.value}")
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
        isEnable.value = !_products.value.isNullOrEmpty()
    }
}


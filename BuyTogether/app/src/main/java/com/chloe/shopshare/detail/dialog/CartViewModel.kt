package com.chloe.shopshare.detail.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.data.Cart
import com.chloe.shopshare.data.Product
import com.chloe.shopshare.data.Shop

class CartViewModel(private val args: Cart) : ViewModel() {

    private val _shop = MutableLiveData<Shop>().apply {
        value = args.shop
    }

    val shop: LiveData<Shop>
        get() = _shop

    private val _products = MutableLiveData<List<Product>>().apply {
        value = args.products
    }
    val products: LiveData<List<Product>>
        get() = _products

    val isEnable = MutableLiveData<Boolean?>()
    val increaseEnable: Boolean = true

    private val _navigateToDetails = MutableLiveData<List<Product>>()
    val navigateToDetails: LiveData<List<Product>>
        get() = _navigateToDetails

    fun navigateToDetails(product: List<Product>) {
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
        _products.value = productList ?: mutableListOf()
    }

    private fun updateProduct(productItem: Product) {
        _products.value?.let {
            for (product in it) {
                if (product.title == productItem.title) {
                    product.quantity = productItem.quantity
                }
            }
            _products.value = it
        }
    }

    fun increaseQuantity(product: Product) {
        product.quantity = product.quantity?.plus(1)
        updateProduct(product)
    }

    fun decreaseQuantity(product: Product) {
        product.quantity = product.quantity?.minus(1)
        updateProduct(product)
    }

    fun isEnable() {
        isEnable.value = !_products.value.isNullOrEmpty()
    }
}


package com.chloe.shopshare.detail.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.data.Cart
import com.chloe.shopshare.data.Product
import com.chloe.shopshare.data.Shop

class VariationViewModel(private val args: Cart) : ViewModel() {

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

    private val _option = MutableLiveData<List<String>>().apply {
        value = _shop.value?.option
    }
    val option: LiveData<List<String>>
        get() = _option

    val quantity = MutableLiveData<Int?>()
    val isEnable = MutableLiveData<Boolean>()
    val isVisible = MutableLiveData<Boolean>()
    val productTitle = MutableLiveData<String>()

    private val _navigateToDetail = MutableLiveData<List<Product>>()
    val navigateToDetail: LiveData<List<Product>>
        get() = _navigateToDetail

    private val _navigateToOrder = MutableLiveData<Cart>()
    val navigateToOrder: LiveData<Cart>
        get() = _navigateToOrder

    init {
        quantity.value = 0
        isEnable.value = false
        isVisible.value = false
    }

    fun navigateToDetail(product: List<Product>) {
        _navigateToDetail.value = product
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }


    fun navigateToOrder(shop: Shop, products: List<Product>) {
        _navigateToOrder.value = Cart(shop, products)
    }

    fun onOrderNavigated() {
        _navigateToOrder.value = null
    }

    //select option
    val selectedChip = MutableLiveData<Int>()
    fun getOption() {
        _shop.value?.let { shop ->
            when (shop.isStandard) {
                true -> {
                    _option.value?.let { option ->
                        selectedChip.value?.let { position ->
                            productTitle.value = option[position]
                        }
                    }
                    quantity.value = 1
                    isEnable.value = true
                    isVisible.value = false
                }
                else -> {
                    isEnable.value = false
                    isVisible.value = true
                }
            }
        }
    }

    fun increaseQuantity() {
        quantity.value = quantity.value?.plus(1)
    }

    fun decreaseQuantity() {
        quantity.value = quantity.value?.minus(1)
    }

    fun isEditable() {
        isEnable.value = !productTitle.value.isNullOrEmpty()
    }

    fun finishSelector(): List<Product> {
        val productList = _products.value?.toMutableList() ?: mutableListOf()
        when (checkSame(productList)) {
            true -> {
                for (item in productList) {
                    if (item.title == productTitle.value) {
                        item.quantity = item.quantity?.plus(quantity.value ?: 0)
                    }
                }
            }
            false -> productList.add(Product(productTitle.value ?: "", quantity.value))
        }
        _products.value = productList
        return _products.value ?: listOf()
    }

    private fun checkSame(list: List<Product>): Boolean {
        var isSame = false
        for (i in list) {
            if (i.title == productTitle.value) {
                isSame = true
            }
        }
        return isSame
    }
}
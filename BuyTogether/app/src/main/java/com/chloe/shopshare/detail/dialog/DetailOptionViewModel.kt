package com.chloe.shopshare.detail.dialog

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.data.Collections
import com.chloe.shopshare.data.Product
import com.chloe.shopshare.data.source.Repository

class DetailOptionViewModel(private val repository: Repository,
                            private val argsCollection: Collections,
                            private val argsProduct: List<Product>
): ViewModel() {

    private val _collection = MutableLiveData<Collections>().apply {
        value = argsCollection
    }

    val collection: LiveData<Collections>
        get() = _collection

    private val _option = MutableLiveData<List<String>>().apply {
        value = _collection.value?.option
    }

    val option: LiveData<List<String>>
        get() = _option

    val quantity = MutableLiveData<Int?>()

    val isEnable = MutableLiveData<Boolean>()
    val isVisible = MutableLiveData<Boolean>()

    private val _product= MutableLiveData<List<Product>>().apply {
        value = argsProduct
    }
    val product: LiveData<List<Product>>
        get() = _product

    val productTitle = MutableLiveData<String>()

    // 要傳給商品清單頁面的(回detail頁)
    private val _navigateToProductList = MutableLiveData<List<Product>>()
    val navigateToProductList: LiveData<List<Product>>
        get() = _navigateToProductList

    fun navigateToProductList(product:List<Product>) {
        _navigateToProductList.value = product
    }

    fun onProductListNavigated() {
        _navigateToProductList.value = null
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

    init {
        quantity.value = 0
        isEnable.value = false
        isVisible.value = false
    }

    //select option
    val selectedChip = MutableLiveData<Int>()
    fun getOption() {
        if (collection.value!!.isStandard) {
            if (_option.value != null && selectedChip.value != null) {
                productTitle.value = _option.value!![selectedChip.value!!]
            }
            quantity.value = 1
            isEnable.value = true
            isVisible.value = false
        }else{
            isEnable.value = false
            isVisible.value = true
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

    private fun updateProduct(productItem: Product) {
        productItem == Product(productTitle.value!!,quantity.value)
        if(_product.value!= null){
            for (item in _product.value!!){
                if (item.productTitle == productItem.productTitle){
                    item.quantity = productItem.quantity
                } }
            _product.value = _product.value
            Log.d("Chloe","new product is updated to ${_product.value},product is ${product.value}")
        }
    }

    fun finishSelector(): List<Product> {
        val productList = _product.value?.toMutableList()?: mutableListOf()
        Log.d("Chloe","finishSelector productList is$productList")

        //如果清單內已有相同商品,應合併數量
        var isSame : Boolean = false
        fun checkSame() : Boolean{
            for (i in productList){
                if(i.productTitle == productTitle.value){
                    isSame = true
                }
            }
            return isSame
        }

        checkSame()

        when (isSame){
            true -> {
                for (item in productList){
                    if(item.productTitle == productTitle.value){
                        item.quantity = item.quantity?.plus(quantity.value!!)
                    }
                }
            }
            false -> productList.add(Product(productTitle.value!!,quantity.value))
        }
        _product.value = productList

        Log.d("Chloe","productTitle.value is${productTitle.value}  quantity.value is ${quantity.value}")
        Log.d("Chloe","productList is${productList}")

        Log.d("Chloe","_product.value is${_product.value}")
        return _product.value?:listOf()
    }



    fun onOptionSend(){
        _product.value = null
    }
}
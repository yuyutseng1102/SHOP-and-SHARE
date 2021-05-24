package com.chloe.shopshare.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.data.Collections
import com.chloe.shopshare.data.Order
import com.chloe.shopshare.data.Product
import com.chloe.shopshare.data.source.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class DetailViewModel(
    private val repository: Repository,
    private val arguments: Collections
):ViewModel() {

    // Detail has product data from arguments
    private val _collection = MutableLiveData<Collections>().apply {
        value = arguments
    }
    val collection: LiveData<Collections>
        get() = _collection

    private val _order = MutableLiveData<Order>()
    val order: LiveData<Order>
        get() = _order


    private val _product = MutableLiveData<List<Product>?>()
    val product: LiveData<List<Product>?>
        get() = _product

    val isChecked = MutableLiveData<Boolean>()

    val productItem = MutableLiveData<Product?>()

    init {
        isChecked.value = false
    }



    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)



    //要傳給選擇商品選項頁面的

    private val _navigateToOption = MutableLiveData<List<Product>>()
    val navigateToOption: LiveData<List<Product>>
        get() = _navigateToOption

    fun navigateToOption(product: List<Product>?) {
        _navigateToOption.value = product?: listOf()
    }

    fun onOptionNavigated() {
        _navigateToOption.value = null
    }

//mock
//    private val orderId : Int = 1222
//    private val orderTime: Long= java.util.Calendar.getInstance().timeInMillis
//    private val userId:Long = 10000002
//
//    private val price: Int = 2000
//    private val phone:String = "0988888888"
//    private val delivery: String = "711永和門市"
//    private val note: String? = "無"
//    private val mockPaymentStatus: Int = 0

    // 要傳給商品清單頁面的
    private val _navigateToProductList = MutableLiveData<List<Product>>()
    val navigateToProductList: LiveData<List<Product>>
        get() = _navigateToProductList

    fun navigateToProductList(product: List<Product>?) {
        _navigateToProductList.value = product?: listOf()
    }

    fun onProductListNavigated() {
        _navigateToProductList.value = null
    }



    // 要傳給確定跟團頁面的
    private val _navigateToParticipate = MutableLiveData<Collections>()

    val navigateToParticipate: LiveData<Collections>
        get() = _navigateToParticipate

    fun navigateToParticipate(collection: Collections) {
        _navigateToParticipate.value = collection
    }

    fun onParticipateNavigated() {
        _navigateToParticipate.value = null
    }


    //從選擇頁面/清單頁面回來要把新的productList更新取代現有的list中

    fun updateProductList(product: List<Product>){
        if (product.isNullOrEmpty()){
            _product.value = null
            Log.d("Chloe","Product is update to null =${_product.value} ,  product from selector is ${product}}")
        }else{
            _product.value = product
            Log.d("Chloe","Product is update to new =${_product.value} ,  product from selector is ${product}}")
        }
    }







}
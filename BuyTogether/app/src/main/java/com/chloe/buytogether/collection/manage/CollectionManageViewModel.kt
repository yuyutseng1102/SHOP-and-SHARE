package com.chloe.buytogether.collection.manage

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.buytogether.data.Collections
import com.chloe.buytogether.data.Order
import com.chloe.buytogether.data.Product
import com.chloe.buytogether.data.source.Repository
import kotlinx.android.parcel.Parcelize
import java.util.*

class CollectionManageViewModel(
        private val repository: Repository,
        private val arguments: Collections
):ViewModel() {


    private val _collection = MutableLiveData<Collections>().apply {
        value = arguments
    }

    val collection: LiveData<Collections>
        get() = _collection

    private val _order = MutableLiveData<List<Order>>()
    val order: LiveData<List<Order>>
        get() = _order

//    private val _product = MutableLiveData<List<Product>>().apply {
//        value = products
//    }
//    val product: LiveData<List<Product>>
//        get() = _product

    //mock data

    private val orderId = 1245L
    private val orderTime: Long= Calendar.getInstance().timeInMillis
    private val userId:Long = 193798
    private val products:List<Product> = listOf(Product("棉麻上衣白色/M",1),Product("法式雪紡背心/M",2),Product("開襟洋裝/M",5))
    private val price: Int = 2000
    private val phone:String = "0988888888"
    private val delivery: String = "711永和門市"
    private val note: String? = "無"
    private val paymentStatus: Int = 0



    fun addMockData(){
        val orderList: MutableList<Order> = mutableListOf()
        orderList.add(Order(
                orderId, orderTime, userId, products, price, phone, delivery,note,paymentStatus
        ))
        orderList.add(Order(
                orderId, orderTime, userId, products, price, phone, delivery,note,paymentStatus
        ))
        orderList.add(Order(
                orderId, orderTime, userId, products, price, phone, delivery,note,paymentStatus
        ))
        _order.value = orderList
    }

}
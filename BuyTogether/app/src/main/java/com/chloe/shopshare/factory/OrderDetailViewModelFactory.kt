package com.chloe.shopshare.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chloe.shopshare.data.MyOrderDetailKey
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.orderdetail.OrderDetailViewModel

class OrderDetailViewModelFactory(
    private val repository: Repository,
    private val orderDetail: MyOrderDetailKey
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(OrderDetailViewModel::class.java) ->
                    OrderDetailViewModel(repository, orderDetail)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
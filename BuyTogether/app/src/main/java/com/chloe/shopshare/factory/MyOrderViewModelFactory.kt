package com.chloe.shopshare.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.myhost.item.MyHostListViewModel
import com.chloe.shopshare.myorder.MyOrderType
import com.chloe.shopshare.myorder.item.MyOrderListViewModel

class MyOrderViewModelFactory(
    private val repository: Repository,
    private val myOrderType: MyOrderType
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MyOrderListViewModel::class.java) ->
                    MyOrderListViewModel(repository, myOrderType)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
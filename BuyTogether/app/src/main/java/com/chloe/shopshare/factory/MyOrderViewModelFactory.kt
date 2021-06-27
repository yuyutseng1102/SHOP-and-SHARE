package com.chloe.shopshare.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.myorder.MyOrderType
import com.chloe.shopshare.myorder.item.MyOrderListViewModel

@Suppress("UNCHECKED_CAST")
class MyOrderViewModelFactory(private val repository: Repository, private val type: MyOrderType) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MyOrderListViewModel::class.java) -> MyOrderListViewModel(repository, type)
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
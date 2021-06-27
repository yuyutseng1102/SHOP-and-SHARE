package com.chloe.shopshare.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.myrequest.MyRequestType
import com.chloe.shopshare.myrequest.item.MyRequestListViewModel

@Suppress("UNCHECKED_CAST")
class MyRequestViewModelFactory(private val repository: Repository, private val type: MyRequestType) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MyRequestListViewModel::class.java) -> MyRequestListViewModel(repository, type)
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
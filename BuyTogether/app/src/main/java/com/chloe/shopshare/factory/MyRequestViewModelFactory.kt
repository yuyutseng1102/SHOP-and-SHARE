package com.chloe.shopshare.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.myrequest.MyRequestType
import com.chloe.shopshare.myrequest.item.MyRequestListViewModel

class MyRequestViewModelFactory(
    private val repository: Repository,
    private val myRequestType: MyRequestType
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MyRequestListViewModel::class.java) ->
                    MyRequestListViewModel(repository, myRequestType)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
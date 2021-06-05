package com.chloe.shopshare.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.myhost.MyHostType
import com.chloe.shopshare.myhost.item.MyHostListViewModel

class MyHostViewModelFactory(
    private val repository: Repository,
    private val myHostType: MyHostType
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MyHostListViewModel::class.java) ->
                    MyHostListViewModel(repository, myHostType)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
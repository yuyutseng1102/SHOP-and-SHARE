package com.chloe.shopshare.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chloe.shopshare.data.Request
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.host.HostViewModel

@Suppress("UNCHECKED_CAST")
class HostViewModelFactory(private val repository: Repository, private val request: Request?) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(HostViewModel::class.java) -> HostViewModel(repository, request)
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}

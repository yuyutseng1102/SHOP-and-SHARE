package com.chloe.shopshare.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chloe.shopshare.manage.ManageViewModel
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.detail.DetailViewModel
import com.chloe.shopshare.host.HostViewModel
import com.chloe.shopshare.request.RequestViewModel
import com.chloe.shopshare.requestdetail.RequestDetailViewModel

@Suppress("UNCHECKED_CAST")
class ShopViewModelFactory(
        private val repository: Repository,
        private val id: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(DetailViewModel::class.java) ->
                        DetailViewModel(repository, id)
                    isAssignableFrom(ManageViewModel::class.java) ->
                        ManageViewModel(repository, id)
                    isAssignableFrom(RequestDetailViewModel::class.java) ->
                        RequestDetailViewModel(repository, id)
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}
package com.chloe.shopshare.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chloe.shopshare.manage.ManageViewModel
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.detail.DetailViewModel

@Suppress("UNCHECKED_CAST")
class ShopViewModelFactory(
        private val repository: Repository,
        private val shopId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(DetailViewModel::class.java) ->
                        DetailViewModel(repository, shopId)
                    isAssignableFrom(ManageViewModel::class.java) ->
                        ManageViewModel(repository, shopId)


                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}
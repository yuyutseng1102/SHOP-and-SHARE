package com.chloe.shopshare.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.Product
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.detail.dialog.DetailOptionViewModel
import com.chloe.shopshare.detail.dialog.ProductListViewModel
import com.chloe.shopshare.participate.ParticipateViewModel

@Suppress("UNCHECKED_CAST")
class ParticipateViewModelFactory(
    private val repository: Repository,
    private val collection: Shop,
    private val product: List<Product>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(ProductListViewModel::class.java) ->
                    ProductListViewModel(repository, collection, product)

                isAssignableFrom(ParticipateViewModel::class.java) ->
                    ParticipateViewModel(repository, collection, product)

                isAssignableFrom(DetailOptionViewModel::class.java) ->
                    DetailOptionViewModel(repository, collection, product)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
package com.chloe.buytogether.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chloe.buytogether.data.Collections
import com.chloe.buytogether.data.Product
import com.chloe.buytogether.data.source.Repository
import com.chloe.buytogether.detail.dialog.DetailOptionDialog
import com.chloe.buytogether.detail.dialog.DetailOptionViewModel
import com.chloe.buytogether.detail.dialog.ProductListViewModel
import com.chloe.buytogether.participate.ParticipateViewModel

@Suppress("UNCHECKED_CAST")
class ParticipateViewModelFactory(
    private val repository: Repository,
    private val collection: Collections,
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
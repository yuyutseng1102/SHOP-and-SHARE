package com.chloe.shopshare.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chloe.shopshare.data.Cart
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.detail.dialog.VariationViewModel
import com.chloe.shopshare.detail.dialog.CartViewModel
import com.chloe.shopshare.order.OrderViewModel

@Suppress("UNCHECKED_CAST")
class CartViewModelFactory(
    private val repository: Repository,
    private val cart: Cart
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(CartViewModel::class.java) ->
                    CartViewModel(repository, cart)

                isAssignableFrom(OrderViewModel::class.java) ->
                    OrderViewModel(repository, cart)

                isAssignableFrom(VariationViewModel::class.java) ->
                    VariationViewModel(repository, cart)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
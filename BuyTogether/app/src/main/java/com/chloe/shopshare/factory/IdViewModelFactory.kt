package com.chloe.shopshare.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.detail.DetailViewModel
import com.chloe.shopshare.manage.ManageViewModel

@Suppress("UNCHECKED_CAST")
class IdViewModelFactory(
    private val repository: Repository,
    private val id: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(DetailViewModel::class.java) ->
                    DetailViewModel(repository, id)


                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
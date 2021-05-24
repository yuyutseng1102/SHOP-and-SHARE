package com.chloe.shopshare.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chloe.shopshare.collection.manage.CollectionManageViewModel
import com.chloe.shopshare.data.Collections
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.detail.DetailViewModel

@Suppress("UNCHECKED_CAST")
class CollectViewModelFactory(
        private val repository: Repository,
        private val collection:Collections
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(CollectionManageViewModel::class.java) ->
                        CollectionManageViewModel(repository, collection)

                    isAssignableFrom(DetailViewModel::class.java) ->
                        DetailViewModel(repository, collection)

                    isAssignableFrom(DetailViewModel::class.java) ->
                        DetailViewModel(repository, collection)




                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}
package com.chloe.buytogether.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chloe.buytogether.collection.manage.CollectionManageViewModel
import com.chloe.buytogether.data.Collections
import com.chloe.buytogether.data.source.Repository
import com.chloe.buytogether.detail.DetailViewModel
import com.chloe.buytogether.detail.dialog.DetailOptionViewModel

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
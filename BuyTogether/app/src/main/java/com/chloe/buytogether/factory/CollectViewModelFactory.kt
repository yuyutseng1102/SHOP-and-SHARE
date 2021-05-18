package com.chloe.buytogether.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chloe.buytogether.collection.manage.CollectionManageViewModel
import com.chloe.buytogether.data.Collections
import com.chloe.buytogether.data.source.Repository

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
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}
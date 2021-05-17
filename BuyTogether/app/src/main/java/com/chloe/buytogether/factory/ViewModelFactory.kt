package com.chloe.buytogether.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chloe.buytogether.collection.CollectionManageViewModel
import com.chloe.buytogether.collection.CollectionViewModel
import com.chloe.buytogether.data.source.Repository
import com.chloe.buytogether.gather.GatherViewModel
import com.chloe.buytogether.gather.item.GatherConditionViewModel
import com.chloe.buytogether.gather.item.GatherOptionViewModel
import com.chloe.buytogether.home.item.HomeCollectViewModel
import com.chloe.buytogether.home.item.HomePageViewModel

/**
 *
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
        private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(HomePageViewModel::class.java) ->
                        HomePageViewModel(repository)

                    isAssignableFrom(HomeCollectViewModel::class.java) ->
                        HomeCollectViewModel(repository)

                    isAssignableFrom(GatherViewModel::class.java) ->
                        GatherViewModel(repository)

                    isAssignableFrom(GatherConditionViewModel::class.java) ->
                        GatherConditionViewModel(repository)

                    isAssignableFrom(CollectionViewModel::class.java) ->
                        CollectionViewModel(repository)

                    isAssignableFrom(CollectionManageViewModel::class.java) ->
                        CollectionManageViewModel(repository)

                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}

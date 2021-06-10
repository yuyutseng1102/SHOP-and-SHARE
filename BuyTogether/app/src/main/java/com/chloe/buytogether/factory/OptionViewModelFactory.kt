package com.chloe.buytogether.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chloe.buytogether.data.source.Repository
import com.chloe.buytogether.host.item.GatherOptionViewModel

@Suppress("UNCHECKED_CAST")
class OptionViewModelFactory(
        private val repository: Repository,
        private val option: List<String>?,
        private val isStandard: Boolean
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(GatherOptionViewModel::class.java) ->
                        GatherOptionViewModel(repository, option, isStandard)
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}
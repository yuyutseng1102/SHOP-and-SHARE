package com.chloe.shopshare.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.host.item.HostVariationViewModel

@Suppress("UNCHECKED_CAST")
class OptionViewModelFactory(
    private val repository: Repository,
    private val option: List<String>?,
    private val isStandard: Boolean
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(HostVariationViewModel::class.java) ->
                    HostVariationViewModel(repository, option, isStandard)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
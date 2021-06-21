package com.chloe.shopshare.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.result.ResultViewModel

@Suppress("UNCHECKED_CAST")
class ResultViewModelFactory(
    private val repository: Repository,
    private val category: Int,
    private val country: Int
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(ResultViewModel::class.java) ->
                    ResultViewModel(repository, category, country)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
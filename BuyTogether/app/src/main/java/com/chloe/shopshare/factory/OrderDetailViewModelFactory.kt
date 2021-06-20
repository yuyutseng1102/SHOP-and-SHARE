package com.chloe.shopshare.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chloe.shopshare.data.Track
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.track.TrackViewModel

class OrderDetailViewModelFactory(
    private val repository: Repository,
    private val orderDetail: Track
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(TrackViewModel::class.java) ->
                    TrackViewModel(repository, orderDetail)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
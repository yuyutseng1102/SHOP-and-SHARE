package com.chloe.buytogether

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.buytogether.data.source.Repository
import com.chloe.buytogether.util.CurrentFragmentType

class MainViewModel(private val repository: Repository) : ViewModel() {

    // Record current fragment to support data binding
    val currentFragmentType = MutableLiveData<CurrentFragmentType>()

}
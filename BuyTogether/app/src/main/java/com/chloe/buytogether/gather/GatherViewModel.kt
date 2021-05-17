package com.chloe.buytogether.gather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.buytogether.data.Collections

class GatherViewModel: ViewModel() {

    private val _collection = MutableLiveData<Collections>()
    val collection: LiveData<Collections>
        get() =  _collection



}
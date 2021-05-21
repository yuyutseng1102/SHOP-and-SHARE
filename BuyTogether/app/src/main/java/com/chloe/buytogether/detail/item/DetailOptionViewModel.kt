package com.chloe.buytogether.detail.item

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.buytogether.R
import com.chloe.buytogether.data.Collections
import com.chloe.buytogether.data.source.Repository
import com.chloe.buytogether.detail.OptionSelector
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class DetailOptionViewModel(private val repository: Repository,
                            private val args: Collections
): ViewModel() {

    private val _collection = MutableLiveData<Collections>().apply {
        value = args
    }

    val collection: LiveData<Collections>
        get() = _collection

    private val _option = MutableLiveData<List<String>>().apply {
        value = _collection.value?.option
    }

    val option: LiveData<List<String>>
        get() = _option

    //select option
    val selectedChip = MutableLiveData<Int>()
    lateinit var optionSelected: String
    fun getOption() {
        if (_option.value != null && selectedChip.value != null) {
            optionSelected = _option.value!![selectedChip.value!!]
        }
    }
}
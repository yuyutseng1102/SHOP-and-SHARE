package com.chloe.buytogether.gather.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.chloe.buytogether.R
import com.chloe.buytogether.data.source.Repository
import com.chloe.buytogether.gather.CategoryType
import com.chloe.buytogether.util.Util

class GatherConditionViewModel(private val repository: Repository): ViewModel() {

    val conditionType = MutableLiveData<Int?>()
    val deadLine = MutableLiveData<Long?>()
    val condition = MutableLiveData<String?>()

    val isTimeChecked = MutableLiveData<Boolean>()
    val isConditionChecked = MutableLiveData<Boolean>()

    //select gather condition
    val selectedConditionPosition = MutableLiveData<Int>()

    val conditionMethod: LiveData<CategoryType> = Transformations.map(selectedConditionPosition) {
        CategoryType.values()[it]
    }

}
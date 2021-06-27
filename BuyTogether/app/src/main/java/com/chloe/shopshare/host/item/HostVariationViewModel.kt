package com.chloe.shopshare.host.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.network.LoadApiStatus

class HostVariationViewModel(oldOption: List<String>?, oldIsStandard: Boolean) : ViewModel() {

    private val _option = MutableLiveData<List<String>>()
    val option: LiveData<List<String>>
        get() = _option

    val optionItem = MutableLiveData<String?>()

    val isStandard = MutableLiveData<Boolean>()

    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status

    private var optionList: MutableList<String>? = mutableListOf()


    init {
        oldOption?.let {
            _option.value = it
            for (item in _option.value!!) {
                optionList?.add(item)
            }
        }
        isStandard.value = oldIsStandard
    }

    fun addOption() {
        if (!optionItem.value.isNullOrEmpty()) {
            optionList =
                when (_option.value.isNullOrEmpty()) {
                    true -> mutableListOf()
                    else -> _option.value?.toMutableList()
                }
            optionItem.value?.let { optionList?.add(it) }
            optionList?.let { _option.value = it }
        }
    }

    fun removeOption(optionItem: String) {
        _option.value?.let {
            val optionList = it.toMutableList()
            optionList.remove(optionItem)
            _option.value = optionList
        }
    }

    fun clearEditOption() {
        optionItem.value = null
    }

    fun optionDone() {
        _status.value = LoadApiStatus.LOADING
        when (isStandard.value) {
            false -> {
                if (!optionItem.value.isNullOrEmpty()) {
                    optionList = mutableListOf(optionItem.value ?: "")
                    optionList?.let {
                        _option.value = it
                        _status.value = LoadApiStatus.DONE
                    }
                }
            }
            else -> {
                if (!optionItem.value.isNullOrEmpty()) {
                    addOption()
                }
                if (!_option.value.isNullOrEmpty()) {
                    _status.value = LoadApiStatus.DONE
                } else {
                    _status.value = LoadApiStatus.LOADING
                }
            }
        }
    }

    val optionToDisplay = MutableLiveData<String>()

    fun showOption() {
        optionToDisplay.value =
            when (isStandard.value) {
                false -> displayScope(_option.value)
                true -> displayVariance(_option.value)
                else -> ""
            }
    }

    private fun displayScope(option: List<String>?): String {
        return when (option) {
            null -> ""
            else -> option[0]
        }
    }

    private fun displayVariance(option: List<String>?): String {
        return when (option) {
            null -> ""
            else -> {
                when {
                    option.size > 2 -> "${option[0]}+${option[1]}...共${option.size}項"
                    option.size == 2 -> "${option[0]}+${option[1]}"
                    option.size == 1 -> option[0]
                    else -> ""
                }
            }
        }
    }
}
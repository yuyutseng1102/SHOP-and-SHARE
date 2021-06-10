package com.chloe.shopshare.host.item

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.network.LoadApiStatus

class GatherOptionViewModel(private val repository: Repository,var oldOption: List<String>?,oldIsStandard: Boolean): ViewModel() {

    private val _option = MutableLiveData<List<String>>()
    val option: LiveData<List<String>>
        get() = _option

    val optionItem = MutableLiveData<String?>()

    //select option method
    val isStandard = MutableLiveData<Boolean>()

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private var optionList: MutableList<String>? = mutableListOf()


    init {
        oldOption?.let{
            _option.value = it
            for (item in _option.value!!) {
                optionList?.add(item)
            }
//            oldOption = null
        }
        isStandard.value = oldIsStandard
    }

    fun addOption() {
        if (!optionItem.value.isNullOrEmpty()) {
            _option.value?.let {
                optionList = it.toMutableList()
            }
            if (_option.value.isNullOrEmpty()) {
                optionList = mutableListOf()
            }
            optionList?.add(optionItem.value!!)
            _option.value = optionList!!
        }
    }
//
//
//    fun addOption() {
//        if (!optionItem.value.isNullOrEmpty()) {
//            _option.value?.let {
//                optionList = it.toMutableList()
//            }
//            if(_option.value.isNullOrEmpty()){
//                optionList = mutableListOf()
//            }
//            optionList?.add(optionItem.value!!)
//            _option.value = optionList!!
//        }
//    }


    fun removeOption(optionItem : String) {
        _option.value?.let {
            Log.d("Chloe","the option cleared is $optionItem")
            val optionList = it.toMutableList()
            optionList.remove(optionItem)
            _option.value = optionList
            Log.d("Chloe","the optionList is ${_option.value}")
        }
    }

    fun clearEditOption() {
        optionItem.value = null
    }

    fun optionDone() {
        Log.d("Chloe","the optionList is ${_option.value}")
        // 將輸入欄加入list中
        //情況1: 自訂義輸入
        if (isStandard.value == false) {
            //先清空舊的選項
            if (!optionItem.value.isNullOrEmpty()) {
                optionList = mutableListOf(optionItem.value ?: "")
//                    optionList?.add(optionItem.value!!)
                if (optionList != null) {
                    _option.value = optionList!!
                    _status.value = LoadApiStatus.DONE
                } else {
                    _status.value = LoadApiStatus.LOADING
                }
            }
            //將輸入欄加入list中

        } else {
            //情況2:新增選項規格
            //先確定選項非為空
            if (!optionItem.value.isNullOrEmpty()) {
                Log.d("Chloe","!optionItem.value.isNullOrEmpty()")
                addOption()}
            if (!_option.value.isNullOrEmpty()){
                _status.value = LoadApiStatus.DONE
            }else{
                _status.value = LoadApiStatus.LOADING
            }
        }
    }

    val optionToDisplay = MutableLiveData<String>()

    fun showOption() {

        optionToDisplay.value =
            when (isStandard.value) {
                false -> {
                    if (_option.value != null) {
                        _option.value!![0]
                    } else {
                        ""
                    }
                }
                true -> if (_option.value != null) {
                    if (_option.value!!.size > 2) {
                        "${_option.value!![0]}+${_option.value!![1]}...共${_option.value!!.size}項"
                    } else if (_option.value!!.size == 2) {
                        "${_option.value!![0]}+${_option.value!![1]}"
                    } else if (_option.value!!.size == 1) {
                        _option.value!![0]
                    } else {
                        ""
                    }
                }else {
                    ""
                }
                else -> ""
            }
    }
}
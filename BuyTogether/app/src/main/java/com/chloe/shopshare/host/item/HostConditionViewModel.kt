package com.chloe.shopshare.host.item

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.R
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.ext.toDisplayYearDateFormat
import com.chloe.shopshare.host.ConditionType
import com.chloe.shopshare.network.LoadApiStatus
import com.chloe.shopshare.util.Util
import com.google.android.material.datepicker.MaterialDatePicker

class HostConditionViewModel : ViewModel() {

    val deadLine = MutableLiveData<Long?>()
    val condition = MutableLiveData<Int?>()
    val content = MutableLiveData<String?>()

    private val _shop = MutableLiveData<Shop>()
    val shop: LiveData<Shop>
        get() =  _shop

    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status

    init{
        condition.value = 0
    }

    val isTimeChecked = MutableLiveData<Boolean>()
    val isConditionChecked = MutableLiveData<Boolean>()

    val datePicker =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setTheme(R.style.DatePicker)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

    fun pickDate(){ deadLine.value = datePicker.selection }

    val selectedConditionPosition = MutableLiveData<Int?>()

    private val conditionMethod: LiveData<ConditionType> = Transformations.map(selectedConditionPosition) {
        it?.let { ConditionType.values()[it] }
    }

    val hint = MutableLiveData<String?>()

    init {
        condition.value = null
    }

    fun hintToShow(){
        hint.value=
            when(conditionMethod.value) {
                ConditionType.BY_PRICE -> Util.getString(R.string.hint_price)
                ConditionType.BY_QUANTITY -> Util.getString(R.string.hint_quantity)
                ConditionType.BY_MEMBER -> Util.getString(R.string.hint_member)
                else -> ""
            }
    }

    fun checkCondition() {

        condition.value = content.value?.toInt()

        if (isTimeChecked.value == false && isConditionChecked.value == false){
            _status.value = LoadApiStatus.LOADING
            Log.d("Chloe", "The input is invalid")
        }else if(isTimeChecked.value == true && deadLine.value==null) {
            _status.value = LoadApiStatus.LOADING
            Log.d("Chloe", "The input is invalid")
        }else if (isConditionChecked.value == true && (condition.value==null||condition.value==0)){
            _status.value = LoadApiStatus.LOADING
            Log.d("Chloe", "The input is invalid")
        }else {
            _status.value = LoadApiStatus.DONE

        }
    }

    fun setCondition(){
        Log.d("Chloe","setting condition is success")
    }

    private val deadLineToDisplay : String?
        get()= "預計${deadLine.value?.toDisplayYearDateFormat()}收團"
    private val conditionToDisplay : String?
        get()=
            when (selectedConditionPosition.value){
                0-> "滿額NT$${condition.value}止"
                1-> "徵滿${condition.value}份止"
                2-> "徵滿${condition.value}人止"
                else -> ""
            }

    val conditionShow = MutableLiveData<String?>()

    fun showCondition(){
        if (deadLine.value == null){
            conditionShow.value = conditionToDisplay
        }else if(condition.value == null){
            conditionShow.value = deadLineToDisplay
        }else{conditionShow.value = deadLineToDisplay+"或"+conditionToDisplay}
    }

}
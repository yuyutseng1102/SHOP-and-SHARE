package com.chloe.buytogether.gather


import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.chloe.buytogether.R
import com.chloe.buytogether.data.Collections
import com.chloe.buytogether.data.Order
import com.chloe.buytogether.data.source.Repository
import com.chloe.buytogether.ext.toDisplayFormat
import com.chloe.buytogether.network.LoadApiStatus
import com.chloe.buytogether.util.Util.getString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class GatherViewModel(private val repository: Repository) : ViewModel() {

    val userId = 193798L

    private val _collection = MutableLiveData<Collections>()
    val collection: LiveData<Collections>
        get() =  _collection

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status


    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    //gather information

    val mainImage = MutableLiveData<String?>()
    val image = MutableLiveData<List<String>>()
    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val category = MutableLiveData<String>()
    val country = MutableLiveData<String>()
    val source = MutableLiveData<String>()
    val isStandard = MutableLiveData<Boolean>()
    val option = MutableLiveData<List<String>>()
    val deliveryMethod = MutableLiveData<String>()
    val conditionType = MutableLiveData<Int?>()
    val deadLine = MutableLiveData<Long?>()
    val condition = MutableLiveData<Int?>()
    val conditionShow = MutableLiveData<String?>()
    val optionShow = MutableLiveData<String>()

    private val _isInvalid = MutableLiveData<Int>()
    val isInvalid: LiveData<Int>
        get() =  _isInvalid

    private val _isConditionDone = MutableLiveData<Boolean>()
    val isConditionDone: LiveData<Boolean>
        get() =  _isConditionDone


    init {
        image.value = listOf("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRn2O7C-ZPE_D1GshuECEOcxjqIMmnXSxo0fA&usqp=CAU")
        _isInvalid.value = null
        _isConditionDone.value = false
    }

    //select gather method
    val selectedMethodRadio = MutableLiveData<Int>()
    private val method : String
        get() = when (selectedMethodRadio.value) {
            R.id.radio_agent -> getString(R.string.purchase_agent)
            R.id.radio_gather -> getString(R.string.purchase_gather)
            R.id.radio_private -> getString(R.string.purchase_private)
            else -> ""
        }

    //select gather category
    val selectedCategoryPosition = MutableLiveData<Int>()

    val categoryType: LiveData<CategoryType> = Transformations.map(selectedCategoryPosition) {
        CategoryType.values()[it]
    }
    fun selectCategory(){
        category.value =
        when(categoryType.value) {
            CategoryType.WOMAN -> getString(R.string.woman)
            CategoryType.MAN -> getString(R.string.man)
            CategoryType.CHILD -> getString(R.string.child)
            CategoryType.SHOES_BAG -> getString(R.string.shoes_bag)
            CategoryType.MAKEUP -> getString(R.string.makeup)
            CategoryType.HEALTH -> getString(R.string.health)
            CategoryType.FOOD -> getString(R.string.food)
            CategoryType.LIVING -> getString(R.string.living)
            CategoryType.APPLIANCE -> getString(R.string.appliance)
            CategoryType.PET -> getString(R.string.pet)
            CategoryType.STATIONARY -> getString(R.string.stationary)
            CategoryType.SPORT -> getString(R.string.sport)
            CategoryType.COMPUTER -> getString(R.string.computer)
            CategoryType.TICKET -> getString(R.string.ticket)
            CategoryType.OTHER -> getString(R.string.other)
            else -> ""
        }
    }

    //select gather country
    val selectedCountryPosition = MutableLiveData<Int>()

    val countryType: LiveData<CountryType> = Transformations.map(selectedCountryPosition) {
        CountryType.values()[it]
    }
    fun selectCountry(){
        country.value =
                when(countryType.value) {
                    CountryType.TAIWAN -> getString(R.string.taiwan)
                    CountryType.JAPAN -> getString(R.string.japan)
                    CountryType.KOREA -> getString(R.string.korea)
                    CountryType.CHINA -> getString(R.string.china)
                    CountryType.USA -> getString(R.string.usa)
                    CountryType.CANADA -> getString(R.string.canada)
                    CountryType.EU -> getString(R.string.eu)
                    CountryType.AUSTRALIA -> getString(R.string.australia)
                    CountryType.SOUTH_EAST_ASIA -> getString(R.string.south_east_asia)
                    CountryType.OTHER -> getString(R.string.other)
                    else -> ""
                }
    }

    fun readyToPost() {


        //紅字訊息提醒

        _isInvalid.value =
            when {
                selectedMethodRadio.value == 0 -> INVALID_FORMAT_METHOD_EMPTY
                image.value.isNullOrEmpty() -> INVALID_FORMAT_IMAGE_EMPTY
                title.value.isNullOrEmpty() -> INVALID_FORMAT_TITLE_EMPTY
                description.value.isNullOrEmpty() -> INVALID_FORMAT_DESCRIPTION_EMPTY
                source.value.isNullOrEmpty() -> INVALID_FORMAT_SOURCE_EMPTY
                option.value.isNullOrEmpty() -> INVALID_FORMAT_OPTION_EMPTY
                deliveryMethod.value.isNullOrEmpty() -> INVALID_FORMAT_DELIVERY_EMPTY
                conditionShow.value.isNullOrEmpty() -> INVALID_FORMAT_CONDITION_EMPTY
                else -> null
            }

        //是否有東西尚未輸入

        if (_isInvalid.value != null){
            _status.value = LoadApiStatus.LOADING
            Log.d("Chloe","The input is invalid, the value is ${_isInvalid.value}")
        }else{
            _status.value = LoadApiStatus.DONE
        }

        Log.d("Chloe","The selectedMethodRadio.value is valid ${selectedMethodRadio.value}")
        Log.d("Chloe","The image.value is valid${image.value}")
        Log.d("Chloe","The title.value is valid${title.value}")
        Log.d("Chloe","The category.value is valid${category.value}")
        Log.d("Chloe","The country.value is valid${country.value}")
        Log.d("Chloe","The  description.value is valid${description.value}")
        Log.d("Chloe","The source.value is valid${source.value}")
        Log.d("Chloe","The isCustom.value is valid${isStandard.value}")
        Log.d("Chloe","The deliveryMethod.value is valid${deliveryMethod.value}")
        Log.d("Chloe","The condition.value is valid${condition.value}")

    }


    fun postGatherCollection(){

        _collection.value = Collections(
            id = 0,
            userId = userId,
            time = Calendar.getInstance().timeInMillis,
            method = method,
            mainImage = image.value?.get(0) ?:"",
            image = image.value?: listOf(),
            title = title.value?:"",
            description = description.value?:"",
            category = category.value?:"",
            country = country.value?:"",
            source = source.value?:"",
            isStandard = isStandard.value?:false,
            option = option.value?: listOf(),
            deliveryMethod = deliveryMethod.value?:"",
            conditionType = conditionType.value,
            deadLine = deadLine.value,
            condition = condition.value,
            status = 0,
            order = listOf()
        )
        Log.d("Chloe","The collection posted is ${_collection.value}")
    }


    fun checkCondition(){
        Log.d("Chloe","_isConditionDone= ${_isConditionDone.value}")
        _isConditionDone.value = !(deadLine.value == null&&condition.value==null)
    }


    companion object {
        const val INVALID_FORMAT_METHOD_EMPTY       = 0x11
        const val INVALID_FORMAT_IMAGE_EMPTY        = 0x12
        const val INVALID_FORMAT_TITLE_EMPTY        = 0x13
        const val INVALID_FORMAT_DESCRIPTION_EMPTY  = 0x14
        const val INVALID_FORMAT_CATEGORY_EMPTY     = 0x15
        const val INVALID_FORMAT_COUNTRY_EMPTY      = 0x16
        const val INVALID_FORMAT_SOURCE_EMPTY          = 0x17
        const val INVALID_FORMAT_OPTION_EMPTY       = 0x18
        const val INVALID_FORMAT_DELIVERY_EMPTY     = 0x19
        const val INVALID_FORMAT_CONDITION_EMPTY    = 0x20
        const val NO_ONE_KNOWS                      = 0x21
    }

}



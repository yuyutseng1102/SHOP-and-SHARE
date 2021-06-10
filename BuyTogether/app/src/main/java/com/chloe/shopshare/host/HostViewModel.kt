package com.chloe.shopshare.host


import android.net.Uri
import android.util.Log
import android.widget.TextView
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.Notify
import com.chloe.shopshare.data.Request
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import com.chloe.shopshare.util.UserManager
import com.chloe.shopshare.data.Result
import com.chloe.shopshare.ext.toDisplayNotifyContent
import com.chloe.shopshare.ext.toDisplayNotifyMessage
import com.chloe.shopshare.notify.NotifyType
import com.chloe.shopshare.util.ServiceLocator.repository

class HostViewModel(private val repository: Repository,private val argument: Request?) : ViewModel() {

//    val userId = "193798"

    private val _request = MutableLiveData<Request>().apply {
        value = argument
    }
    val request: LiveData<Request>
        get() =  _request

    private val _shop = MutableLiveData<Shop>()
    val shop: LiveData<Shop>
        get() =  _shop

    private val _shopId = MutableLiveData<String?>()
    val shopId: LiveData<String?>
        get() =  _shopId

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    private val _leave = MutableLiveData<Boolean>()
    val leave: LiveData<Boolean>
        get() = _leave

    private val _uploadDone = MutableLiveData<Boolean>()
    val uploadDone: LiveData<Boolean>
        get() = _uploadDone
    private val _editShopDone = MutableLiveData<Boolean>()
    val editShopDone: LiveData<Boolean>
        get() = _editShopDone
    private val _notifyRequestRequesterDone = MutableLiveData<Boolean>()
    val notifyRequestRequesterDone: LiveData<Boolean>
        get() = _notifyRequestRequesterDone
    private val _notifyRequestMemberDone = MutableLiveData<Boolean>()
    val notifyRequestMemberDone: LiveData<Boolean>
        get() = _notifyRequestMemberDone
    private val _updateRequestHostDone = MutableLiveData<Boolean>()
    val updateRequestHostDone: LiveData<Boolean>
        get() = _updateRequestHostDone


    private val _image = MutableLiveData<List<String>>()
    val image: LiveData<List<String>>
        get() =  _image

    private val _postImage = MutableLiveData<List<String>>()
    val postImage: LiveData<List<String>>
        get() =  _postImage



    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    //gather information

    val mainImage = MutableLiveData<String?>()
    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val category = MutableLiveData<Int>()
    val country = MutableLiveData<Int>()
    val source = MutableLiveData<String>()
    val isStandard = MutableLiveData<Boolean>()
    val option = MutableLiveData<List<String>>()
    val deliveryMethod = MutableLiveData<List<Int>>()
    val conditionType = MutableLiveData<Int?>()
    val deadLine = MutableLiveData<Long?>()
    val condition = MutableLiveData<Int?>()
    val conditionShow = MutableLiveData<String?>()
    val optionShow = MutableLiveData<String>()

    val imageUri = MutableLiveData<String>()



    private val _isInvalid = MutableLiveData<Int>()
    val isInvalid: LiveData<Int>
        get() =  _isInvalid

    private val _isOptionDone = MutableLiveData<Boolean>()
    val isOptionDone: LiveData<Boolean>
        get() =  _isOptionDone

    private val _isConditionDone = MutableLiveData<Boolean>()
    val isConditionDone: LiveData<Boolean>
        get() =  _isConditionDone

    val initCategoryPosition = MutableLiveData<Int>()
    val initCountryPosition = MutableLiveData<Int>()


    init {
        _status.value = LoadApiStatus.DONE
        _isInvalid.value = null
        _isOptionDone.value = false
        _isConditionDone.value = false

        _request.value?.let{
//            _image.value = it.image
            title.value = it.title
            description.value = it.description
            source.value = it.source
            initCategoryPosition.value = convertCategoryToPosition(it.category)
            initCountryPosition.value = convertCountryToPosition(it.country)
        }


    }

    private fun convertCategoryToPosition(category:Int) : Int {

        var position : Int = 0

        for (type in CategoryType.values()) {
            if (type.category == category) {
                position =  type.positionOnSpinner
            }
        }
        return position
    }
    private fun convertCountryToPosition(country:Int) : Int {

        var position : Int = 0

        for (type in CountryType.values()) {
            if (type.country == country) {
                position =  type.positionOnSpinner
            }
        }
        return position
    }

    //select gather method
    val selectedMethodRadio = MutableLiveData<Int>()
    private val method : Int
        get() = when (selectedMethodRadio.value) {
            R.id.radio_agent -> ShopType.AGENT.shopType
            R.id.radio_gather -> ShopType.GATHER.shopType
            R.id.radio_private -> ShopType.PRIVATE.shopType
            else -> 1
        }

    //add uri downloaded from storage and display on layout
    var imageList : MutableList<String> = mutableListOf()

    fun pickImages(uri:Uri){
        imageList =
            if (image.value!= null){
                image.value?.toMutableList()?: mutableListOf()
            }else{
                mutableListOf()
            }
        imageList.add(uri.toString())
        _image.value = imageList
        Log.d("Chloe","imageList add $uri , the list change to ${_image.value} ")
    }


    private fun postShop(shop: Shop) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING
            val result = repository.postShop(shop)

            when (result) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    Log.d("Chloe","result.data.number")
                    _shopId.value = result.data.number
                    leave(true)
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    _shopId.value = null
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    _shopId.value = null
                }
                else -> {
                    _error.value = MyApplication.instance.getString(R.string.result_fail)
                    _status.value = LoadApiStatus.ERROR
                    _shopId.value = null
                }
            }
        }
    }

    fun updateRequestHost(requestId: String, shopId: String , hostId: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.updateRequestHost(requestId,shopId,hostId)) {
                    is Result.Success -> {
                        _error.value = null
                        _status.value = LoadApiStatus.DONE
                        _updateRequestHostDone.value = true

                    }
                    is Result.Fail -> {
                        _error.value = result.error
                        _status.value = LoadApiStatus.ERROR
                        _updateRequestHostDone.value = null
                    }
                    is Result.Error -> {
                        _error.value = result.exception.toString()
                        _status.value = LoadApiStatus.ERROR
                        _updateRequestHostDone.value = null
                    }
                    else -> {
                        _error.value = MyApplication.instance.getString(R.string.result_fail)
                        _status.value = LoadApiStatus.ERROR
                        _updateRequestHostDone.value = null
                    }
                }
        }
    }

    fun editNotify(){
        _shopId.value?.let {
            val notifyToRequester = Notify(
                shopId = it,
                requestId = _request.value?.id,
                type = NotifyType.REQUEST_SUCCESS_REQUESTER.type,
                title = NotifyType.REQUEST_SUCCESS_REQUESTER.title,
                content = NotifyType.REQUEST_SUCCESS_REQUESTER.toDisplayNotifyContent(
                    _request.value!!.title
                )
            )

            val notifyToRequesterMember = Notify(
                shopId = it,
                requestId = _request.value?.id,
                type = NotifyType.REQUEST_SUCCESS_MEMBER.type,
                title = NotifyType.REQUEST_SUCCESS_MEMBER.title,
                content = NotifyType.REQUEST_SUCCESS_MEMBER.toDisplayNotifyContent(
                    _request.value!!.title
                )
            )

            _request.value?.let {
                postNotifyToRequester(it.userId, notifyToRequester)
                postRequestNotifyToMember(notifyToRequesterMember)
            }

            }
        }


    private fun postNotifyToRequester(requesterId: String, notify: Notify) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.postNotifyToHost(requesterId, notify)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    _notifyRequestRequesterDone.value = true
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    _notifyRequestRequesterDone.value = null
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    _notifyRequestRequesterDone.value = null
                }
                else -> {
                    _error.value = MyApplication.instance.getString(R.string.result_fail)
                    _status.value = LoadApiStatus.ERROR
                    _notifyRequestRequesterDone.value = null
                }
            }
        }
    }

    private fun postRequestNotifyToMember(notify: Notify) {
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            when (val result = repository.postRequestNotifyToMember(notify)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    _notifyRequestMemberDone.value = true
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    _notifyRequestMemberDone.value = null
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    _notifyRequestMemberDone.value = null
                }
                else -> {
                    _error.value = MyApplication.instance.getString(R.string.result_fail)
                    _status.value = LoadApiStatus.ERROR
                    _notifyRequestMemberDone.value = null
                }
            }
        }
    }




    fun uploadImages(uriList: List<String>) {
        val list : MutableList<String> = mutableListOf()


        Log.d("Chloe","uriList = ${uriList} ")

        val totalCount = uriList.size
        var count = 0

        for (item in uriList) {
            _status.value = LoadApiStatus.LOADING

            coroutineScope.launch {

                _status.value = LoadApiStatus.LOADING
                val result = repository.uploadImage(item.toUri(), "host")

                    when (result) {
                    is Result.Success -> {
                        _error.value = null
                        _status.value = LoadApiStatus.DONE
                        Log.d("Chloe","download uri is ${result.data}")
                        imageUri.value =result.data
                        list.add(imageUri.value!!)
                        count ++

//                        _uploadDone.value = (list.size == uriList.size && list.isNotEmpty())

                    }
                    is Result.Fail -> {
                        _error.value = result.error
                        _status.value = LoadApiStatus.ERROR
                        imageUri.value = null
                        count ++
                    }
                    is Result.Error -> {
                        _error.value = result.exception.toString()
                        _status.value = LoadApiStatus.ERROR
                        imageUri.value = null
                        count ++
                    }
                    else -> {
                        _error.value = MyApplication.instance.getString(R.string.result_fail)
                        _status.value = LoadApiStatus.ERROR
                        imageUri.value = null
                        count ++
                    }

                }

                if (count == totalCount){

                    _postImage.value = list
                    Log.d("Chloe","_postImage.value  = ${_postImage.value } ")
                    _uploadDone.value = true
                    Log.d("Chloe","_uploadDone.value = true")
                }






            }

        }


    }


    //delete photo
    fun removeImages(item:String){
        imageList.remove(item)
        _image.value = imageList
        Log.d("Chloe","imageList remove $item , the list change to ${_image.value} ")
    }
//    fun removeImages(item:String){
//        imageList.remove(item)
//        _image.value = imageList
//        Log.d("Chloe","imageList remove $item , the list change to ${_image.value} ")
//    }

    //select gather category
    val selectedCategoryPosition = MutableLiveData<Int>()

    val categoryType: LiveData<CategoryType> = Transformations.map(selectedCategoryPosition) {
        CategoryType.values()[it]
    }
    fun selectCategory(){
        category.value = categoryType.value?.category
        Log.d("Chloe","====Look at category ${category.value}==== ")
    }

    //select gather country
    val selectedCountryPosition = MutableLiveData<Int>()

    val countryType: LiveData<CountryType> = Transformations.map(selectedCountryPosition) {
        CountryType.values()[it]
    }
    fun selectCountry(){
        country.value = countryType.value?.country
        Log.d("Chloe","====Look at country ${country.value}==== ")
    }

    fun selectDelivery(delivery: Int){
        val deliveryList = deliveryMethod.value?.toMutableList() ?: mutableListOf()
        deliveryList.add(delivery)
        Log.d("Chloe","====deliveryList ${deliveryList}==== ")
        deliveryMethod.value = deliveryList
    }

    fun removeDelivery(delivery: Int){
        val deliveryList = deliveryMethod.value?.toMutableList() ?: mutableListOf()
        deliveryList.remove(delivery)
        Log.d("Chloe","====deliveryList ${deliveryList}==== ")
        deliveryMethod.value = deliveryList
    }

    lateinit var uriList : List<Uri>



    fun readyToPost() {


        //紅字訊息提醒
        _isInvalid.value =
            when {
                selectedMethodRadio.value == 0 -> INVALID_FORMAT_METHOD_EMPTY
                _image.value.isNullOrEmpty() -> INVALID_FORMAT_IMAGE_EMPTY
                title.value.isNullOrEmpty() -> INVALID_FORMAT_TITLE_EMPTY
                description.value.isNullOrEmpty() -> INVALID_FORMAT_DESCRIPTION_EMPTY
                source.value.isNullOrEmpty() -> INVALID_FORMAT_SOURCE_EMPTY
                option.value.isNullOrEmpty() -> INVALID_FORMAT_OPTION_EMPTY
                deliveryMethod.value.isNullOrEmpty() -> INVALID_FORMAT_DELIVERY_EMPTY
                conditionShow.value.isNullOrEmpty() -> INVALID_FORMAT_CONDITION_EMPTY
                else -> null
            }

        //是否有東西尚未輸入

//        if (_isInvalid.value != null){
//            _status.value = LoadApiStatus.LOADING
//            Log.d("Chloe","The input is invalid, the value is ${_isInvalid.value}")
//        }
//        else{
//            _status.value = LoadApiStatus.DONE
//        }

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
        UserManager.userId?.let {
            Log.d("Chloe","_postImage.value to post  ${_postImage.value}")
            _shop.value = Shop(
                userId = it,
                type = method,
                mainImage = _postImage.value?.get(0) ?:"",
                image =_postImage.value?: listOf(),
                title = title.value?:"",
                description = description.value?:"",
                category = category.value?:0,
                country = country.value?:0,
                source = source.value?:"",
                isStandard = isStandard.value?:false,
                option = option.value?: listOf(),
                deliveryMethod = deliveryMethod.value?: listOf(),
                conditionType = conditionType.value,
                deadLine = deadLine.value,
                condition = condition.value,
                status = 0,
                order = listOf()
            )
        }

        _shop.value?.let{
            postShop(it)
        }
        Log.d("Chloe","The collection posted is ${_shop.value}")
    }

    fun checkOption(){
        _isOptionDone.value = !(option.value.isNullOrEmpty())
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

    fun leave(needRefresh: Boolean = false) {
        _leave.value = needRefresh
    }

    fun onLeft() {
        _leave.value = null
    }

    fun onImageUploadDone(){
        _uploadDone.value = null
    }

}


//    fun pickImages(){
//        imageList =
//        if (image.value!= null){
//            image.value?.toMutableList()?: mutableListOf()
//        }else{
//            mutableListOf()
//        }
//        imageList.add(imageUri.value!!)
//        _image.value = imageList
//        Log.d("Chloe","imageList add $imageUri , the list change to ${_image.value} ")
//    }
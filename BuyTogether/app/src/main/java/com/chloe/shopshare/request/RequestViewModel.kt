package com.chloe.shopshare.request

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.Request
import com.chloe.shopshare.data.Result
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.host.CategoryType
import com.chloe.shopshare.host.CountryType
import com.chloe.shopshare.host.HostViewModel
import com.chloe.shopshare.network.LoadApiStatus
import com.chloe.shopshare.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RequestViewModel(private val repository: Repository) : ViewModel() {

    private val _request = MutableLiveData<Request>()
    val request: LiveData<Request>
        get() = _request


    private val _successPost = MutableLiveData<Boolean>()
    val successPost: LiveData<Boolean>
        get() = _successPost

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

    private val _image = MutableLiveData<List<String>>()
    val image: LiveData<List<String>>
        get() = _image

    private val _postImage = MutableLiveData<List<String>>()
    val postImage: LiveData<List<String>>
        get() =  _postImage

    private val _isInvalid = MutableLiveData<Int>()
    val isInvalid: LiveData<Int>
        get() = _isInvalid


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
    private lateinit var imageList: MutableList<String>

    init {
        _status.value = LoadApiStatus.DONE
        _isInvalid.value = null
    }


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


    //delete photo
    fun removeImages(item: String) {
        imageList.remove(item)
        _image.value = imageList
        Log.d("Request", "imageList remove $item , the list change to ${_image.value} ")
    }

    val selectedCategoryTitle = MutableLiveData<String>()

    fun convertCategoryTitleToInt(title:String) {

        var item : Int = 0

        for (type in CategoryType.values()) {
            if (type.title == title) {
                item =  type.category
            }
        }
        category.value = item
    }

    val selectedCountryTitle = MutableLiveData<String>()

    fun convertCountryTitleToInt(title:String){

        var item : Int = 0

        for (type in CountryType.values()) {
            if (type.title == title) {
                item =  type.country
            }
        }
        country.value = item
    }

    fun checkRequest() {
        _isInvalid.value =
            when {
                title.value.isNullOrEmpty() -> INVALID_FORMAT_TITLE_EMPTY
                image.value.isNullOrEmpty() -> INVALID_FORMAT_IMAGE_EMPTY
                description.value.isNullOrEmpty() -> INVALID_FORMAT_DESCRIPTION_EMPTY
                source.value.isNullOrEmpty() -> INVALID_FORMAT_SOURCE_EMPTY
                category.value == null -> INVALID_FORMAT_CATEGORY_EMPTY
                country.value == null -> INVALID_FORMAT_COUNTRY_EMPTY
                else -> null
            }
    }

    fun editRequest() {
        UserManager.userId?.let {
            _request.value = Request(
                userId = it,
                mainImage = _postImage.value?.get(0) ?: "",
                image = _postImage.value ?: listOf(),
                title = title.value ?: "",
                description = description.value ?: "",
                category = category.value ?: 0,
                country = country.value ?: 0,
                source = source.value ?: ""
            )
        }
        _request.value?.let {
            Log.d("Request", "The request posted is $it")
            postRequest(it)
        }
    }

    fun leave(needRefresh: Boolean = false) {
        _leave.value = needRefresh
    }

    fun onLeft() {
        _leave.value = null
    }


    private fun postRequest(request: Request) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.postRequest(request)
            _successPost.value =
                when (result) {
                    is Result.Success -> {
                        _error.value = null
                        _status.value = LoadApiStatus.DONE
                        result.data
                    }
                    is Result.Fail -> {
                        _error.value = result.error
                        _status.value = LoadApiStatus.ERROR
                        null
                    }
                    is Result.Error -> {
                        _error.value = result.exception.toString()
                        _status.value = LoadApiStatus.ERROR
                        null
                    }
                    else -> {
                        _error.value = MyApplication.instance.getString(R.string.result_fail)
                        _status.value = LoadApiStatus.ERROR
                        null
                    }
                }
        }
    }

    fun uploadImages(uriList: List<String>) {
        val list : MutableList<String> = mutableListOf()
        val imageUri = MutableLiveData<String>()
        val totalCount = uriList.size
        var count = 0
        for (item in uriList) {
            _status.value = LoadApiStatus.LOADING
            coroutineScope.launch {
                _status.value = LoadApiStatus.LOADING
                val result = repository.uploadImage(item.toUri(), "request")
                when (result) {
                    is Result.Success -> {
                        _error.value = null
                        _status.value = LoadApiStatus.DONE
                        Log.d("Chloe","download uri is ${result.data}")
                        imageUri.value =result.data
                        list.add(imageUri.value!!)
                        count ++
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
                    _uploadDone.value = true
                    _status.value = LoadApiStatus.DONE
                }
            }
        }
    }


    companion object {
        const val INVALID_FORMAT_TITLE_EMPTY = 0x11
        const val INVALID_FORMAT_IMAGE_EMPTY = 0x12
        const val INVALID_FORMAT_DESCRIPTION_EMPTY = 0x13
        const val INVALID_FORMAT_CATEGORY_EMPTY = 0x14
        const val INVALID_FORMAT_COUNTRY_EMPTY = 0x15
        const val INVALID_FORMAT_SOURCE_EMPTY = 0x16
        const val NO_ONE_KNOWS = 0x21
    }
}
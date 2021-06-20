package com.chloe.shopshare.request

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.Request
import com.chloe.shopshare.data.Result
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.host.CategoryType
import com.chloe.shopshare.host.CountryType
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

    private val _postDone = MutableLiveData<Boolean?>()
    val postDone: LiveData<Boolean?>
        get() = _postDone

    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    private val _uploadDone = MutableLiveData<Boolean>()
    val uploadDone: LiveData<Boolean>
        get() = _uploadDone

    private val _image = MutableLiveData<List<String>>()
    val image: LiveData<List<String>>
        get() = _image

    private val postImage = MutableLiveData<List<String>>()

    private val _isInvalid = MutableLiveData<Int>()
    val isInvalid: LiveData<Int>
        get() = _isInvalid

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val title = MutableLiveData<String?>()
    val description = MutableLiveData<String>()
    val category = MutableLiveData<Int>()
    val country = MutableLiveData<Int>()
    val source = MutableLiveData<String>()
    private lateinit var imageList: MutableList<String>
    private lateinit var userId: String

    init {
        _status.value = LoadApiStatus.DONE
        UserManager.userId?.let {
            userId = it
        }
    }

    fun pickImages(uri: Uri) {
        imageList =
            when (image.value) {
                null -> mutableListOf()
                else -> image.value?.toMutableList() ?: mutableListOf()
            }
        imageList.add(uri.toString())
        _image.value = imageList
    }

    fun removeImages(item: String) {
        imageList.remove(item)
        _image.value = imageList
    }

    val selectedCategoryTitle = MutableLiveData<String>()

    fun convertCategoryTitleToInt(title: String) {
        val value = CategoryType.values().filter { it.title == title }[0]
        category.value = value.category
        Log.d("RequestTag", "selectedCategory = $title in ${category.value}")
    }

    val selectedCountryTitle = MutableLiveData<String>()

    fun convertCountryTitleToInt(title: String) {
        val value = CountryType.values().filter { it.title == title }[0]
        country.value = value.country
        Log.d("RequestTag", "selectedCountry = $title in ${country.value}")
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
        _request.value = Request(
            userId = userId,
            mainImage = postImage.value?.get(0) ?: "",
            image = postImage.value ?: listOf(),
            title = title.value ?: "",
            description = description.value ?: "",
            category = category.value ?: 0,
            country = country.value ?: 0,
            source = source.value ?: ""
        )

        _request.value?.let {
            postRequest(it)
        }
    }

    private fun postRequest(request: Request) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.postRequest(request)

            _postDone.value =
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

        val list: MutableList<String> = mutableListOf()
        val imageUri = MutableLiveData<String>()
        val totalCount = uriList.size
        var count = 0

        for (item in uriList) {
            _status.value = LoadApiStatus.LOADING
            coroutineScope.launch {
                _status.value = LoadApiStatus.LOADING

                when (val result = repository.uploadImage(item.toUri(), "request")) {
                    is Result.Success -> {
                        _error.value = null
                        _status.value = LoadApiStatus.DONE
                        Log.d("Chloe", "download uri is ${result.data}")
                        imageUri.value = result.data
                        list.add(imageUri.value!!)
                        count++
                    }
                    is Result.Fail -> {
                        _error.value = result.error
                        _status.value = LoadApiStatus.ERROR
                        imageUri.value = null
                        count++
                    }
                    is Result.Error -> {
                        _error.value = result.exception.toString()
                        _status.value = LoadApiStatus.ERROR
                        imageUri.value = null
                        count++
                    }
                    else -> {
                        _error.value = MyApplication.instance.getString(R.string.result_fail)
                        _status.value = LoadApiStatus.ERROR
                        imageUri.value = null
                        count++
                    }
                }
                if (count == totalCount) {
                    postImage.value = list
                    _uploadDone.value = true
                    _status.value = LoadApiStatus.DONE
                }
            }
        }
    }

    fun onUploadDone() {
        _uploadDone.value = null
    }

    fun onPostDone() {
        _postDone.value = null
    }


    companion object {
        const val INVALID_FORMAT_TITLE_EMPTY = 0x11
        const val INVALID_FORMAT_IMAGE_EMPTY = 0x12
        const val INVALID_FORMAT_DESCRIPTION_EMPTY = 0x13
        const val INVALID_FORMAT_CATEGORY_EMPTY = 0x14
        const val INVALID_FORMAT_COUNTRY_EMPTY = 0x15
        const val INVALID_FORMAT_SOURCE_EMPTY = 0x16
    }
}
package com.chloe.shopshare.host


import android.net.Uri
import android.view.View
import android.widget.CheckBox
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.Notify
import com.chloe.shopshare.data.Request
import com.chloe.shopshare.data.Result
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.ext.toDisplayNotifyContent
import com.chloe.shopshare.network.LoadApiStatus
import com.chloe.shopshare.notify.NotifyType
import com.chloe.shopshare.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HostViewModel(private val repository: Repository, private val argument: Request?) :
    ViewModel() {

    private val PATH = "host"

    private val _request = MutableLiveData<Request?>().apply {
        value = argument
    }
    val request: LiveData<Request?>
        get() = _request

    private val _shop = MutableLiveData<Shop>()
    val shop: LiveData<Shop>
        get() = _shop

    private val _shopId = MutableLiveData<String>()
    val shopId: LiveData<String>
        get() = _shopId

    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _isOptionDone = MutableLiveData<Boolean>()
    val isOptionDone: LiveData<Boolean>
        get() = _isOptionDone

    private val _isConditionDone = MutableLiveData<Boolean>()
    val isConditionDone: LiveData<Boolean>
        get() = _isConditionDone

    private val _isInvalid = MutableLiveData<Int>()
    val isInvalid: LiveData<Int>
        get() = _isInvalid

    private val _uploadImageDone = MutableLiveData<Boolean>()
    val uploadImageDone: LiveData<Boolean>
        get() = _uploadImageDone

    private val _postShopDone = MutableLiveData<Boolean>()
    val postShopDone: LiveData<Boolean>
        get() = _postShopDone

    private val _notifyRequesterDone = MutableLiveData<Boolean>()
    val notifyRequesterDone: LiveData<Boolean>
        get() = _notifyRequesterDone

    private val _notifyMemberDone = MutableLiveData<Boolean>()
    val notifyMemberDone: LiveData<Boolean>
        get() = _notifyMemberDone

    private val _updateRequestHostDone = MutableLiveData<Boolean>()
    val updateRequestHostDone: LiveData<Boolean>
        get() = _updateRequestHostDone

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // Shop Content
    private val image = MutableLiveData<List<String>>()
    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val category = MutableLiveData<Int>()
    val country = MutableLiveData<Int>()
    val source = MutableLiveData<String>()
    val isStandard = MutableLiveData<Boolean>()
    val option = MutableLiveData<List<String>>()
    val deliveryList = MutableLiveData<List<Int>>()
    val conditionType = MutableLiveData<Int?>()
    val deadLine = MutableLiveData<Long?>()
    val condition = MutableLiveData<Int?>()
    val conditionContent = MutableLiveData<String?>()
    val optionContent = MutableLiveData<String>()
    val initCategoryPosition = MutableLiveData<Int>()
    val initCountryPosition = MutableLiveData<Int>()
    private val _imagesPicked = MutableLiveData<List<String>>()
    val imagesPicked: LiveData<List<String>>
        get() = _imagesPicked

    private lateinit var userId: String

    init {
        _status.value = LoadApiStatus.DONE
        _isInvalid.value = null
        _isOptionDone.value = false
        _isConditionDone.value = false

        _request.value?.let {
            title.value = it.title
            description.value = it.description
            source.value = it.source
            initCategoryPosition.value = convertValueToPosition(true, it.category)
            initCountryPosition.value = convertValueToPosition(false, it.country)
        }

        UserManager.userId?.let { userId = it }
    }


    fun checkOption() {
        _isOptionDone.value = !(option.value.isNullOrEmpty())
    }

    fun checkCondition() {
        _isConditionDone.value = !(deadLine.value == null && condition.value == null)
    }

    /**Shop Host Type Selected**/

    val selectedTypeRadio = MutableLiveData<Int>()
    private val type: Int
        get() = when (selectedTypeRadio.value) {
            R.id.radio_agent -> ShopType.AGENT.shopType
            R.id.radio_gather -> ShopType.GATHER.shopType
            R.id.radio_private -> ShopType.PRIVATE.shopType
            else -> ShopType.AGENT.shopType
        }

    /**Shop Category amd Country Selected**/

    val categoryTitle = MutableLiveData<String>()
    val countryTitle = MutableLiveData<String>()

    fun convertTitleToValue(isCategory: Boolean, title: String) {
        when (isCategory) {
            true -> category.value = CategoryType.values().filter { it.title == title }[0].category
            else -> country.value = CountryType.values().filter { it.title == title }[0].country
        }
    }

    private fun convertValueToPosition(isCategory: Boolean, value: Int): Int {
        return when (isCategory) {
            true -> CategoryType.values().filter { it.category == value }[0].positionOnSpinner
            else -> CountryType.values().filter { it.country == value }[0].positionOnSpinner
        }
    }

    /**Shop Image Picked**/

    fun pickImages(uri: Uri) {
        var list = mutableListOf<String>()
        _imagesPicked.value?.let { list = it.toMutableList() }
        list.add(uri.toString())
        _imagesPicked.value = list
    }

    fun removeImages(image: String) {
        var list = mutableListOf<String>()
        _imagesPicked.value?.let { list = it.toMutableList() }
        list.remove(image)
        _imagesPicked.value = list
    }

    /**Shop Delivery Selected**/

    private fun selectDelivery(delivery: Int) {
        var list = mutableListOf<Int>()
        deliveryList.value?.let { list = it.toMutableList() }
        list.add(delivery)
        deliveryList.value = list.sorted()
    }

    private fun removeDelivery(delivery: Int) {
        var list = mutableListOf<Int>()
        deliveryList.value?.let { list = it.toMutableList() }
        list.remove(delivery)
        deliveryList.value = list.sorted()
    }

    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val value = DeliveryMethod.values().filter { it.title == view.text }[0].delivery
            when (view.isChecked) {
                true -> selectDelivery(value)
                else -> removeDelivery(value)
            }
        }
    }

    /**Field Empty Check**/

    fun isShopInvalid() {
        _isInvalid.value =
            when {
                selectedTypeRadio.value == 0 -> INVALID_FORMAT_TYPE_EMPTY
                _imagesPicked.value.isNullOrEmpty() -> INVALID_FORMAT_IMAGE_EMPTY
                title.value.isNullOrEmpty() -> INVALID_FORMAT_TITLE_EMPTY
                description.value.isNullOrEmpty() -> INVALID_FORMAT_DESCRIPTION_EMPTY
                source.value.isNullOrEmpty() -> INVALID_FORMAT_SOURCE_EMPTY
                category.value == null -> INVALID_FORMAT_CATEGORY_EMPTY
                country.value == null -> INVALID_FORMAT_COUNTRY_EMPTY
                option.value.isNullOrEmpty() -> INVALID_FORMAT_OPTION_EMPTY
                deliveryList.value.isNullOrEmpty() -> INVALID_FORMAT_DELIVERY_EMPTY
                conditionContent.value.isNullOrEmpty() -> INVALID_FORMAT_CONDITION_EMPTY
                else -> null
            }
    }

    /**Image Upload to Server**/

    fun uploadImages(images: List<String>) {

        val list: MutableList<String> = mutableListOf()
        val totalCount = images.size
        var count = 0

        for (item in images) {
            coroutineScope.launch {
                _status.value = LoadApiStatus.LOADING

                when (val result = repository.uploadImage(item.toUri(), PATH)) {
                    is Result.Success -> {
                        _error.value = null
                        _status.value = LoadApiStatus.DONE
                        list.add(result.data)
                        count++
                    }
                    is Result.Fail -> {
                        _error.value = result.error
                        _status.value = LoadApiStatus.ERROR
                        count++
                    }
                    is Result.Error -> {
                        _error.value = result.exception.toString()
                        _status.value = LoadApiStatus.ERROR
                        count++
                    }
                    else -> {
                        _error.value = MyApplication.instance.getString(R.string.result_fail)
                        _status.value = LoadApiStatus.ERROR
                        count++
                    }
                }

                if (count == totalCount) {
                    image.value = list
                    _uploadImageDone.value = true
                }
            }
        }
    }

    fun onImageUploadDone() {
        _uploadImageDone.value = null
    }

    /**Edit Shop Content , Post it to Server**/

    fun editShop() {
        _shop.value = Shop(
            userId = userId,
            type = type,
            mainImage = image.value?.get(0) ?: "",
            image = image.value ?: listOf(),
            title = title.value ?: "",
            description = description.value ?: "",
            category = category.value ?: 0,
            country = country.value ?: 0,
            source = source.value ?: "",
            isStandard = isStandard.value ?: false,
            option = option.value ?: listOf(),
            deliveryMethod = deliveryList.value ?: listOf(),
            conditionType = conditionType.value,
            deadLine = deadLine.value,
            condition = condition.value,
            status = 0,
            order = listOf()
        )
        _shop.value?.let { postShop(it) }
    }

    private fun postShop(shop: Shop) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.postShop(shop)
            _shopId.value =
                when (result) {
                    is Result.Success -> {
                        _error.value = null
                        _status.value = LoadApiStatus.DONE
                        _postShopDone.value = true
                        result.data.number
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

    fun onShopPostDone() {
        _postShopDone.value = null
    }

    /**Notify to the related people**/

    fun editRequesterNotify() {
        _shopId.value?.let { id ->
            _request.value?.let { request ->
                val notify = Notify(
                    shopId = id,
                    requestId = request.id,
                    type = NotifyType.REQUEST_SUCCESS_REQUESTER.type,
                    title = NotifyType.REQUEST_SUCCESS_REQUESTER.title,
                    content = NotifyType.REQUEST_SUCCESS_REQUESTER.toDisplayNotifyContent(request.title)
                )
                notifyToRequester(request.userId, notify)
            }
        }
    }

    fun editMemberNotify() {
        _shopId.value?.let { id ->
            _request.value?.let { request ->
                val notify = Notify(
                    shopId = id,
                    requestId = _request.value?.id,
                    type = NotifyType.REQUEST_SUCCESS_MEMBER.type,
                    title = NotifyType.REQUEST_SUCCESS_MEMBER.title,
                    content = NotifyType.REQUEST_SUCCESS_MEMBER.toDisplayNotifyContent(request.title)
                )
                notifyToMember(notify)
            }
        }
    }

    private fun notifyToRequester(id: String, notify: Notify) {
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            when (val result = repository.postNotifyToHost(id, notify)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    _notifyRequesterDone.value = result.data
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _error.value = MyApplication.instance.getString(R.string.result_fail)
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
    }

    private fun notifyToMember(notify: Notify) {
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            when (val result = repository.postRequestNotifyToMember(notify)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    _notifyMemberDone.value = result.data
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _error.value = MyApplication.instance.getString(R.string.result_fail)
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
    }

    fun onRequesterNotifyDone() {
        _notifyRequesterDone.value = null
    }

    fun onMemberNotifyDone() {
        _notifyMemberDone.value = null
    }

    fun updateHost() {
        request.value?.let { request ->
            shopId.value?.let { shopId ->
                updateRequestHost(request.id, shopId, userId)
            }
        }
    }

    private fun updateRequestHost(requestId: String, shopId: String, hostId: String) {
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            when (val result = repository.updateRequestHost(requestId, shopId, hostId)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    _updateRequestHostDone.value = result.data
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _error.value = MyApplication.instance.getString(R.string.result_fail)
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
    }

    fun onRequestHostUpdateDone() {
        _updateRequestHostDone.value = null
    }

    companion object {
        const val INVALID_FORMAT_TYPE_EMPTY = 0x11
        const val INVALID_FORMAT_IMAGE_EMPTY = 0x12
        const val INVALID_FORMAT_TITLE_EMPTY = 0x13
        const val INVALID_FORMAT_DESCRIPTION_EMPTY = 0x14
        const val INVALID_FORMAT_CATEGORY_EMPTY = 0x15
        const val INVALID_FORMAT_COUNTRY_EMPTY = 0x16
        const val INVALID_FORMAT_SOURCE_EMPTY = 0x17
        const val INVALID_FORMAT_OPTION_EMPTY = 0x18
        const val INVALID_FORMAT_DELIVERY_EMPTY = 0x19
        const val INVALID_FORMAT_CONDITION_EMPTY = 0x20
    }
}

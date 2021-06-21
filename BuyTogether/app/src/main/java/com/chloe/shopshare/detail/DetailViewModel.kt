package com.chloe.shopshare.detail

import android.graphics.Rect
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.*
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.network.LoadApiStatus
import com.chloe.shopshare.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: Repository,
    private val args: String
) : ViewModel() {

    private val _shopId = MutableLiveData<String>().apply {
        value = args
    }
    val shopId: LiveData<String>
        get() = _shopId

    private var _shop = MutableLiveData<Shop>()
    val shop: LiveData<Shop>
        get() = _shop

    private var _orderList = MutableLiveData<List<Order>>()
    val orderList: LiveData<List<Order>>
        get() = _orderList

    private val _product = MutableLiveData<List<Product>?>()
    val product: LiveData<List<Product>?>
        get() = _product

    private var _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _chatRoom = MutableLiveData<ChatRoom>()
    val chatRoom: LiveData<ChatRoom>
        get() = _chatRoom

    val isExpanded = MutableLiveData<Boolean?>()

    private val _navigateToVariation = MutableLiveData<Cart>()
    val navigateToVariation: LiveData<Cart>
        get() = _navigateToVariation

    private val _navigateToCart = MutableLiveData<Cart>()
    val navigateToCart: LiveData<Cart>
        get() = _navigateToCart

    private val _navigateToChatRoom = MutableLiveData<ChatRoom>()
    val navigateToChatRoom: LiveData<ChatRoom>
        get() = _navigateToChatRoom

    private val _navigateToHome = MutableLiveData<Boolean>()

    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome


    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _refreshStatus = MutableLiveData<Boolean>()
    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    private val _snapPosition = MutableLiveData<Int>()
    val snapPosition: LiveData<Int>
        get() = _snapPosition

    val myId: String
        get() = UserManager.userId ?: ""

    init {
        isExpanded.value = false
        _shopId.value?.let {
            getLiveDetailShop(it)
            getLiveOrderOfShop(it)
        }


    }


    fun navigateToVariation(shop: Shop, products: List<Product>?) {
        _navigateToVariation.value = Cart(shop, products ?: listOf())
    }

    fun onVariationNavigated() {
        _navigateToVariation.value = null
    }

    fun navigateToCart(shop: Shop, products: List<Product>?) {
        _navigateToCart.value = Cart(shop, products ?: listOf())
    }

    fun onCartNavigated() {
        _navigateToCart.value = null
    }

    fun navigateToChatRoom(chatRoom: ChatRoom) {
        _navigateToChatRoom.value = chatRoom
    }

    fun onChatRoomNavigated() {
        _navigateToChatRoom.value = null
    }

    fun navigateToHome() {
        _navigateToHome.value = true
    }

    fun onHomeNavigated() {
        _navigateToHome.value = null
    }

    fun isExpanded(isChecked: Boolean) {
        isExpanded.value = isChecked
    }


    fun updateProductList(products: List<Product>) {
        when (products.isNotEmpty()) {
            true -> _product.value = null
            false -> _product.value = products
        }
    }

    val decoration = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.left = 0
            } else {
                outRect.left =
                    MyApplication.instance.resources.getDimensionPixelSize(R.dimen.space_detail_circle)
            }
        }
    }


    /**
     * When the gallery scroll, at the same time circles design will switch.
     */
    fun onGalleryScrollChange(
        layoutManager: RecyclerView.LayoutManager?,
        linearSnapHelper: LinearSnapHelper
    ) {
        val snapView = linearSnapHelper.findSnapView(layoutManager)
        snapView?.let {
            layoutManager?.getPosition(snapView)?.let {
                if (it != _snapPosition.value) {
                    _snapPosition.value = it
                }
            }
        }
    }

    private fun getLiveDetailShop(shopId: String) {
        _status.value = LoadApiStatus.LOADING
        _shop = repository.getLiveDetailShop(shopId)
        _status.value = LoadApiStatus.DONE
        _refreshStatus.value = false
    }

    private fun getLiveOrderOfShop(shopId: String) {
        _status.value = LoadApiStatus.LOADING
        _orderList = repository.getLiveOrderOfShop(shopId)
        _status.value = LoadApiStatus.DONE
        _refreshStatus.value = false
    }


    fun getUserProfile(userId: String) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.getUserProfile(userId)

            _user.value =
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

    fun getChatRoom(myId: String, friendId: String) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.getChatRoom(myId, friendId)
            _chatRoom.value =
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

}



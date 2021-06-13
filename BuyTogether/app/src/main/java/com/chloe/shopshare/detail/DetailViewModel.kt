package com.chloe.shopshare.detail

import android.graphics.Rect
import android.util.Log
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
import com.chloe.shopshare.data.source.remote.RemoteDataSource.getLiveDetailShop
import com.chloe.shopshare.network.LoadApiStatus
import com.chloe.shopshare.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: Repository,
    private val arguments: String
):ViewModel() {

    private val _shopId = MutableLiveData<String>().apply {
        Log.d("Chloe", "_shopId = $arguments")
        value = arguments
    }
    val shopId: LiveData<String>
        get() = _shopId

    private var _shop = MutableLiveData<Shop>()
    val shop: LiveData<Shop>
        get() = _shop

    private var _orderList = MutableLiveData<List<Order>>()
    val orderList: LiveData<List<Order>>
        get() = _orderList


    private val _order = MutableLiveData<Order>()
    val order: LiveData<Order>
        get() = _order


    private val _product = MutableLiveData<List<Product>?>()
    val product: LiveData<List<Product>?>
        get() = _product

    private var _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _chatRoom = MutableLiveData<ChatRoom>()
    val chatRoom: LiveData<ChatRoom>
        get() = _chatRoom

    val isChecked = MutableLiveData<Boolean>()

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // status for the loading icon of swl
    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    // it for gallery circles design
    private val _snapPosition = MutableLiveData<Int>()

    val snapPosition: LiveData<Int>
        get() = _snapPosition

    val productItem = MutableLiveData<Product?>()
    lateinit var myId : String

    init {
        Log.i("Chloe", "Detail")
        isChecked.value = false
        _shopId.value?.let {
            getLiveDetailShop(it)
            getLiveOrderOfShop(it)
            Log.i("Chloe", "_shop is ${_shop.value}")
        }
        UserManager.userId?.let {
            myId = it
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

    //要傳給選擇商品選項頁面的

    private val _navigateToOption = MutableLiveData<List<Product>>()
    val navigateToOption: LiveData<List<Product>>
        get() = _navigateToOption

    fun navigateToOption(product: List<Product>?) {
        _navigateToOption.value = product?: listOf()
    }

    fun onOptionNavigated() {
        _navigateToOption.value = null
    }

    // 要傳給聊天頁面的
    private val _navigateToChatRoom = MutableLiveData<ChatRoomKey>()
    val navigateToChatRoom: LiveData<ChatRoomKey>
        get() = _navigateToChatRoom

    fun navigateToChatRoom(chatRoom: ChatRoom) {
        Log.d("Chloe","chatRoomKey : myId = ${UserManager.userId!!},friendId = ${_shop.value?.userId},chatRoomId = ${chatRoom.id}")
        _shop.value?.let {
            _navigateToChatRoom.value = ChatRoomKey(myId = UserManager.userId!!,friendId = it.userId,chatRoomId = chatRoom.id)
        }

    }

    fun onChatRoomNavigated() {
        _navigateToChatRoom.value = null
    }



    // 要傳給商品清單頁面的
    private val _navigateToProductList = MutableLiveData<List<Product>>()
    val navigateToProductList: LiveData<List<Product>>
        get() = _navigateToProductList

    fun navigateToProductList(product: List<Product>?) {
        _navigateToProductList.value = product?: listOf()
    }

    fun onProductListNavigated() {
        _navigateToProductList.value = null
    }


    //從選擇頁面/清單頁面回來要把新的productList更新取代現有的list中

    fun updateProductList(product: List<Product>){
        if (product.isNullOrEmpty()){
            _product.value = null
            Log.d("Chloe","Product is update to null =${_product.value} ,  product from selector is ${product}}")
        }else{
            _product.value = product
            Log.d("Chloe","Product is update to new =${_product.value} ,  product from selector is ${product}}")
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
                if (it != snapPosition.value) {
                    _snapPosition.value = it
                }
            }
        }
    }



    fun getUserProfile(userId: String) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.getUserProfile(userId)
            // It will return Result object after Deferred flow
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


//mock
//    private val orderId : Int = 1222
//    private val orderTime: Long= java.util.Calendar.getInstance().timeInMillis
//    private val userId:Long = 10000002
//
//    private val price: Int = 2000
//    private val phone:String = "0988888888"
//    private val delivery: String = "711永和門市"
//    private val note: String? = "無"
//    private val mockPaymentStatus: Int = 0

//private fun getDetailShop(shopId: String) {
//
//    coroutineScope.launch {
//
//        _status.value = LoadApiStatus.LOADING
//
//        val result = repository.getDetailShop(shopId)
//
//        _shop.value = when (result) {
//            is Result.Success -> {
//                _error.value = null
//                _status.value = LoadApiStatus.DONE
//                result.data
//            }
//            is Result.Fail -> {
//                _error.value = result.error
//                _status.value = LoadApiStatus.ERROR
//                null
//            }
//            is Result.Error -> {
//                _error.value = result.exception.toString()
//                _status.value = LoadApiStatus.ERROR
//                null
//            }
//            else -> {
//                _error.value = MyApplication.instance.getString(R.string.result_fail)
//                _status.value = LoadApiStatus.ERROR
//                null
//            }
//        }
//        _refreshStatus.value = false
//    }
//}

// 要傳給確定跟團頁面的
//private val _navigateToParticipate = MutableLiveData<Shop>()
//
//val navigateToParticipate: LiveData<Shop>
//    get() = _navigateToParticipate
//
//fun navigateToParticipate(shop: Shop) {
//    _navigateToParticipate.value = shop
//}
//
//fun onParticipateNavigated() {
//    _navigateToParticipate.value = null
//}


//fun getOrderOfShop(shopId : String) {
//
//    coroutineScope.launch {
//        _status.value = LoadApiStatus.LOADING
//        val result = repository.getOrderOfShop(shopId)
//        _orderList.value = when (result) {
//            is Result.Success -> {
//                _error.value = null
//                _status.value = LoadApiStatus.DONE
//                Log.d("Chloe","result = ${result.data}")
//                result.data
//            }
//            is Result.Fail -> {
//                _error.value = result.error
//                _status.value = LoadApiStatus.ERROR
//                null
//            }
//            is Result.Error -> {
//                _error.value = result.exception.toString()
//                _status.value = LoadApiStatus.ERROR
//                null
//            }
//            else -> {
//                _error.value = MyApplication.instance.getString(R.string.result_fail)
//                _status.value = LoadApiStatus.ERROR
//                null
//            }
//        }
//        _refreshStatus.value = false
//    }
//}

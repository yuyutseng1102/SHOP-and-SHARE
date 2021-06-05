package com.chloe.shopshare.myhost.item

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.*
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.myhost.MyHostType
import com.chloe.shopshare.myorder.MyOrderType
import com.chloe.shopshare.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.chloe.shopshare.util.UserManager

class MyHostListViewModel(private val repository: Repository,private val myHostType: MyHostType): ViewModel() {

    private val _shop = MutableLiveData<List<Shop>>()
    val shop: LiveData<List<Shop>>
        get() = _shop

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String?>()

    val error: LiveData<String?>
        get() = _error

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _visible = MutableLiveData<Boolean>()

    val visible: LiveData<Boolean>
        get() = _visible

    // status for the loading icon of swl
    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    lateinit var userId : String

    init {
        _visible.value = false
        UserManager.userId?.let {
            userId = it
            getShop()
        }
    }

    private fun getShop(){
        when(myHostType){
            MyHostType.ALL_SHOP -> getMyShop(userId)
            MyHostType.OPENING_SHOP -> getMyShopByStatus(userId, MyHostType.OPENING_SHOP.status)
            MyHostType.PROCESS_SHOP -> getMyShopByStatus(userId, MyHostType.PROCESS_SHOP.status)
            MyHostType.SHIPMENT_SHOP -> getMyShopByStatus(userId, MyHostType.SHIPMENT_SHOP.status)
            MyHostType.FINISH_SHOP -> getMyShopByStatus(userId, MyHostType.FINISH_SHOP.status)
        }
    }

    // Handle navigation to manage
    private val _navigateToManage = MutableLiveData<String?>()
    val navigateToManage: LiveData<String?>
        get() = _navigateToManage

    fun navigateToManage(shopId: String) {
        _navigateToManage.value = shopId
    }

    fun onManageNavigated() {
        _navigateToManage.value = null
    }

    private fun getMyShop(userId : String) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.getMyShop(userId)
            _shop.value = when (result) {
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
            Log.d("Shop","shop is ${_shop.value}")

            _visible.value = _shop.value.isNullOrEmpty()
            Log.d("Shop","visible is ${_visible.value}")
            _refreshStatus.value = false
        }
    }

    private fun getMyShopByStatus(userId : String, status: List<Int>) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.getMyShopByStatus(userId,status)
            _shop.value = when (result) {
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
            Log.d("Shop","shop is ${_shop.value}")

            _visible.value = _shop.value.isNullOrEmpty()
            Log.d("Shop","visible is ${_visible.value}")
            _refreshStatus.value = false
        }
    }


    fun refresh() {
        if (status.value != LoadApiStatus.LOADING) {
            getShop()
        }
    }


}





//mockData
//    val mockUserId = "chloe123"
//    private val time: Long= java.util.Calendar.getInstance().timeInMillis
//    private val method = 1
//    private val category = 101
//    private val country = 12
//    private val source = ""
//    private val isStandard = false
//    private val option = listOf("全網站")
//    private val deliveryMethod  = listOf(10,11,12)
//    private val conditionType = 1
//    private val deadLine = java.util.Calendar.getInstance().timeInMillis
//    private val condition = 5000
//    private val shopStatus: Int = 0
//    private val orderId1 = "1245L"
//    private val orderId2 = "1243L"
//    private val orderId3 = "1241L"
//    private val orderTime: Long= Calendar.getInstance().timeInMillis
//    private val userId = "193798"
//    private val products:List<Product> = listOf(Product("棉麻上衣白色/M",1), Product("法式雪紡背心/M",2), Product("開襟洋裝/M",5))
//    private val products2:List<Product> = listOf(Product("棉麻上衣白色/M",1), Product("法式雪紡背心/M",2), Product("開襟洋裝/M",5), Product("法式雪紡背心/M",2), Product("開襟洋裝/M",5))
//    private val price: Int = 2000
//    private val phone:String = "0988888888"
//    private val delivery: Int = 10
//    private val address: String = "永和門市"
//    private val note: String = "無"
//    private val mockPaymentStatus: Int = 0


//    private val order:List<Order>? =
//            listOf(
//                    Order(orderId1, orderTime, userId, products, price, phone, delivery, address,note, mockPaymentStatus),
//                    Order(orderId2, orderTime, userId, products2, price, phone, delivery,address,note,mockPaymentStatus),
//                    Order(orderId3, orderTime, userId, products, price, phone, delivery,address,note,mockPaymentStatus)
//            )
//
//
//
//    fun addMockData(){
//        val mockCollectList: MutableList<Shop> = mutableListOf()
//        mockCollectList.add(Shop("389089",mockUserId,time,method,"https://img3.momoshop.com.tw/1619364953/goodsimg/0008/361/096/8361096_B.jpg", listOf(),"romand唇釉","",category,country,source,isStandard,option,deliveryMethod,conditionType,deadLine,condition,shopStatus,order))
//        mockCollectList.add(Shop("332798",mockUserId,time,method,"https://img2.momoshop.com.tw/expertimg/0007/989/213/mobile//1.jpg", listOf(),"CHANEL淡香水","",category,country,source,isStandard,option,deliveryMethod,conditionType,deadLine,condition,shopStatus,order))
//        mockCollectList.add(Shop("885433",mockUserId,time,method,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTz-DzMBHjD69t2DehnZqVk7iqMH2NxsRGIBQ&usqp=CAU", listOf(),"ETUDE HOUSE玩轉色彩四色眼彩盤","",category,country,source,isStandard,option,deliveryMethod,conditionType,deadLine,condition,shopStatus,order))
//        mockCollectList.add(Shop("324566",mockUserId,time,method,"https://img4.momoshop.com.tw/1619225003/goodsimg/0008/435/142/8435142_R.jpg", listOf(),"ettusais絕不失手眼線膠筆","",category,country,source,isStandard,option,deliveryMethod,conditionType,deadLine,condition,shopStatus,order))
//        mockCollectList.add(Shop("432435",mockUserId,time,method,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQKhEkNviKcGv5VF1HKlgNfYr7eV2hqBvydYA&usqp=CAU", listOf(),"niko and...","",category,country,source,isStandard,option,deliveryMethod,conditionType,deadLine,condition,shopStatus,order))
//        mockCollectList.add(Shop("353254",mockUserId,time,method,"https://img4.momoshop.com.tw/1619308533/goodsimg/0005/951/668/5951668_R.jpg", listOf(),"ettusais高機能毛孔淨透凝膠","",category,country,source,isStandard,option,deliveryMethod,conditionType,deadLine,condition,shopStatus,order))
//        mockCollectList.add(Shop("432543",mockUserId,time,method,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQyY877kzJmXsn21vfh0kDfJoWM11ZWGzvxVA&usqp=CAU", listOf(),"【DHC】濃密保濕潤色唇膏","",category,country,source,isStandard,option,deliveryMethod,conditionType,deadLine,condition,shopStatus,order))
//        mockCollectList.add(Shop("464575",mockUserId,time,method,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTzGTV2OR_4UJC__k6wmYq-9MuiSw6ReecI7w&usqp=CAU", listOf(),"復古おまち堂拉麵主題陶瓷碗","",category,country,source,isStandard,option,deliveryMethod,conditionType,deadLine,condition,shopStatus,order))
//        _shop.value = mockCollectList
//        }
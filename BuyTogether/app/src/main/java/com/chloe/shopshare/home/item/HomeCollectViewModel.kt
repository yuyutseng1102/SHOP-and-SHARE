package com.chloe.shopshare.home.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.home.HomeType
import com.chloe.shopshare.home.SortMethod
import com.chloe.shopshare.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.chloe.shopshare.data.Result

class HomeCollectViewModel(private val repository: Repository) : ViewModel() {

    private val _shop = MutableLiveData<List<Shop>>()
    val shop: LiveData<List<Shop>>
    get() = _shop

    var liveShops = MutableLiveData<List<Shop>>()

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

    private val _navigateToDetail = MutableLiveData<String>()

    val navigateToDetail: LiveData<String>
        get() = _navigateToDetail


    fun navigateToDetail(shop:Shop){
        _navigateToDetail.value = shop.id
    }

    fun onDetailNavigated(){
        _navigateToDetail.value = null
    }

    init {
            getOpeningShop()
    }

    private fun getOpeningShop() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getOpeningShop()

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
            _refreshStatus.value = false
        }
    }


    fun refresh() {

        if (status.value != LoadApiStatus.LOADING) {
            getOpeningShop()
        }
    }




    //排序方式
    val selectedSortMethodPosition = MutableLiveData<Int>()
    val sortMethod: LiveData<SortMethod> = Transformations.map(selectedSortMethodPosition) {
        SortMethod.values()[it]
    }


}




//mock
//private val mockId = "387986522"
//private val mockUserId = "1873585"
//private val method = 1
//private val category = 101
//private val country = 12
//private val delivery =  listOf(10,20)
//
//fun addMockData(type:HomeType){
//    val mockCollectList: MutableList<Shop> = mutableListOf()
//    mockCollectList.add(Shop(mockId,mockUserId,0L,method,"https://img3.momoshop.com.tw/1619364953/goodsimg/0008/361/096/8361096_B.jpg", listOf(),"romand唇釉","徵滿10支",category,country,"",true,
//        listOf(),delivery,1,null,100,0, listOf()))
//    mockCollectList.add(Shop(mockId,mockUserId,0L,method,"https://img2.momoshop.com.tw/expertimg/0007/989/213/mobile//1.jpg", listOf(),"CHANEL淡香水","500鎂收團",category,country,"",true,
//        listOf(),delivery,1,null,100,0, listOf()))
//    mockCollectList.add(Shop(mockId,mockUserId,0L,method,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTz-DzMBHjD69t2DehnZqVk7iqMH2NxsRGIBQ&usqp=CAU", listOf(),"ETUDE HOUSE玩轉色彩四色眼彩盤","5/30收團",category,country,"",true,
//        listOf(),delivery,1,null,100,0, listOf()))
//    mockCollectList.add(Shop(mockId,mockUserId,0L,method,"https://img4.momoshop.com.tw/1619225003/goodsimg/0008/435/142/8435142_R.jpg", listOf(),"ettusais絕不失手眼線膠筆","徵滿10支",category,country,"",true,
//        listOf(),delivery,1,null,100,0, listOf()))
//    mockCollectList.add(Shop(mockId,mockUserId,0L,method,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQKhEkNviKcGv5VF1HKlgNfYr7eV2hqBvydYA&usqp=CAU", listOf(),"niko and... イオンモール","5/30收團",category,country,"",true,
//        listOf(),delivery,1,null,100,0, listOf()))
//    mockCollectList.add(Shop(mockId,mockUserId,0L,method,"https://img4.momoshop.com.tw/1619308533/goodsimg/0005/951/668/5951668_R.jpg", listOf(),"ettusais高機能毛孔淨透凝膠","徵滿10支",category,country,"",true,
//        listOf(),delivery,1,null,100,0, listOf()))
//    mockCollectList.add(Shop(mockId,mockUserId,0L,method,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQyY877kzJmXsn21vfh0kDfJoWM11ZWGzvxVA&usqp=CAU", listOf(),"【DHC】濃密保濕潤色唇膏","徵滿10支",category,country,"",true,
//        listOf(),delivery,1,null,100,0, listOf()))
//    mockCollectList.add(Shop(mockId,mockUserId,0L,method,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTzGTV2OR_4UJC__k6wmYq-9MuiSw6ReecI7w&usqp=CAU", listOf(),"復古おまち堂拉麵主題陶瓷碗","徵滿10支",category,country,"",true,
//        listOf(),delivery,1,null,100,0, listOf()))
//
//    val mockSeekList: MutableList<Shop> = mutableListOf()
//    mockSeekList.add(Shop(mockId,mockUserId,0L,method,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR1hGuwvKNU575HvvSCGfY3ANhTIE6nB5gAAQ&usqp=CAU", listOf(),"男裝 T/C 格紋 工作 短袖襯衫","徵滿10支",category,country,"",true,
//        listOf(),delivery,1,null,100,0, listOf()))
//    mockSeekList.add(Shop(mockId,mockUserId,0L,method,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQDQ7YdtgPVknTMvDfE7CegM3TBKdqaocZafg&usqp=CAU", listOf(),"男裝 T/C 格紋 工作 短袖襯衫","500鎂收團",category,country,"",true,
//        listOf(),delivery,1,null,100,0, listOf()))
//    mockSeekList.add(Shop(mockId,mockUserId,0L,method,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTZEoeDTqE3eNvlxUm96GbEazY6XkXHy-PYNg&usqp=CAU", listOf(),"女裝 編織 大 托特包","5/30收團",category,country,"",true,
//        listOf(),delivery,1,null,100,0, listOf()))
//    mockSeekList.add(Shop(mockId,mockUserId,0L,method,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR_KV6pFFhxIVvypk3kmeQgKcKnjbeQ9kJFMA&usqp=CAU", listOf(),"BEAMS貝雷帽","徵滿10支",category,country,"",true,
//        listOf(),delivery,1,null,100,0, listOf()))
//    mockSeekList.add(Shop(mockId,mockUserId,0L,method,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR_yZLDq7EAlzC3wgdc6mZPTQiwTvAnGmcaaw&usqp=CAU", listOf(),"Ray BEAMS項鍊","5/30收團",category,country,"",true,
//        listOf(),delivery,1,null,100,0, listOf()))
//    mockSeekList.add(Shop(mockId,mockUserId,0L,method,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTOSoBSpK2BSoQU1b7Cm_WxzdvHNS6P6919lQ&usqp=CAU", listOf(),"購物籃","徵滿10支",category,country,"",true,
//        listOf(),delivery,1,null,100,0, listOf()))
//    mockSeekList.add(Shop(mockId,mockUserId,0L,method,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRP94tKQE-Y1rtiT_m6feLDq1RsiZ_PCd1R0g&usqp=CAU", listOf(),"加菲貓休閒防水拖鞋","徵滿10支",category,country,"",true,
//        listOf(),delivery,1,null,100,0, listOf()))
//    mockSeekList.add(Shop(mockId,mockUserId,0L,method,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRgx-UB_xN_-uVudnJLOjtcSgZir-BB_oUExw&usqp=CAU", listOf(),"【DHC】濃密保濕潤色唇膏","徵滿10支",category,country,"",true,
//        listOf(),delivery,1,null,100,0, listOf()))
//
//    when (type){
//        HomeType.HOST -> _shop.value = mockCollectList
//        HomeType.SEEK -> _shop.value = mockSeekList
//    }
//
//}

package com.chloe.buytogether.collection.manage

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.chloe.buytogether.bindEditorMemberChecked
import com.chloe.buytogether.collection.groupmessage.GroupMessageDialog
import com.chloe.buytogether.data.Collections
import com.chloe.buytogether.data.Order
import com.chloe.buytogether.data.Product
import com.chloe.buytogether.data.source.Repository
import com.google.android.gms.common.config.GservicesValue.value
import kotlinx.android.parcel.Parcelize
import java.util.*

class CollectionManageViewModel(
        private val repository: Repository,
        private val arguments: Collections
):ViewModel() {


    private val _collection = MutableLiveData<Collections>().apply {
        value = arguments
    }

    val collection: LiveData<Collections>
        get() = _collection

    private val _order = MutableLiveData<List<Order?>>()
    val order: LiveData<List<Order?>>
        get() = _order

    private val _member = MutableLiveData<Order?>()
    val member : LiveData<Order?>
        get() = _member

    private val _isChecked = MutableLiveData<Boolean>()
    val isChecked: LiveData<Boolean>
        get() = _isChecked

    private val _collectionStatus = MutableLiveData<Int>()
    val collectionStatus: LiveData<Int>
        get() = _collectionStatus

    val messageContent = MutableLiveData<String>()




    init {
        _order.value = _collection.value?.order
        _collectionStatus.value = _collection.value?.status
    }


//
//    private val _paymentStatus = MutableLiveData<Int>()
//    val paymentStatus: LiveData<Int>
//        get() = _paymentStatus



    var checkedMemberPosition = MutableLiveData<Int?>()
//    var orderList: MutableList<Order>? = mutableListOf()
private val deleteList: MutableList<Order>? = mutableListOf()



    //打勾的是即將要刪除的團員
    fun checkAgain(member: Order, position: Int){
        checkedMemberPosition.value = position
        Log.d("checkChloe","selectMember=$member, position=$position")
        _member.value = member
        if (_member.value!=null) {
            if (_member.value!!.isCheck) {
                deleteList?.add(_member.value!!)
            }else if (!_member.value!!.isCheck){
                deleteList?.remove(_member.value!!)
            }
            Log.d("checkChloe","deleteList=${deleteList}")
        }
    }

    fun deleteMember() {

        if(deleteList!=null){
            val realList = _order.value?.toMutableList()
            for(i in deleteList){
                realList?.remove(i)
            }
            _order.value = realList
        }
    }


    //點選確定後
    //把剩下的所有團員 狀態改為待付款

    fun readyCollect(){

        if (_order.value!=null){
                for (i in _order.value!!){
                    i?.paymentStatus = 1
                }
        }
            Log.d("Chloe","after delete, order value is ${_order.value}")
            //更新collection
            _collection.value?.order =_order.value
            Log.d("Chloe","after update, now collection is ${_collection.value}")
        //更新collection的status
        _collectionStatus.value = 1
        expectStatus.value = 0
        Log.d("Chloe","after update, now status is ${_collectionStatus.value}")
        }

    //按鈕改變團購狀態

    var expectStatus = MutableLiveData<Int>()


    fun clickStatus(status:Int){
        expectStatus.value = status
        Log.d("Chloe","i want to change the status to $expectStatus")
    }

    fun updateStatus(){
        _collectionStatus.value = expectStatus.value

            expectStatus.value = 0
        Log.d("Chloe","after update, now status is ${_collectionStatus.value}")
    }




    }






//    private val _product = MutableLiveData<List<Product>>().apply {
//        value = products
//    }
//    val product: LiveData<List<Product>>
//        get() = _product



//    val orderList: MutableList<Order> = mutableListOf()
//    fun addMockData(){
//
//        orderList.add(Order(
//                orderId, orderTime, userId, products, price, phone, delivery,note,mockPaymentStatus
//        ))
//        orderList.add(Order(
//                orderId, orderTime, userId, products2, price, phone, delivery,note,mockPaymentStatus
//        ))
//        orderList.add(Order(
//                orderId, orderTime, userId, products, price, phone, delivery,note,mockPaymentStatus
//        ))
//        _order.value = orderList
//    }
//if (orderList!=null) {
//    for (i in orderList) {
//        i.paymentStatus = 1
//    }
//    _order.value = orderList
//
//}else _order.value = null
//
//if (_order.value!=null) {
//    for (i in _order.value!!) {
//        _collection.value?.order = _collection.value?.order?.filter { it.orderId == i.orderId }
//        Log.d("Chloe", "order value now is ${_collection.value?.order}")
//    }
//}else{
//    _collection.value?.order = listOf()
//}

//    fun checkMember(member: Order, position: Int) {
//        Log.d("checkChloe","selectMember=$member, position=$position")
//        checkedMemberPosition.value = position
//        _member.value = member
//
//        if(_member.value!=null){deleteList?.add(_member.value!!)}
//        Log.d("checkChloe","_memberChecked.value=${_member.value}, paymentStatus=${_member.value?.paymentStatus}")
//        Log.d("checkChloe","deleteList=${deleteList}")
//    }
//
//    fun removeCheckMember(member: Order, position: Int) {
//        Log.d("checkChloe","remove selectMember=$member, position=$position")
//        checkedMemberPosition.value = position
//        _member.value = member
//        if(_member.value!=null){deleteList?.remove(_member.value!!)}
//        Log.d("checkChloe","remove _memberChecked.value=${_member.value}, paymentStatus=${_member.value?.paymentStatus}")
//        Log.d("checkChloe","deleteList=${deleteList}")
//    }
//fun check(){
//    _isChecked.value = true
//    _order.value!![checkedMemberPosition.value!!]
//}
//fun unCheck(){
//    _isChecked.value = false
//}
//先把現有的order都加到list
//        if (_order.value != null) {
//             orderList = mutableListOf()
//            if (orderList.isNullOrEmpty()) {
//                for (i in _order.value!!) {
//
//                    orderList?.add(i!!)
//                }
//            }
//                Log.d("Chloe", "after order value is $orderList")
//                //再把上面有勾起來的都刪掉,並改變現有的order
//                if (deleteList != null) {
//                    for (i in deleteList) {
//                        orderList?.remove(i)
//                        deleteList.remove(i)
//                        _order.value = orderList
//                    }
//
//                }
//            }

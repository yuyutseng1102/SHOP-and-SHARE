package com.chloe.shopshare.data.source

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.chloe.shopshare.data.*
import com.google.firebase.auth.FirebaseAuth

/**
 *
 * Interface to the Publisher layers.
 */

interface Repository {

    /** SIGN IN **/
    suspend fun signInWithGoogle(idToken: String): Result<User>
    suspend fun getUserProfile(userId: String): Result<User>

    /** HOME **/
    suspend fun getOpeningShop(): Result<List<Shop>>
    suspend fun getAllRequest(): Result<List<Request>>

    /** Detail Shop **/
    suspend fun getDetailShop(shopId: String): Result<Shop>
    fun getLiveDetailShop(shopId: String): MutableLiveData<Shop>

    /** Detail Order **/
    suspend fun getOrderOfShop(shopId: String): Result<List<Order>>
    fun getLiveOrderOfShop(shopId: String): MutableLiveData<List<Order>>

    /** Detail Request **/
    fun getLiveDetailRequest(requestId: String): MutableLiveData<Request>

    /** Shop Host **/
    suspend fun postShop(shop: Shop): Result<PostHostResult>
    suspend fun uploadImage(uri: Uri,folder:String): Result<String>

    /** Shop Manage **/
    suspend fun getMyShop(userId:String): Result<List<Shop>>
    suspend fun deleteOrder(shopId: String, order: Order): Result<Boolean>
    suspend fun updateShopStatus(shopId: String, shopStatus:Int): Result<Boolean>
    suspend fun updateOrderStatus(shopId: String, paymentStatus: Int): Result<Boolean>

    /** Shop Request **/
    suspend fun postRequest(request: Request): Result<Boolean>
    suspend fun updateRequestHost(requestId: String, hostId: String): Result<Boolean>
    suspend fun updateRequestMember(requestId: String, memberId: String): Result<Boolean>

    /** Shop Manage **/
    suspend fun getMyRequest(userId:String): Result<List<Request>>

    /** Participate **/
    suspend fun postOrder(shopId: String, order: Order): Result<PostOrderResult>


    /** Notify **/
    suspend fun addSubscribe(userId: String, shopId: String): Result<Boolean>
    suspend fun removeSubscribe(userId: String, shopId: String): Result<Boolean>
    suspend fun postShopNotifyToMember(notify: Notify): Result<Boolean>
    suspend fun postOrderNotifyToMember(orderList: List<Order>, notify: Notify): Result<Boolean>
    suspend fun postNotifyToHost(hostId: String, notify: Notify): Result<Boolean>
    fun getLiveNotify(userId: String): MutableLiveData<List<Notify>>

}
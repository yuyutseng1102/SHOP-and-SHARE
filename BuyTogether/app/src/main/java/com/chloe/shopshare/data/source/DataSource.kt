package com.chloe.shopshare.data.source

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.chloe.shopshare.data.*
import com.google.firebase.auth.FirebaseAuth

/**
 *
 * Main entry point for accessing Publisher sources.
 */
interface DataSource {

    /** SIGN IN **/
    suspend fun signInWithGoogle(idToken: String): Result<User>
    suspend fun getUserProfile(userId: String): Result<User>

    /** HOME **/
    suspend fun getAllShop(): Result<List<Shop>>
    suspend fun getAllOpeningShop(): Result<List<Shop>>
    suspend fun getAllRequest(): Result<List<Request>>
    suspend fun getAllFinishedRequest(): Result<List<Request>>

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
    suspend fun getMyShopByStatus(userId:String, status:List<Int>): Result<List<Shop>>
    suspend fun deleteOrder(shopId: String, order: Order): Result<Boolean>
    suspend fun updateShopStatus(shopId: String, shopStatus:Int): Result<Boolean>
    suspend fun updateOrderStatus(shopId: String, paymentStatus: Int): Result<Boolean>

    /** Shop Request **/
    suspend fun postRequest(request: Request): Result<Boolean>
    suspend fun updateRequestHost(requestId: String,shopId: String , hostId: String): Result<Boolean>
    suspend fun updateRequestMember(requestId: String, memberId: String): Result<Boolean>

    /** My Order **/
    suspend fun getMyOrder(userId:String, status:List<Int>): Result<List<MyOrder>>
    suspend fun getDetailOrder(shopId: String, orderId:String): Result<Order>
    suspend fun getShopByOrder(orderId:String): Result<List<Shop>>

    /** My Request **/
    suspend fun getMyRequest(userId:String): Result<List<Request>>
    suspend fun getMyFinishedRequest(userId:String): Result<List<Request>>
    suspend fun getMyOngoingRequest(userId:String): Result<List<Request>>

    /** Participate **/
    suspend fun postOrder(shopId: String, order: Order): Result<PostOrderResult>
    suspend fun increaseOrderSize(shopId: String): Result<Boolean>
    suspend fun decreaseOrderSize(shopId: String, orderSize: Int): Result<Boolean>

    /** Like **/
    suspend fun getShopDetailLiked(shopIdList: List<String>): Result<List<Shop>>

    /** Notify **/
    suspend fun addShopLiked(userId: String, shopId: String): Result<Boolean>
    suspend fun removeShopLiked(userId: String, shopId: String): Result<Boolean>
    suspend fun postShopNotifyToMember(notify: Notify): Result<Boolean>
    suspend fun postRequestNotifyToMember(notify: Notify): Result<Boolean>
    suspend fun postOrderNotifyToMember(orderList: List<Order>, notify: Notify): Result<Boolean>
    suspend fun postNotifyToHost(hostId: String, notify: Notify): Result<Boolean>
    fun getLiveNotify(userId: String): MutableLiveData<List<Notify>>

    /** CHAT ROOM **/
    suspend fun getChatRoom(myId: String, friendId: String): Result<ChatRoom>
    fun getRoomMessage(roomId: String): MutableLiveData<List<Message>>
    suspend fun sendMessage(chatRoomId: String, message: Message): Result<Boolean>
}
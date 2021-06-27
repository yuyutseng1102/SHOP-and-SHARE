package com.chloe.shopshare.data.source

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.chloe.shopshare.data.*

/**
 *
 * Main entry point for accessing Publisher sources.
 */
interface DataSource {

    /** Login **/
    suspend fun signInWithGoogle(idToken: String): Result<User>
    suspend fun getUserProfile(userId: String): Result<User>

    /** Home **/
    suspend fun getAllShop(): Result<List<Shop>>
    suspend fun getAllOpeningShop(): Result<List<Shop>>
    suspend fun getHotShopByType(category: Int): Result<List<Shop>>
    suspend fun getNewShop(): Result<List<Shop>>
    suspend fun getAllRequest(): Result<List<Request>>
    suspend fun getAllFinishedRequest(): Result<List<Request>>

    /** Search **/
    suspend fun getShopByCategory(category: Int): Result<List<Shop>>
    suspend fun getShopByCountry(country: Int): Result<List<Shop>>
    suspend fun getShopByCategoryAndCountry(category: Int, country: Int): Result<List<Shop>>

    /** Detail Shop **/
    suspend fun getDetailShop(shopId: String): Result<Shop>
    fun getLiveDetailShop(shopId: String): MutableLiveData<Shop>

    /** Detail Order **/
    suspend fun getOrderOfShop(shopId: String): Result<List<Order>>
    fun getLiveOrderOfShop(shopId: String): MutableLiveData<List<Order>>

    /** Detail Request **/
    fun getLiveDetailRequest(requestId: String): MutableLiveData<Request>

    /** Host **/
    suspend fun postShop(shop: Shop): Result<PostHostResult>
    suspend fun uploadImage(uri: Uri, folder: String): Result<String>

    /** Shop Manage **/
    suspend fun getMyShop(userId: String): Result<List<Shop>>
    suspend fun getMyShopByStatus(userId: String, status: List<Int>): Result<List<Shop>>
    suspend fun deleteOrder(shopId: String, order: Order): Result<Boolean>
    suspend fun updateShopStatus(shopId: String, shopStatus: Int): Result<Boolean>
    suspend fun updateOrderStatus(shopId: String, paymentStatus: Int): Result<Boolean>

    /** Shop Request **/
    suspend fun postRequest(request: Request): Result<Boolean>
    suspend fun updateRequestHost(requestId: String, shopId: String, hostId: String): Result<Boolean>
    suspend fun updateRequestMember(requestId: String, memberId: String): Result<Boolean>

    /** Order Tracking **/
    suspend fun getMyOrder(userId: String, status: List<Int>): Result<List<MyOrder>>
    suspend fun getDetailOrder(shopId: String, orderId: String): Result<Order>

    /** My Request **/
    suspend fun getMyRequest(userId: String): Result<List<Request>>
    suspend fun getMyFinishedRequest(userId: String): Result<List<Request>>
    suspend fun getMyOngoingRequest(userId: String): Result<List<Request>>

    /** Participate **/
    suspend fun postOrder(shopId: String, order: Order): Result<PostOrderResult>
    suspend fun increaseOrderSize(shopId: String): Result<Boolean>
    suspend fun decreaseOrderSize(shopId: String, orderSize: Int): Result<Boolean>

    /** Like **/
    suspend fun getShopDetailLiked(shopIdList: List<String>): Result<List<Shop>>
    suspend fun addShopLiked(userId: String, shopId: String): Result<Boolean>
    suspend fun removeShopLiked(userId: String, shopId: String): Result<Boolean>

    /** Notify **/
    suspend fun postShopNotifyToMember(notify: Notify): Result<Boolean>
    suspend fun postRequestNotifyToMember(notify: Notify): Result<Boolean>
    suspend fun postOrderNotifyToMember(orderList: List<Order>, notify: Notify): Result<Boolean>
    suspend fun postNotifyToHost(hostId: String, notify: Notify): Result<Boolean>
    suspend fun getMyNotify(userId: String): Result<List<Notify>>
    suspend fun updateNotifyChecked(userId: String, notifyId: String): Result<Boolean>
    suspend fun deleteNotify(userId: String, notify: Notify): Result<Boolean>
    fun getLiveNewNotify(userId: String): MutableLiveData<List<Notify>>

    /** Chat **/
    fun getMyLiveChatList(myId: String): MutableLiveData<List<ChatRoom>>
    suspend fun getMyChatList(myId: String): Result<List<ChatRoom>>
    suspend fun getChatRoom(myId: String, friendId: String): Result<ChatRoom>
    fun getRoomMessage(roomId: String): MutableLiveData<List<Message>>
    suspend fun sendMessage(chatRoomId: String, message: Message): Result<Boolean>
}
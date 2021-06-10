package com.chloe.shopshare.data.source.local

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.chloe.shopshare.data.*
import com.chloe.shopshare.data.source.DataSource
import com.google.firebase.auth.FirebaseAuth


class LocalDataSource(val context: Context): DataSource {


    override suspend fun signInWithGoogle(idToken: String): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserProfile(userId: String): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllShop(): Result<List<Shop>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllOpeningShop(): Result<List<Shop>> {
        TODO("Not yet implemented")
    }

    override suspend fun getHotShopByType(category: Int): Result<List<Shop>> {
        TODO("Not yet implemented")
    }

    override suspend fun getNewShop(): Result<List<Shop>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllRequest(): Result<List<Request>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllFinishedRequest(): Result<List<Request>> {
        TODO("Not yet implemented")
    }

    override suspend fun getShopByCategory(category: Int): Result<List<Shop>> {
        TODO("Not yet implemented")
    }

    override suspend fun getShopByCountry(country: Int): Result<List<Shop>> {
        TODO("Not yet implemented")
    }

    override suspend fun getShopByCategoryAndCountry(
        category: Int,
        country: Int
    ): Result<List<Shop>> {
        TODO("Not yet implemented")
    }


    override suspend fun getDetailShop(shopId: String): Result<Shop> {
        TODO("Not yet implemented")
    }

    override fun getLiveDetailShop(shopId: String): MutableLiveData<Shop> {
        TODO("Not yet implemented")
    }

    override suspend fun getMyShop(userId: String): Result<List<Shop>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMyShopByStatus(userId: String, status: List<Int>): Result<List<Shop>> {
        TODO("Not yet implemented")
    }

    override suspend fun getOrderOfShop(shopId: String): Result<List<Order>> {
        TODO("Not yet implemented")
    }


    override fun getLiveOrderOfShop(shopId: String): MutableLiveData<List<Order>> {
        TODO("Not yet implemented")
    }

    override fun getLiveDetailRequest(requestId: String): MutableLiveData<Request> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteOrder(shopId: String, order: Order): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateShopStatus(shopId: String, shopStatus: Int): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateOrderStatus(
        shopId: String,
        paymentStatus: Int
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postRequest(request: Request): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateRequestHost(requestId: String, shopId: String , hostId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateRequestMember(requestId: String, memberId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getMyOrder(userId: String, status:List<Int>): Result<List<MyOrder>> {
        TODO("Not yet implemented")
    }

    override suspend fun getDetailOrder(shopId: String, orderId: String): Result<Order> {
        TODO("Not yet implemented")
    }

    override suspend fun getShopByOrder(orderId:String): Result<List<Shop>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMyRequest(userId: String): Result<List<Request>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMyFinishedRequest(
        userId: String
    ): Result<List<Request>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMyOngoingRequest(userId: String): Result<List<Request>> {
        TODO("Not yet implemented")
    }

    override suspend fun postShop(shop: Shop): Result<PostHostResult> {
        TODO("Not yet implemented")
    }

    override suspend fun postOrder(shopId: String, order: Order): Result<PostOrderResult> {
        TODO("Not yet implemented")
    }

    override suspend fun increaseOrderSize(shopId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun decreaseOrderSize(shopId: String, orderSize: Int): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getShopDetailLiked(shopIdList: List<String>): Result<List<Shop>> {
        TODO("Not yet implemented")
    }


    override suspend fun uploadImage(uri: Uri, folder: String): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun addShopLiked(userId: String, shopId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun removeShopLiked(userId: String, shopId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postShopNotifyToMember(notify: Notify): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postRequestNotifyToMember(notify: Notify): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postOrderNotifyToMember(
        orderList: List<Order>,
        notify: Notify
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postNotifyToHost(hostId: String, notify: Notify): Result<Boolean> {
        TODO("Not yet implemented")
    }


    override fun getLiveNotify(userId: String): MutableLiveData<List<Notify>> {
        TODO("Not yet implemented")
    }

    override fun getMyAllChatRoom(myId: String): MutableLiveData<List<ChatRoom>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMyChatList(myId: String): Result<List<ChatRoom>> {
        TODO("Not yet implemented")
    }

    override suspend fun getChatRoom(myId: String, friendId: String): Result<ChatRoom> {
        TODO("Not yet implemented")
    }

    override fun getRoomMessage(roomId: String): MutableLiveData<List<Message>> {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(chatRoomId: String, message: Message): Result<Boolean> {
        TODO("Not yet implemented")
    }
}
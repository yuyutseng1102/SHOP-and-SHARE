package com.chloe.shopshare.data.source

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.chloe.shopshare.data.*


class DefaultRepository (private val remoteDataSource: DataSource,
                         private val localDataSource: DataSource
) : Repository {
    override suspend fun signInWithGoogle(idToken: String): Result<User> {
        return remoteDataSource.signInWithGoogle(idToken)
    }

    override suspend fun getUserProfile(userId: String): Result<User> {
        return remoteDataSource.getUserProfile(userId)
    }


    override suspend fun getOpeningShop(): Result<List<Shop>> {
        return remoteDataSource.getOpeningShop()
    }

    override suspend fun getAllRequest(): Result<List<Request>> {
        return remoteDataSource.getAllRequest()
    }


    override suspend fun getDetailShop(shopId: String): Result<Shop> {
        return remoteDataSource.getDetailShop(shopId)
    }


    override fun getLiveDetailShop(shopId: String): MutableLiveData<Shop> {
        return remoteDataSource.getLiveDetailShop(shopId)
    }

    override suspend fun getMyShop(userId: String): Result<List<Shop>> {
        return remoteDataSource.getMyShop(userId)
    }

    override suspend fun getMyShopByStatus(userId: String, status: List<Int>): Result<List<Shop>> {
        return remoteDataSource.getMyShopByStatus(userId, status)
    }

    override suspend fun getOrderOfShop(shopId: String): Result<List<Order>> {
        return remoteDataSource.getOrderOfShop(shopId)
    }


    override fun getLiveOrderOfShop(shopId: String): MutableLiveData<List<Order>> {
        return remoteDataSource.getLiveOrderOfShop(shopId)
    }

    override fun getLiveDetailRequest(requestId: String): MutableLiveData<Request> {
        return remoteDataSource.getLiveDetailRequest(requestId)
    }


    override suspend fun deleteOrder(shopId: String, order: Order): Result<Boolean>  {
        return remoteDataSource.deleteOrder(shopId,order)
    }

    override suspend fun updateShopStatus(shopId: String, shopStatus: Int): Result<Boolean> {
        return remoteDataSource.updateShopStatus(shopId,shopStatus)
    }

    override suspend fun updateOrderStatus(shopId: String, paymentStatus: Int): Result<Boolean> {
        return remoteDataSource.updateOrderStatus(shopId,paymentStatus)
    }

    override suspend fun postRequest(request: Request): Result<Boolean> {
        return remoteDataSource.postRequest(request)
    }

    override suspend fun updateRequestHost(requestId: String,shopId: String ,  hostId: String): Result<Boolean> {
        return remoteDataSource.updateRequestHost(requestId,shopId, hostId)
    }

    override suspend fun updateRequestMember(requestId: String, memberId: String): Result<Boolean> {
        return remoteDataSource.updateRequestMember(requestId, memberId)
    }

    override suspend fun getMyOrder(userId: String, status:List<Int>): Result<List<MyOrder>> {
        return remoteDataSource.getMyOrder(userId, status)
    }

    override suspend fun getDetailOrder(shopId: String, orderId: String): Result<Order> {
        return remoteDataSource.getDetailOrder(shopId, orderId)
    }

    override suspend fun getShopByOrder(orderId:String): Result<List<Shop>> {
        return remoteDataSource.getShopByOrder(orderId)
    }

    override suspend fun getMyRequest(userId: String): Result<List<Request>> {
        return remoteDataSource.getMyRequest(userId)
    }

    override suspend fun getMyFinishedRequest(userId: String): Result<List<Request>> {
        return remoteDataSource.getMyFinishedRequest(userId)
    }

    override suspend fun getMyOngoingRequest(userId: String): Result<List<Request>> {
        return remoteDataSource.getMyOngoingRequest(userId)
    }

    override suspend fun postShop(shop: Shop): Result<PostHostResult> {
        return remoteDataSource.postShop(shop)
    }

    override suspend fun postOrder(shopId: String, order: Order): Result<PostOrderResult> {
        return remoteDataSource.postOrder(shopId, order)
    }

    override suspend fun increaseOrderSize(shopId: String): Result<Boolean> {
        return remoteDataSource.increaseOrderSize(shopId)
    }

    override suspend fun getShopDetailLiked(shopIdList: List<String>): Result<List<Shop>> {
        return remoteDataSource.getShopDetailLiked(shopIdList)
    }

    override suspend fun uploadImage(uri: Uri,folder:String): Result<String> {
        return remoteDataSource.uploadImage(uri,folder)
    }

    override suspend fun addShopLiked(userId: String, shopId: String): Result<Boolean> {
        return remoteDataSource.addShopLiked(userId,shopId)
    }

    override suspend fun removeShopLiked(userId: String, shopId: String): Result<Boolean> {
        return remoteDataSource.removeShopLiked(userId,shopId)
    }

    override suspend fun postShopNotifyToMember(notify: Notify): Result<Boolean> {
        return remoteDataSource.postShopNotifyToMember(notify)
    }

    override suspend fun postRequestNotifyToMember(notify: Notify): Result<Boolean> {
        return remoteDataSource.postRequestNotifyToMember(notify)
    }

    override suspend fun postOrderNotifyToMember(orderList: List<Order>, notify: Notify): Result<Boolean> {
        return remoteDataSource.postOrderNotifyToMember(orderList, notify)
    }

    override suspend fun postNotifyToHost(hostId: String, notify: Notify): Result<Boolean> {
        return remoteDataSource.postNotifyToHost(hostId, notify)
    }


    override fun getLiveNotify(userId: String): MutableLiveData<List<Notify>> {
        return remoteDataSource.getLiveNotify(userId)
    }


}
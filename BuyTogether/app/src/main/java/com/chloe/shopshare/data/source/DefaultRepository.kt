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



    override suspend fun getDetailShop(shopId: String): Result<Shop> {
        return remoteDataSource.getDetailShop(shopId)
    }

//    override suspend fun getLiveDetailShop(shopId: String): Result<Shop> {
//        return remoteDataSource.getLiveDetailShop(shopId)
//    }

    override fun getLiveDetailShop(shopId: String): MutableLiveData<Shop> {
        return remoteDataSource.getLiveDetailShop(shopId)
    }

    override suspend fun getMyShop(userId: String): Result<List<Shop>> {
        return remoteDataSource.getMyShop(userId)
    }

    override suspend fun getOrderOfShop(shopId: String): Result<List<Order>> {
        return remoteDataSource.getOrderOfShop(shopId)
    }

//    override suspend fun getLiveOrderOfShop(shopId: String): Result<List<Order>> {
//        return remoteDataSource.getLiveOrderOfShop(shopId)
//    }

    override fun getLiveOrderOfShop(shopId: String): MutableLiveData<List<Order>> {
        return remoteDataSource.getLiveOrderOfShop(shopId)
    }


    override suspend fun deleteOrder(shopId: String, order: Order): Result<Boolean>  {
        return remoteDataSource.deleteOrder(shopId,order)
    }

    override suspend fun updateShopStatus(shopId: String, shopStatus: Int): Result<Boolean> {
        return remoteDataSource.updateShopStatus(shopId,shopStatus)
    }

    override suspend fun postShop(shop: Shop): Result<Boolean> {
        return remoteDataSource.postShop(shop)
    }

    override suspend fun postOrder(shopId: String, order: Order): Result<Boolean> {
        return remoteDataSource.postOrder(shopId, order)
    }

    override suspend fun uploadImage(uri: Uri,folder:String): Result<String> {
        return remoteDataSource.uploadImage(uri,folder)
    }

    override suspend fun addSubscribe(userId: String, shopId: String): Result<Boolean> {
        return remoteDataSource.addSubscribe(userId,shopId)
    }

    override suspend fun removeSubscribe(userId: String, shopId: String): Result<Boolean> {
        return remoteDataSource.removeSubscribe(userId,shopId)
    }

    override suspend fun postShopNotifyToMember(notify: Notify): Result<Boolean> {
        return remoteDataSource.postShopNotifyToMember(notify)
    }

    override suspend fun postNotifyToHost(hostId: String, notify: Notify): Result<Boolean> {
        return remoteDataSource.postNotifyToHost(hostId, notify)
    }


    override fun getLiveNotify(userId: String): MutableLiveData<List<Notify>> {
        return remoteDataSource.getLiveNotify(userId)
    }


}
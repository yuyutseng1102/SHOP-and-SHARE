package com.chloe.shopshare.data.source

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.chloe.shopshare.data.Order
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.Result


class DefaultRepository (private val remoteDataSource: DataSource,
                         private val localDataSource: DataSource
) : Repository {
    override suspend fun getOpeningShop(): Result<List<Shop>> {
        return remoteDataSource.getOpeningShop()
    }

    override fun getLiveShop(): MutableLiveData<Boolean>{
        return remoteDataSource.getLiveShop()
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

    override suspend fun postShop(shop: Shop): Result<Boolean> {
        return remoteDataSource.postShop(shop)
    }

    override suspend fun postOrder(shopId: String, order: Order): Result<Boolean> {
        return remoteDataSource.postOrder(shopId, order)
    }

    override suspend fun uploadImage(uri: Uri,folder:String): Result<String> {
        return remoteDataSource.uploadImage(uri,folder)
    }


}
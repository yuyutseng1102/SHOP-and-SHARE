package com.chloe.shopshare.data.source

import android.net.Uri
import com.chloe.shopshare.data.Order
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.Result


class DefaultRepository (private val remoteDataSource: DataSource,
                         private val localDataSource: DataSource
) : Repository {
    override suspend fun getMyShop(userId: String): Result<List<Shop>> {
        return remoteDataSource.getMyShop(userId)
    }

    override suspend fun getOrderOfShop(shopId: String): Result<List<Order>> {
        return remoteDataSource.getOrderOfShop(shopId)
    }

    override suspend fun deleteOrder(shopId: String, order: Order): Result<Boolean>  {
        return remoteDataSource.deleteOrder(shopId,order)
    }

    override suspend fun postShop(shop: Shop): Result<Boolean> {
        return remoteDataSource.postShop(shop)
    }

    override suspend fun sendOrder(order: Order): Result<Boolean> {
        return remoteDataSource.sendOrder(order)
    }

    override suspend fun uploadImage(uri: Uri,folder:String): Result<String> {
        return remoteDataSource.uploadImage(uri,folder)
    }


}
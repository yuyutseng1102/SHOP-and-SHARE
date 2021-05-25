package com.chloe.shopshare.data.source.local

import android.content.Context
import android.net.Uri
import com.chloe.shopshare.data.Order
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.source.DataSource
import com.chloe.shopshare.data.Result


class LocalDataSource(val context: Context): DataSource {
    override suspend fun getMyShop(userId: String): Result<List<Shop>> {
        TODO("Not yet implemented")
    }

    override suspend fun getOrderOfShop(shopId: String): Result<List<Order>> {
        TODO("Not yet implemented")
    }

    override suspend fun postShop(shop: Shop): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun sendOrder(order: Order): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadImage(uri: Uri, folder: String): Result<String> {
        TODO("Not yet implemented")
    }
}
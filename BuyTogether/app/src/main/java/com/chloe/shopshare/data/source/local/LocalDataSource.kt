package com.chloe.shopshare.data.source.local

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.chloe.shopshare.data.Order
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.source.DataSource
import com.chloe.shopshare.data.Result


class LocalDataSource(val context: Context): DataSource {
    override suspend fun getOpeningShop(): Result<List<Shop>> {
        TODO("Not yet implemented")
    }

    override fun getLiveShop(): MutableLiveData<Boolean> {
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

    override suspend fun getOrderOfShop(shopId: String): Result<List<Order>> {
        TODO("Not yet implemented")
    }

    override fun getLiveOrderOfShop(shopId: String): MutableLiveData<List<Order>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteOrder(shopId: String, order: Order): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postShop(shop: Shop): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postOrder(shopId: String, order: Order): Result<Boolean> {
        TODO("Not yet implemented")
    }


    override suspend fun uploadImage(uri: Uri, folder: String): Result<String> {
        TODO("Not yet implemented")
    }
}
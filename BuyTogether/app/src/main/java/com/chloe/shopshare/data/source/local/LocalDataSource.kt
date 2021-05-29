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

    override suspend fun getOpeningShop(): Result<List<Shop>> {
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

    override suspend fun updateShopStatus(shopId: String, shopStatus: Int): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateOrderStatus(
        shopId: String,
        paymentStatus: Int
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postShop(shop: Shop): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postOrder(shopId: String, order: Order): Result<PostOrderResult> {
        TODO("Not yet implemented")
    }


    override suspend fun uploadImage(uri: Uri, folder: String): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun addSubscribe(userId: String, shopId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun removeSubscribe(userId: String, shopId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postShopNotifyToMember(notify: Notify): Result<Boolean> {
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
}
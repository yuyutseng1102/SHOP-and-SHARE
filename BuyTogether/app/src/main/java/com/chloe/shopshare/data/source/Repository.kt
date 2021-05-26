package com.chloe.shopshare.data.source

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.chloe.shopshare.data.Order
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.Result

/**
 *
 * Interface to the Publisher layers.
 */

interface Repository {


    suspend fun getOpeningShop(): Result<List<Shop>>
    fun getLiveShop(): MutableLiveData<Boolean>

    suspend fun getDetailShop(shopId: String): Result<Shop>
//    suspend fun getLiveDetailShop(shopId: String): Result<Shop>
    fun getLiveDetailShop(shopId: String): MutableLiveData<Shop>

    suspend fun getOrderOfShop(shopId: String): Result<List<Order>>
//    suspend fun getLiveOrderOfShop(shopId: String): Result<List<Order>>
    fun getLiveOrderOfShop(shopId: String): MutableLiveData<List<Order>>

    suspend fun getMyShop(userId:String): Result<List<Shop>>

    suspend fun deleteOrder(shopId: String, order: Order): Result<Boolean>
    suspend fun postShop(shop: Shop): Result<Boolean>
    suspend fun postOrder(shopId: String, order: Order): Result<Boolean>
    suspend fun uploadImage(uri: Uri,folder:String): Result<String>

}
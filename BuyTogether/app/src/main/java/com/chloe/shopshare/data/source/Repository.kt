package com.chloe.shopshare.data.source

import android.net.Uri
import com.chloe.shopshare.data.Order
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.Result

/**
 *
 * Interface to the Publisher layers.
 */

interface Repository {

    suspend fun getMyShop(userId:String): Result<List<Shop>>
    suspend fun getOrderOfShop(shopId: String): Result<List<Order>>
    suspend fun deleteOrder(shopId: String, order: Order): Result<Boolean>
    suspend fun postShop(shop: Shop): Result<Boolean>
    suspend fun sendOrder(order: Order): Result<Boolean>
    suspend fun uploadImage(uri: Uri,folder:String): Result<String>

}
package com.chloe.shopshare.data.source

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.chloe.shopshare.data.*
import com.google.firebase.auth.FirebaseAuth

/**
 *
 * Interface to the Publisher layers.
 */

interface Repository {

    suspend fun signInWithGoogle(idToken: String): Result<User>
    suspend fun getUserProfile(userId: String): Result<User>

    suspend fun getOpeningShop(): Result<List<Shop>>

    suspend fun getDetailShop(shopId: String): Result<Shop>
    fun getLiveDetailShop(shopId: String): MutableLiveData<Shop>
//    suspend fun getLiveDetailShop(shopId: String): Result<Shop>


    suspend fun getOrderOfShop(shopId: String): Result<List<Order>>
    fun getLiveOrderOfShop(shopId: String): MutableLiveData<List<Order>>
//    suspend fun getLiveOrderOfShop(shopId: String): Result<List<Order>>


    suspend fun getMyShop(userId:String): Result<List<Shop>>
    suspend fun deleteOrder(shopId: String, order: Order): Result<Boolean>
    suspend fun updateShopStatus(shopId: String, shopStatus:Int): Result<Boolean>

    suspend fun postShop(shop: Shop): Result<Boolean>
    suspend fun postOrder(shopId: String, order: Order): Result<Boolean>
    suspend fun uploadImage(uri: Uri,folder:String): Result<String>

    //Notify

    suspend fun addSubscribe(userId: String, shopId: String): Result<Boolean>
    suspend fun removeSubscribe(userId: String, shopId: String): Result<Boolean>
    suspend fun postShopNotifyToMember(notify: Notify): Result<Boolean>
//    suspend fun postOrderNotifyToMember(notify: Notify): Result<Boolean>
    suspend fun postNotifyToHost(hostId: String, notify: Notify): Result<Boolean>
    fun getLiveNotify(userId: String): MutableLiveData<List<Notify>>

}
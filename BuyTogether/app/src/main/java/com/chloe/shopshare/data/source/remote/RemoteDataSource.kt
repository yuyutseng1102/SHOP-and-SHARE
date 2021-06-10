package com.chloe.shopshare.data.source.remote

import android.icu.util.Calendar
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.Order
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.source.DataSource
import com.chloe.shopshare.data.Result
import com.google.common.io.Files.getFileExtension
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.storage.FirebaseStorage
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


object RemoteDataSource : DataSource {
    private const val PATH_SHOP = "shop"
    private const val PATH_ORDER = "order"
    private const val KEY_CREATED_TIME = "time"
    private const val FIREBASE_STORAGE_PATH = "gs://shopshare-592fa.appspot.com/"

    override suspend fun getOpeningShop(): Result<List<Shop>> = suspendCoroutine { continuation ->
        val shopDataBase = FirebaseFirestore.getInstance().collection(PATH_SHOP)
        shopDataBase
//            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val shopList = mutableListOf<Shop>()
                    for (document in task.result!!) {
                        Log.d("Chloe", document.id + " => " + document.data)
                        val shop = document.toObject(Shop::class.java)
                        shopList.add(shop)
                    }
                    continuation.resume(Result.Success(shopList))
                } else {
                    task.exception?.let {

                        Log.w(
                            "Chloe",
                            "[${this::class.simpleName}] Error getting documents. ${it.message}"
                        )
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
                }

            }
    }

    override suspend fun getDetailShop(shopId: String): Result<Shop> =
        suspendCoroutine { continuation ->
            val shopDataBase =
                FirebaseFirestore.getInstance().collection(PATH_SHOP).document(shopId)
            Log.d("Chloe", "shopDataBase= $shopDataBase")
            shopDataBase
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        Log.d("Chloe", document?.id + " => " + document?.data)
                        val shop: Shop? = document?.toObject(Shop::class.java)
                        Log.d("Chloe", "shop = $shop")
                        shop?.let {
                            continuation.resume(Result.Success(shop))
                        }

                    } else {
                        task.exception?.let {

                            Log.w(
                                "Chloe",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
                    }

                }
        }
//    override suspend fun getLiveDetailShop(shopId: String): Result<Shop> = suspendCoroutine { continuation ->
//
//        val shopDataBase = FirebaseFirestore.getInstance().collection(PATH_SHOP).document(shopId)
//        shopDataBase
//            .addSnapshotListener { snapshot, exception ->
//
//                Log.i("Chloe", "addSnapshotListener detect")
//
//                exception?.let {
//                    Log.w(
//                        "Chloe",
//                        "[${this::class.simpleName}] Error getting documents. ${it.message}"
//                    )
//                    continuation.resume(Result.Error(it))
//                    return@addSnapshotListener
//                }
//                val shop = snapshot?.toObject(Shop::class.java)
//                shop?.let {
//                    continuation.resume(Result.Success(shop))
//                }
//
//            }
//    }

    override fun getLiveDetailShop(shopId: String): MutableLiveData<Shop> {

        val liveData = MutableLiveData<Shop>()

        val shopDataBase = FirebaseFirestore.getInstance().collection(PATH_SHOP).document(shopId)
        shopDataBase
            .addSnapshotListener { snapshot, exception ->

                Log.i("Chloe", "addSnapshotListener detect")

                exception?.let {
                    Log.w(
                        "Chloe",
                        "[${this::class.simpleName}] Error getting documents. ${it.message}"
                    )
                }
                val shop = snapshot?.toObject(Shop::class.java)
                shop?.let { liveData.value = it }
                Log.d("Chloe", snapshot!!.id  + " => " + snapshot.data)
            }
        Log.d("Chloe","livedata = ${liveData.value}")
        return liveData
    }


    override suspend fun getMyShop(userId: String): Result<List<Shop>> =
        suspendCoroutine { continuation ->
            val shopDataBase = FirebaseFirestore.getInstance().collection(PATH_SHOP)
            shopDataBase
                .whereEqualTo("userId", userId)
                .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val shopList = mutableListOf<Shop>()
                        for (document in task.result!!) {
                            Log.d("Chloe", document.id + " => " + document.data)
                            val shop = document.toObject(Shop::class.java)
                            shopList.add(shop)
                        }
                        continuation.resume(Result.Success(shopList))
                    } else {
                        task.exception?.let {

                            Log.w(
                                "Chloe",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
                    }

                }
        }

    override suspend fun getOrderOfShop(shopId: String): Result<List<Order>> =
        suspendCoroutine { continuation ->
            val shopDataBase =
                FirebaseFirestore.getInstance().collection(PATH_SHOP).document(shopId)

            shopDataBase
                .collection(PATH_ORDER)
                .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                            val orderList = mutableListOf<Order>()
                        for (document in task.result!!) {
                            Log.d("Chloe", document.id + " => " + document.data)
                            val order = document.toObject(Order::class.java)
                            orderList.add(order)
                        }
                        continuation.resume(Result.Success(orderList))
                    } else {
                        task.exception?.let {

                            Log.w(
                                "Chloe",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
                    }

                }
        }

//    override suspend fun getLiveOrderOfShop(shopId: String): Result<List<Order>> = suspendCoroutine { continuation ->
//
//        val shopDataBase = FirebaseFirestore.getInstance().collection(PATH_SHOP).document(shopId)
//
//        shopDataBase
//            .collection(PATH_ORDER)
//            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
//            .addSnapshotListener { snapshot, exception ->
//
//                Log.i("Chloe", "addSnapshotListener detect")
//
//                exception?.let {
//                    Log.w(
//                        "Chloe",
//                        "[${this::class.simpleName}] Error getting documents. ${it.message}"
//                    )
//                    continuation.resume(Result.Error(it))
//                    return@addSnapshotListener
//                }
//
//                val orderList = mutableListOf<Order>()
//                for (document in snapshot!!) {
//                    Log.d("Chloe", document.id + " => " + document.data)
//
//                    val order = document.toObject(Order::class.java)
//                    orderList.add(order)
//                }
//                continuation.resume(Result.Success(orderList))
//            }
//    }


    override fun getLiveOrderOfShop(shopId: String): MutableLiveData<List<Order>> {

        val liveData = MutableLiveData<List<Order>>()

        val shopDataBase = FirebaseFirestore.getInstance().collection(PATH_SHOP).document(shopId)
        shopDataBase
            .collection(PATH_ORDER)
            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->

                Log.i("Chloe", "addSnapshotListener detect")

                exception?.let {
                    Log.w(
                        "Chloe",
                        "[${this::class.simpleName}] Error getting documents. ${it.message}"
                    )
                }

                val orderList = mutableListOf<Order>()
                for (document in snapshot!!) {
                    Log.d("Chloe", document.id + " => " + document.data)

                    val order = document.toObject(Order::class.java)
                    orderList.add(order)
                }
                liveData.value = orderList
            }
        Log.d("Chloe","livedata = ${liveData.value}")
        return liveData
    }


    override suspend fun deleteOrder(shopId: String, order: Order): Result<Boolean> =
        suspendCoroutine { continuation ->
            val orderDataBase =
                FirebaseFirestore.getInstance().collection(PATH_SHOP).document(shopId)
                    .collection(PATH_ORDER)
            orderDataBase
                .document(order.id)
                .delete()
                .addOnSuccessListener {
                    Log.d("Chloe", "Delete: $order")

                    continuation.resume(Result.Success(true))
                }.addOnFailureListener {
                    Log.w(
                        "Chloe",
                        "[${this::class.simpleName}] Error getting documents. ${it.message}"
                    )
                    continuation.resume(Result.Error(it))
                }
        }

    override suspend fun updateShopStatus(shopId: String, shopStatus: Int): Result<Boolean> =
        suspendCoroutine { continuation ->
            val shopDataBase =
                FirebaseFirestore.getInstance().collection(PATH_SHOP).document(shopId)
            shopDataBase
                .update("status", shopStatus)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i("Chloe", "status is updated to : $shopStatus")
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {
                            Log.w(
                                "Chloe",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )

                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
                    }
                }
        }


    override suspend fun postShop(shop: Shop): Result<Boolean> = suspendCoroutine { continuation ->
        val shopDataBase = FirebaseFirestore.getInstance().collection(PATH_SHOP)
        val document = shopDataBase.document()

        shop.id = document.id
        shop.time = Calendar.getInstance().timeInMillis

        document
            .set(shop)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("Chloe", "postShop: $shop")
                    continuation.resume(Result.Success(true))
                } else {
                    task.exception?.let {

                        Log.i(
                            "Chloe",
                            "[${this::class.simpleName}] Error getting documents. ${it.message}"
                        )
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
                }
            }

    }

    override suspend fun postOrder(shopId: String, order: Order): Result<Boolean> =
        suspendCoroutine { continuation ->
            val orderDataBase = FirebaseFirestore.getInstance().collection(PATH_SHOP)
                .document(shopId).collection(PATH_ORDER)
            val document = orderDataBase.document()

            order.id = document.id
            order.time = Calendar.getInstance().timeInMillis

            document
                .set(order)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i("Chloe", "postOrder: $order")
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            Log.i(
                                "Chloe",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
                    }
                }

        }

    override suspend fun uploadImage(uri: Uri, folder: String): Result<String> =
        suspendCoroutine { continuation ->

            val storageReference = FirebaseStorage.getInstance(FIREBASE_STORAGE_PATH)
//        val databaseReference = FirebaseDatabase.getInstance(FIREBASE_STORAGE_PATH)
            val extension = getFileExtension(uri.path)
            val fileReference = storageReference.reference.child(
                "images/${folder}/${System.currentTimeMillis()}.${extension}"
            )
            fileReference.putFile(uri)
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    fileReference.downloadUrl
                }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(task.result.toString()))
                    } else {
                        task.exception?.let {
                            Log.w(
                                "Chloe",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
                    }
                }
        }
}
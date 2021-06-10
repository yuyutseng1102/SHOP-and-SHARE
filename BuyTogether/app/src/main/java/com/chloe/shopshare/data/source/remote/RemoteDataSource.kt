package com.chloe.shopshare.data.source.remote

import android.icu.util.Calendar
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.*
import com.chloe.shopshare.data.source.DataSource
import com.chloe.shopshare.ext.toDisplayNotifyMessage
import com.chloe.shopshare.notify.NotifyType
import com.google.common.io.Files.getFileExtension
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


object RemoteDataSource : DataSource {
    private const val PATH_REQUEST = "request"
    private const val PATH_USER = "user"
    private const val PATH_ORDER = "order"
    private const val PATH_SHOP = "shop"
    private const val PATH_NOTIFY = "notify"
    private const val KEY_CREATED_TIME = "time"
    private const val FIREBASE_STORAGE_PATH = "gs://shopshare-592fa.appspot.com/"

    override suspend fun signInWithGoogle(idToken: String): Result<User> =
        suspendCoroutine { continuation ->
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val auth = FirebaseAuth.getInstance()
            val userDataBase = FirebaseFirestore.getInstance().collection(PATH_USER)
            var userProfile = User()

            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Login", "signInWithCredential:success")
                        val user = auth.currentUser
                        user?.let {
                            userDataBase
                                .whereEqualTo("id",user.uid)
                                .get()
                                .addOnCompleteListener { documents ->
                                    if (documents.isSuccessful) {
                                        if (documents.result!!.isEmpty){
                                            userProfile = User(
                                                provider = "google",
                                                id = user.uid,
                                                name = user.displayName ?: "UserName",
                                                email = user.email,
                                                photo = user.photoUrl.toString()
                                            )
                                            userDataBase.document(user.uid).set(userProfile)
                                        }else{
                                            for (document in documents.result!!) {
                                                userProfile = document.toObject(User::class.java)
                                            }
                                            userProfile = userProfile
                                        }
                                        Log.d("Login", "userProfile = $userProfile")
                                        continuation.resume(Result.Success(userProfile))
                                    }else {
                                        task.exception?.let {
                                            Log.w(
                                                "Login",
                                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                                            )
                                            continuation.resume(Result.Error(it))
                                            return@addOnCompleteListener
                                        }
                                        continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
                                    }
                                }
                        }
                    } else {
                        task.exception?.let {
                            Log.w(
                                "Login",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
                    }
                }
        }

    override suspend fun getUserProfile(userId: String): Result<User> =
        suspendCoroutine { continuation ->
            val userDocument =
                FirebaseFirestore.getInstance().collection(PATH_USER).document(userId)
            userDocument
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        Log.d("Profile", document?.id + " => " + document?.data)
                        val user: User? = document?.toObject(User::class.java)
                        Log.d("Profile", "UserProfile = $user")
                        user?.let {
                            continuation.resume(Result.Success(user))
                        }
                    } else {
                        task.exception?.let {
                            Log.w(
                                "Profile",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
                    }
                }
        }



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


    override suspend fun getAllRequest(): Result<List<Request>> = suspendCoroutine { continuation ->
        val shopDataBase = FirebaseFirestore.getInstance().collection(PATH_REQUEST)
        shopDataBase
//            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val requestList = mutableListOf<Request>()
                    for (document in task.result!!) {
                        Log.d("HomeTag", document.id + " => " + document.data)
                        val request = document.toObject(Request::class.java)
                        requestList.add(request)
                    }
                    continuation.resume(Result.Success(requestList))
                } else {
                    task.exception?.let {

                        Log.w("HomeTag", "[${this::class.simpleName}] Error getting documents. ${it.message}")
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
                Log.d("Chloe", snapshot!!.id + " => " + snapshot.data)
            }
        Log.d("Chloe", "livedata = ${liveData.value}")
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

    override suspend fun getMyShopByStatus(userId: String, status: List<Int>): Result<List<Shop>> =
        suspendCoroutine { continuation ->
            val shopDataBase = FirebaseFirestore.getInstance().collection(PATH_SHOP)
            shopDataBase
                .whereEqualTo("userId", userId)
                .whereIn("status",status)
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
                            Log.d("Order", document.id + " => " + document.data)
                            val order = document.toObject(Order::class.java)
                            orderList.add(order)
                        }
                        continuation.resume(Result.Success(orderList))
                    } else {
                        task.exception?.let {

                            Log.w(
                                "Order",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
                    }

                }
        }


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
        Log.d("Chloe", "livedata = ${liveData.value}")
        return liveData
    }

    override fun getLiveDetailRequest(requestId: String): MutableLiveData<Request> {

        val liveData = MutableLiveData<Request>()

        val shopDataBase = FirebaseFirestore.getInstance().collection(PATH_REQUEST).document(requestId)
        shopDataBase
            .addSnapshotListener { snapshot, exception ->

                Log.i("RequestDetailTag", "addSnapshotListener detect")

                exception?.let {
                    Log.w(
                        "RequestDetailTag", "[${this::class.simpleName}] Error getting documents. ${it.message}"
                    )
                }
                val request = snapshot?.toObject(Request::class.java)
                request?.let { liveData.value = it }
                Log.d("RequestDetailTag", snapshot!!.id + " => " + snapshot.data)
            }
        Log.d("RequestDetailTag", "livedata = ${liveData.value}")
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

    override suspend fun updateOrderStatus(shopId: String, paymentStatus: Int): Result<Boolean> =
        suspendCoroutine { continuation ->
            val orderDataBase =
                FirebaseFirestore.getInstance().collection(PATH_SHOP).document(shopId)
                    .collection(PATH_ORDER)
            orderDataBase
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val totalTaskCount = task.result!!.size()
                        var count = 0
                        for (document in task.result!!) {
                            Log.d("Manage", document.id + " => " + document.data)
                            orderDataBase
                                .document(document.id)
                                .update("paymentStatus", paymentStatus)
                                .addOnCompleteListener { orderTask ->
                                    if (orderTask.isSuccessful) {
                                        Log.i("Manage", "status is updated to : $paymentStatus")
                                        Log.i("Manage", "count is  $count")
                                        count++
                                    }else{
                                        orderTask.exception?.let {
                                            Log.w("Manage", "[${this::class.simpleName}] Error getting documents. ${it.message}")
                                            count++
                                        }
                                        count++
                                    }
                                    }
                            Log.i("Manage", "count finally is  $count")
                            if (count == totalTaskCount){
                                continuation.resume(Result.Success(true))
                                }
                        }
                    } else {
                        task.exception?.let {
                            Log.w(
                                "Manage", "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )

                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
                    }
                }
        }

    override suspend fun postRequest(request: Request): Result<Boolean> = suspendCoroutine { continuation ->
        val requestDataBase = FirebaseFirestore.getInstance().collection(PATH_REQUEST)
        val document = requestDataBase.document()

        request.id = document.id
        request.time = Calendar.getInstance().timeInMillis

        document
            .set(request)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("RequestTag", "postRequest: $request")
                    continuation.resume(Result.Success(true))
                } else {
                    task.exception?.let {
                        Log.i("RequestTag","[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
                }
            }
    }

    override suspend fun updateRequestHost(requestId: String, shopId: String , hostId: String): Result<Boolean> =
        suspendCoroutine { continuation ->
            val requestDataBase = FirebaseFirestore.getInstance().collection(PATH_REQUEST).document(requestId)
            requestDataBase
                .update("host", hostId, "shopId", shopId)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i("RequestTag", "addHost: $hostId")
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {
                            Log.i(
                                "RequestTag",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
                    }
                }
        }

    override suspend fun updateRequestMember(requestId: String, memberId: String): Result<Boolean> =
    suspendCoroutine { continuation ->
        val requestDataBase = FirebaseFirestore.getInstance().collection(PATH_REQUEST).document(requestId)

        requestDataBase
            .update("member", FieldValue.arrayUnion(memberId))
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("RequestDetailTag", "addMember: $memberId")
                    continuation.resume(Result.Success(true))
                } else {
                    task.exception?.let {

                        Log.i(
                            "RequestDetailTag",
                            "[${this::class.simpleName}] Error getting documents. ${it.message}"
                        )
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
                }
            }
    }

    override suspend fun getMyOrder(userId: String, status:List<Int>): Result<List<MyOrder>>  =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance()
                .collectionGroup(PATH_ORDER)
                .whereEqualTo("userId", userId)
                .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener { orderTask ->
                    if (orderTask.isSuccessful) {
                        val detailList = mutableListOf<MyOrder>()
                        val totalCount = orderTask.result!!.size()
                        var count = 0
                        for (document in orderTask.result!!) {
                            Log.d("MyOrderTag", document.id + " => " + document.data)
                            val order = document.toObject(Order::class.java)
                            var shop: Shop
                            val shopPath = document.reference.parent.parent?.path
                            if (shopPath != null) {
                                FirebaseFirestore
                                    .getInstance()
                                    .collection(PATH_SHOP)
                                    .document(shopPath.replace("shop/", ""))
                                    .get()
                                    .addOnCompleteListener { shopTask ->
                                        if (shopTask.isSuccessful) {
                                            Log.d("MyOrderShopTag", shopTask.result!!.id + " => " + shopTask.result!!.data)
                                            shop = shopTask.result!!.toObject(Shop::class.java)!!
                                            if (status.contains(shop.status)){
                                                detailList.add(MyOrder(shop = shop, order = order))
                                            }
                                            count++
                                        } else {
                                            shopTask.exception?.let {
                                                count++
                                                Log.w(
                                                    "MyOrderTag",
                                                    "[${this::class.simpleName}] Error getting documents. ${it.message}"
                                                )
                                            }
                                            count++
                                            Log.w(
                                                "MyOrderTag",
                                                "Fail: ${MyApplication.instance.getString(R.string.result_fail)}"
                                            )
                                        }
                                        if (count == totalCount){
                                                continuation.resume(Result.Success(detailList))
                                        }
                                    }
                            }
                        }
                    }
                    else {
                        orderTask.exception?.let {
                            Log.w("MyOrderTag", "[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
                    }
                }
        }

    override suspend fun getDetailOrder(shopId: String, orderId: String): Result<Order> =
        suspendCoroutine { continuation ->
            val orderDataBase = FirebaseFirestore.getInstance().collection(PATH_SHOP).document(shopId).collection(
                PATH_ORDER)
            orderDataBase
                .document(orderId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result.let {
                            Log.d("Chloe", it?.id + " => " + it?.data)
                            val order: Order? = it?.toObject(Order::class.java)
                            order?.let {
                                continuation.resume(Result.Success(order))
                            }
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

    override suspend fun getShopByOrder(orderId: String): Result<List<Shop>> {
        TODO("Not yet implemented")
    }

//val shopPath = document.reference.parent.parent
//FirebaseFirestore.getInstance().collection("order").parent
//val path = FirebaseFirestore.getInstance().collection("order").document(document.id).path
//val parent = FirebaseFirestore.getInstance().collection("order").document(document.id).parent
//Log.d("MyOrderTag", "path = $path; parent = $parent")
//Log.d("MyOrderTag", "shopPath = ${shopPath!!.path}")
//FirebaseFirestore.getInstance().collection(PATH_SHOP).document(shopPath.path.replace("shop/","")).get().addOnCompleteListener { shopTask ->
//    if (shopTask.isSuccessful) {
//        Log.d("MyOrderShopTag", shopTask.result!!.id + " => " + shopTask.result!!.data)
////                                    for (shop in shopTask.result!!) {
////                                        Log.d("MyOrderShopTag", shop.id + " => " + shop.data)
//    }

//    override suspend fun getMyOrder(userId: String): Result<List<Order>>  =
//        suspendCoroutine { continuation ->
//            FirebaseFirestore.getInstance()
//                .collectionGroup("order")
//                .whereEqualTo("userId", userId)
//                .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
//                .get()
//                .addOnCompleteListener { orderTask ->
//                    if (orderTask.isSuccessful) {
//                        val orderList = mutableListOf<Order>()
//                        for (document in orderTask.result!!) {
//                            Log.d("MyOrderTag", document.id + " => " + document.data)
//                            val order = document.toObject(Order::class.java)
//                            orderList.add(order)
//                        }
//                        continuation.resume(Result.Success(orderList))
//                    } else {
//                        orderTask.exception?.let {
//
//                            Log.w(
//                                "MyOrderTag",
//                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
//                            )
//                            continuation.resume(Result.Error(it))
//                            return@addOnCompleteListener
//                        }
//                        continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
//                    }
//
//                }
//        }
//


//
//    override suspend fun getShopByOrder(orderId:String) =
//        suspendCoroutine { continuation ->
//            val shopDataBase = FirebaseFirestore.getInstance().collection(PATH_SHOP)
//            val orderDataBase =
//            shopDataBase
//                .document()
//                .get()
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        val shopList = mutableListOf<Shop>()
//                        for (document in task.result!!) {
//                            Log.d("Chloe", document.id + " => " + document.data)
//                            val shop = document.toObject(Shop::class.java)
//                            shopList.add(shop)
//                        }
//                        continuation.resume(Result.Success(shopList))
//                    } else {
//                        task.exception?.let {
//
//                            Log.w(
//                                "Chloe",
//                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
//                            )
//                            continuation.resume(Result.Error(it))
//                            return@addOnCompleteListener
//                        }
//                        continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
//                    }
//
//                }
//


    override suspend fun getMyRequest(userId: String): Result<List<Request>> =
        suspendCoroutine { continuation ->
            val requestDataBase = FirebaseFirestore.getInstance().collection(PATH_REQUEST)
            requestDataBase
                .whereEqualTo("userId", userId)
                .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val requestList = mutableListOf<Request>()
                        for (document in task.result!!) {
                            Log.d("MyRequestTag", document.id + " => " + document.data)
                            val request = document.toObject(Request::class.java)
                            requestList.add(request)
                        }
                        continuation.resume(Result.Success(requestList))
                    } else {
                        task.exception?.let {

                            Log.w(
                                "MyRequestTag",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
                    }

                }
        }

    override suspend fun getMyFinishedRequest(userId: String): Result<List<Request>> =
        suspendCoroutine { continuation ->
            val requestDataBase = FirebaseFirestore.getInstance().collection(PATH_REQUEST)

            requestDataBase
                .whereEqualTo("userId", userId)
//                .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
                .whereNotEqualTo("host",null)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val requestList = mutableListOf<Request>()
                        for (document in task.result!!) {
                            Log.d("MyRequestTag", document.id + " => " + document.data)
                            val request = document.toObject(Request::class.java)
                            requestList.add(request)
                        }
                        continuation.resume(Result.Success(requestList))
                    } else {
                        task.exception?.let {

                            Log.w(
                                "MyRequestTag",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
                    }

                }
        }

    override suspend fun getMyOngoingRequest(userId: String): Result<List<Request>> =
        suspendCoroutine { continuation ->
            val requestDataBase = FirebaseFirestore.getInstance().collection(PATH_REQUEST)

            requestDataBase
                .whereEqualTo("userId", userId)
                .whereEqualTo("host", null)
                .whereEqualTo("host","")
                .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val requestList = mutableListOf<Request>()
                        for (document in task.result!!) {
                            Log.d("MyRequestTag", document.id + " => " + document.data)
                            val request = document.toObject(Request::class.java)
                            requestList.add(request)
                        }
                        continuation.resume(Result.Success(requestList))
                    } else {
                        task.exception?.let {

                            Log.w(
                                "MyRequestTag",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
                    }

                }
        }


    override suspend fun postShop(shop: Shop): Result<PostHostResult> = suspendCoroutine { continuation ->
        val shopDataBase = FirebaseFirestore.getInstance().collection(PATH_SHOP)
        val document = shopDataBase.document()

        shop.id = document.id
        shop.time = Calendar.getInstance().timeInMillis

        val postHostResult = PostHostResult(document.id)

        document
            .set(shop)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("Chloe", "postShop: $shop")
                    continuation.resume(Result.Success(postHostResult))
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

    override suspend fun postOrder(shopId: String, order: Order): Result<PostOrderResult> =
        suspendCoroutine { continuation ->
            val orderDataBase = FirebaseFirestore.getInstance().collection(PATH_SHOP)
                .document(shopId).collection(PATH_ORDER)
            val document = orderDataBase.document()

            order.id = document.id
            order.time = Calendar.getInstance().timeInMillis

            val postOrderResult = PostOrderResult(document.id)

            document
                .set(order)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i("Chloe", "postOrder: $order")
                        continuation.resume(Result.Success(postOrderResult))
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

    override suspend fun increaseOrderSize(shopId: String): Result<Boolean> =suspendCoroutine { continuation ->
        val shopDataBase = FirebaseFirestore.getInstance().collection(PATH_SHOP).document(shopId)
        shopDataBase
            .update("orderSize", FieldValue.increment(1))
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
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

    override suspend fun getShopDetailLiked(shopIdList: List<String>): Result<List<Shop>>  =
        suspendCoroutine { continuation ->
            val shopDataBase = FirebaseFirestore.getInstance().collection(PATH_SHOP)
            shopDataBase
                .whereIn("id",shopIdList)
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

    override suspend fun addShopLiked(userId: String, shopId: String): Result<Boolean> =
        suspendCoroutine { continuation ->
            val userDataBase = FirebaseFirestore.getInstance().collection(PATH_USER)
                .document(userId)

            userDataBase
                .update("like", FieldValue.arrayUnion(shopId))
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i("Notify", "addSubscribe: $shopId")
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            Log.i(
                                "Notify",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
                    }
                }
        }

    override suspend fun removeShopLiked(userId: String, shopId: String): Result<Boolean> =
        suspendCoroutine { continuation ->
            val userDataBase = FirebaseFirestore.getInstance().collection(PATH_USER)
                .document(userId)

            userDataBase
                .update("like", FieldValue.arrayRemove(shopId))
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i("Notify", "addSubscribe: $shopId")
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            Log.i(
                                "Notify",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
                    }
                }
        }

    override suspend fun postShopNotifyToMember(notify: Notify): Result<Boolean> =
        suspendCoroutine { continuation ->
            val shopId = notify.shopId
            val userDataBase = FirebaseFirestore.getInstance().collection(PATH_USER)
            val orderDataBase =
                FirebaseFirestore.getInstance().collection(PATH_SHOP).document(shopId)
                    .collection(PATH_ORDER)
            notify.time = Calendar.getInstance().timeInMillis

            orderDataBase
                .get()
                .addOnCompleteListener { orderTask ->
                    if (orderTask.isSuccessful) {
                        val totalCount = orderTask.result!!.size()
                        var count = 0

                        for (document in orderTask.result!!) {
                            Log.d("Notify", document.id + " => " + document.data)
                            val order = document.toObject(Order::class.java)
                            val memberId = order.userId
                            val notifyDocument =
                                userDataBase.document(memberId).collection(PATH_NOTIFY).document()
                            notify.id = document.id
                            notifyDocument
                                .set(notify)
                                .addOnCompleteListener { notifyTask ->
                                    if (notifyTask.isSuccessful) {
                                        Log.i("Notify", "Notify: ${notifyTask.result}")
                                        count += 1
//                                        continuation.resume(Result.Success(true))
                                    } else {
                                        notifyTask.exception?.let {
                                            Log.i("Notify", "[${this::class.simpleName}] Error getting documents. ${it.message}")
                                            count += 1
                                        }
                                    }
                                    if (count == totalCount){
                                        continuation.resume(Result.Success(true))
                                        Log.d("Notify", "count: count == totalCoun")
                                    }
                                }
                        }
                    } else {
                        orderTask.exception?.let {
                            Log.i(
                                "Notify",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(
                            Result.Fail(
                                MyApplication.instance.getString(
                                    R.string.result_fail
                                )
                            )
                        )
                    }
                }
        }

    override suspend fun postRequestNotifyToMember(notify: Notify): Result<Boolean> =
        suspendCoroutine { continuation ->
            val requestId = notify.requestId
            val userDataBase = FirebaseFirestore.getInstance().collection(PATH_USER)
            val requestDataBase = FirebaseFirestore.getInstance().collection(PATH_REQUEST).document(requestId!!)

            notify.time = Calendar.getInstance().timeInMillis

            requestDataBase
                .get()
                .addOnCompleteListener { requestTask ->
                    if (requestTask.isSuccessful) {
                        val request = requestTask.result!!.toObject(Request::class.java)
                        val memberList = request!!.member
                        if (memberList != null) {
                            val totalCount = memberList.size
                            var count = 0
                            for (member in memberList){
                                val notifyDocument = userDataBase.document(member).collection(PATH_NOTIFY).document()
                                notify.id = notifyDocument.id
                                notifyDocument
                                    .set(notify)
                                    .addOnCompleteListener { notifyTask ->
                                        if (notifyTask.isSuccessful) {
                                            Log.i("Notify", "Notify: ${notifyTask.result}")
                                            count += 1
//                                        continuation.resume(Result.Success(true))
                                        } else {
                                            notifyTask.exception?.let {
                                                Log.i("Notify", "[${this::class.simpleName}] Error getting documents. ${it.message}")
                                                count += 1
                                            }
                                        }
                                        if (count == totalCount){
                                            continuation.resume(Result.Success(true))
                                            Log.d("Notify", "count: count == totalCoun")
                                        }
                                    }
                            }
                        }
                    } else {
                        requestTask.exception?.let {
                            Log.i(
                                "Notify",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(
                            Result.Fail(
                                MyApplication.instance.getString(
                                    R.string.result_fail
                                )
                            )
                        )
                    }
                }
        }



override suspend fun postOrderNotifyToMember(orderList: List<Order>, notify: Notify): Result<Boolean> =
    suspendCoroutine { continuation ->
        val shopId = notify.shopId
        val userDataBase = FirebaseFirestore.getInstance().collection(PATH_USER)

        notify.time = Calendar.getInstance().timeInMillis

        val totalCount = orderList.size
        var count = 0
        for (order in orderList){
            notify.orderId = order.id
            notify.message = NotifyType.ORDER_FAIL.toDisplayNotifyMessage(order)
            val memberId = order.userId
            userDataBase.document(memberId).collection(PATH_NOTIFY).document()
                .set(notify)
                .addOnCompleteListener { notifyTask ->
                    if (notifyTask.isSuccessful) {
                        Log.i("Notify", "Notify: ${notifyTask.result}")
                        Result.Success(true)
                        count += 1
                    } else {
                        notifyTask.exception?.let {
                        Log.i(
                            "Notify",
                            "[${this::class.simpleName}] Error getting documents. ${it.message}"
                        )
                        continuation.resume(Result.Error(it))
                        count += 1
                        return@addOnCompleteListener
                    }
                        count += 1
                        continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
                    }
                    if(count == totalCount) {
                        continuation.resume(Result.Success(true))
                    }
                }

        }

    }
//    override suspend fun postOrderNotifyToMember(orderList: List<Order>, notify: Notify): Result<Boolean> =
//        suspendCoroutine {
//            val shopId = notify.shopId
//            val userDataBase = FirebaseFirestore.getInstance().collection(PATH_USER)
//
//            notify.time = Calendar.getInstance().timeInMillis
//
//            for (order in orderList){
//                notify.orderId = order.id
//                notify.message = NotifyType.ORDER_FAIL.toDisplayNotifyMessage(order)
//                val memberId = order.userId
//                userDataBase.document(memberId).collection(PATH_NOTIFY).document()
//                    .set(notify)
//                    .addOnCompleteListener { notifyTask ->
//                        if (notifyTask.isSuccessful) {
//                            Log.i("Notify", "Notify: ${notifyTask.result}")
//                            Result.Success(true)
//                        } else {notifyTask.exception?.let {
//                            Log.i(
//                                "Notify",
//                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
//                            )
//                            Result.Error(it)
//                            return@addOnCompleteListener
//                        }
//                        }
//                        Result.Fail(MyApplication.instance.getString(R.string.result_fail))
//                    }
//            }
//        }


    override suspend fun postNotifyToHost(hostId: String, notify: Notify): Result<Boolean> =
        suspendCoroutine { continuation ->
            val notifyDataBase =
                FirebaseFirestore.getInstance().collection(PATH_USER).document(hostId)
                    .collection(PATH_NOTIFY)

            val document = notifyDataBase.document()
            notify.id = document.id
            notify.time = Calendar.getInstance().timeInMillis

            document
                .set(notify)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i("Notify", "postNotify: $notify")
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {
                            Log.i(
                                "Notify",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
                    }
                }
        }


    override fun getLiveNotify(userId: String): MutableLiveData<List<Notify>> {

        val liveData = MutableLiveData<List<Notify>>()

        val notifyDataBase = FirebaseFirestore.getInstance().collection(PATH_USER).document(userId)
            .collection(PATH_NOTIFY)
        notifyDataBase
            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->

                Log.i("Notify", "addSnapshotListener detect")

                exception?.let {
                    Log.w(
                        "Notify",
                        "[${this::class.simpleName}] Error getting documents. ${it.message}"
                    )
                }

                val notifyList = mutableListOf<Notify>()
                for (document in snapshot!!) {
                    Log.d("Notify", document.id + " => " + document.data)

                    val notify = document.toObject(Notify::class.java)
                    notifyList.add(notify)
                }
                liveData.value = notifyList
            }
        Log.d("Notify", "livedata = ${liveData.value}")
        return liveData
    }
}




//    override suspend fun signInWithGoogle(idToken: String): Result<User> =
//        suspendCoroutine { continuation ->
//            val credential = GoogleAuthProvider.getCredential(idToken, null)
//            val auth = FirebaseAuth.getInstance()
//            val userDataBase = FirebaseFirestore.getInstance().collection(PATH_USER)
//            var userProfile = User()
//
//
//            auth.signInWithCredential(credential)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        // Sign in success, update UI with the signed-in user's information
//                        Log.d("Login", "signInWithCredential:success")
//                        val user = auth.currentUser
//                        user?.let {
//                            userDataBase
//                                .get()
//                                .addOnCompleteListener { documents ->
//                                    if (documents.isSuccessful) {
//                                        for (document in documents.result!!) {
//                                            document.id == user.uid
//                                            if (document.id == user.uid) {
//                                                userProfile = document.toObject(User::class.java)
//                                            } else {
//                                                userProfile = User(
//                                                    provider = "google",
//                                                    id = user.uid,
//                                                    name = user.displayName ?: "UserName",
//                                                    email = user.email,
//                                                    photo = user.photoUrl.toString()
//                                                )
//                                                userDataBase.document(user.uid).set(userProfile)
//                                            }
//                                        }
//                                        userProfile = userProfile
//                                        Log.d("Login", "userProfile = $userProfile")
//                                        continuation.resume(Result.Success(userProfile))
//                                    }
//                                }
//                        }
//                    } else {
//                        task.exception?.let {
//                            Log.w(
//                                "Login",
//                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
//                            )
//                            continuation.resume(Result.Error(it))
//                            return@addOnCompleteListener
//                        }
//                        continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
//                    }
//                }
//        }


//    override suspend fun getOpeningShop(): Result<List<Shop>> = suspendCoroutine { continuation ->
//        val shopDataBase = FirebaseFirestore.getInstance().collection(PATH_SHOP)
//        shopDataBase
////            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
//            .get()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    var shopList = mutableListOf<Shop>()
//                    val totalTaskCount = task.result!!.size()
//                    Log.d("HomeTag"," totalTaskCount = ${totalTaskCount}")
//                    var count = 0
//                    for (document in task.result!!) {
//                        Log.d("HomeTag", document.id + " => " + document.data)
//                        val shop = document.toObject(Shop::class.java)
//
//                        while (count < totalTaskCount){
//                            shopDataBase.document(shop.id).collection(PATH_ORDER)
//                                .get()
//                                .addOnCompleteListener { orderTask ->
//                                    if (orderTask.isSuccessful){
//                                        val memberNumber = orderTask.result!!.size()
//                                        Log.d("HomeTag"," memberNumber = ${orderTask.result!!.size()}")
//                                        shop.member = memberNumber
//                                        Log.d("HomeTag"," shop = ${shop.member}")
//                                        shopList.add(shop)
//
//                                    }
//                                    Log.d("HomeTag"," count = ${count}")
//                                }
//                        }
//                        count++
//                        if (count == totalTaskCount){
//                            shopList = shopList
//                            Log.d("HomeTag"," shopList = ${shopList}")
//                            continuation.resume(Result.Success(shopList))
//                    }
//                    }
//
//
//                } else {
//                    task.exception?.let {
//
//                        Log.w(
//                            "Chloe",
//                            "[${this::class.simpleName}] Error getting documents. ${it.message}"
//                        )
//                        continuation.resume(Result.Error(it))
//                        return@addOnCompleteListener
//                    }
//                    continuation.resume(Result.Fail(MyApplication.instance.getString(R.string.result_fail)))
//                }
//
//            }
//    }


//    override suspend fun postShopNotifyToMember(notify: Notify): Result<Boolean> =
//        suspendCoroutine { continuation ->
//            val shopId = notify.shopId
//            val userDataBase = FirebaseFirestore.getInstance().collection(PATH_USER)
//            val orderDataBase =
//                FirebaseFirestore.getInstance().collection(PATH_SHOP).document(shopId)
//                    .collection(PATH_ORDER)
//            notify.time = Calendar.getInstance().timeInMillis
//
//            orderDataBase
//                .get()
//                .addOnCompleteListener { orderTask ->
//                    if (orderTask.isSuccessful) {
//                        for (document in orderTask.result!!) {
//                            Log.d("Notify", document.id + " => " + document.data)
//                            val order = document.toObject(Order::class.java)
//                            val memberId = order.userId
//                            val notifyDocument =
//                                userDataBase.document(memberId).collection(PATH_NOTIFY).document()
//                            notify.id = document.id
//                            notifyDocument
//                                .set(notify)
//                                .addOnCompleteListener { notifyTask ->
//                                    if (notifyTask.isSuccessful) {
//                                        Log.i("Notify", "Notify: ${notifyTask.result}")
////                                        continuation.resume(Result.Success(true))
////                                    } else {
////                                        notifyTask.exception?.let {
////                                            Log.i(
////                                                "Notify",
////                                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
////                                            )
////                                            continuation.resume(Result.Error(it))
////                                            return@addOnCompleteListener
////                                        }
////                                        continuation.resume(
////                                            Result.Fail(
////                                                MyApplication.instance.getString(
////                                                    R.string.result_fail
////                                                )
////                                            )
////                                        )
//                                    }
//                                }
//                        }
//
//                        continuation.resume(Result.Success(true))
//                    } else {
//                        orderTask.exception?.let {
//                            Log.i(
//                                "Notify",
//                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
//                            )
//                            continuation.resume(Result.Error(it))
//                            return@addOnCompleteListener
//                        }
//                        continuation.resume(
//                            Result.Fail(
//                                MyApplication.instance.getString(
//                                    R.string.result_fail
//                                )
//                            )
//                        )
//                    }
//                }
//        }
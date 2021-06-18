package com.chloe.shopshare.login

import android.content.Intent
import com.chloe.shopshare.data.Result
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.User
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.network.LoadApiStatus
import com.chloe.shopshare.util.UserManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository):ViewModel() {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _user = MutableLiveData<User>()
    val user : LiveData<User>
        get() = _user

    // Handle leave login
    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

    private val _getTokenDone = MutableLiveData<Boolean>()

    val getTokenDone: LiveData<Boolean>
        get() = _getTokenDone


    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)




    //登入google 取得id及基本資料->確認是否為首次登入->回傳profile
    private fun signInWithGoogle(idToken: String) {
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.signInWithGoogle(idToken)
            // It will return Result object after Deferred flow
            _user.value =
            when (result) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    UserManager.userId = result.data.id
                    result.data
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                else -> {
                    _error.value = MyApplication.instance.getString(R.string.result_fail)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
        }
    }

    fun signInGoogle(){
        signInWithGoogle(account.idToken!!)
    }

    fun onGetTokenDone(){
        _getTokenDone.value = null
    }


    lateinit var account : GoogleSignInAccount

    fun signIn(data: Intent){
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            // Google Sign In was successful, authenticate with Firebase
            account = task.getResult(ApiException::class.java)!!
            Log.d("Login", "firebaseAuthWithGoogle:" + account.id)
            _getTokenDone.value = true

        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.w("Login", "Google sign in failed", e)
        }
    }


}






//    fun firebaseAuthWithGoogle(idToken: String) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        auth.signInWithCredential(credential).addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d("Chloe", "signInWithCredential:success")
//                    var userList = User()
//                    val user = auth.currentUser
//                    user?.let {
//                        userList=
//                            User(
//                                provider = "google",
//                                id = user.uid,
//                                name = user.displayName?:"user",
//                                email = user.email,
//                                photo = user.photoUrl.toString()
//                            )
//
//                        googleProfile = userList
//
//                        Log.d("Chloe", "name=${user.displayName},email=${user.email},photoUrl =${user.photoUrl},emailVerified=${user.isEmailVerified},uid=${user.uid}")
//                    }
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w("Chloe", "signInWithCredential:failure", task.exception)
//                }
//            }
//    }
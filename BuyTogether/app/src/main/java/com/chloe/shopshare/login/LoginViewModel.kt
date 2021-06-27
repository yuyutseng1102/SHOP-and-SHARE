package com.chloe.shopshare.login

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.Result
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

class LoginViewModel(private val repository: Repository) : ViewModel() {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _getTokenDone = MutableLiveData<Boolean?>()
    val getTokenDone: LiveData<Boolean?>
        get() = _getTokenDone

    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    private fun signInWithGoogle(idToken: String) {
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.signInWithGoogle(idToken)
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

    fun signInGoogle() {
        signInWithGoogle(account.idToken!!)
    }

    fun onGetTokenDone() {
        _getTokenDone.value = null
    }


    private lateinit var account: GoogleSignInAccount

    fun signIn(data: Intent) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            task.getResult(ApiException::class.java)?.let { account = it }
            _getTokenDone.value = true

        } catch (e: ApiException) {
            Log.w("Login", "Google sign in failed", e)
        }
    }
}


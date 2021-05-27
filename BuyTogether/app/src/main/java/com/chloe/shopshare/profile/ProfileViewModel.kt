package com.chloe.shopshare.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.Result
import com.chloe.shopshare.data.User
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.login.LoginViewModel
import com.chloe.shopshare.network.LoadApiStatus
import com.chloe.shopshare.util.UserManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: Repository): ViewModel() {

    private val _user = MutableLiveData<User>()
    val user : LiveData<User>
        get() = _user

    // Handle leave login
    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String?>()

    val error: LiveData<String?>
        get() = _error

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    var auth= FirebaseAuth.getInstance()

    init {
        if (_user.value == null) {
            UserManager.userId?.let {
                getUserProfile(it)
            }
        }
    }



    private fun getUserProfile(userId: String) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.getUserProfile(userId)
            // It will return Result object after Deferred flow
            _user.value =
                when (result) {
                    is Result.Success -> {
                        _error.value = null
                        _status.value = LoadApiStatus.DONE
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



    fun logout(){
        auth.signOut()
        Log.d("Login","currentUser = ${auth.currentUser}")
    }
}
package com.chloe.shopshare

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.data.Notify
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.util.CurrentFragmentType
import com.chloe.shopshare.util.UserManager

class MainViewModel(private val repository: Repository) : ViewModel() {

    private var _notify = MutableLiveData<List<Notify>>()
    val notify: LiveData<List<Notify>>
        get() = _notify

    val currentFragmentType = MutableLiveData<CurrentFragmentType>()

    private val _navigateToHomeByBottomNav = MutableLiveData<Boolean?>()
    val navigateToHomeByBottomNav: LiveData<Boolean?>
        get() = _navigateToHomeByBottomNav

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error


    init {
        UserManager.userId?.let { getLiveNewNotify(it) }
    }

    fun onHomeNavigated() {
        _navigateToHomeByBottomNav.value = null
    }

    private fun getLiveNewNotify(userId: String) {
        _notify = repository.getLiveNewNotify(userId)
    }
}
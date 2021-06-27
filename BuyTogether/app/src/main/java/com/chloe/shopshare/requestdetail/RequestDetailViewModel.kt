package com.chloe.shopshare.requestdetail

import android.graphics.Rect
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.Request
import com.chloe.shopshare.data.Result
import com.chloe.shopshare.data.User
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.network.LoadApiStatus
import com.chloe.shopshare.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RequestDetailViewModel(private val repository: Repository, private val args: String) :
    ViewModel() {

    private val _requestId = MutableLiveData<String>().apply {
        value = args
    }

    private var _request = MutableLiveData<Request>()
    val request: LiveData<Request>
        get() = _request

    private var _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _navigateToHostDetail = MutableLiveData<String?>()
    val navigateToHostDetail: LiveData<String?>
        get() = _navigateToHostDetail

    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    private val _refreshStatus = MutableLiveData<Boolean>()
    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // it for gallery circles design
    private val _snapPosition = MutableLiveData<Int>()
    val snapPosition: LiveData<Int>
        get() = _snapPosition

    lateinit var userId: String

    init {
        _requestId.value?.let {
            getLiveDetail(it)
        }
        UserManager.userId?.let {
            userId = it
        }
    }

    fun navigateToHostDetail(id: String) {
        _navigateToHostDetail.value = id
    }

    fun onHostDetailNavigated() {
        _navigateToHostDetail.value = null
    }

    private fun getLiveDetail(id: String) {
        _status.value = LoadApiStatus.LOADING
        _request = repository.getLiveDetailRequest(id)
        _status.value = LoadApiStatus.DONE
        _refreshStatus.value = false
    }

    fun getUserProfile(userId: String) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val result = repository.getUserProfile(userId)

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


    fun updateMember(requestId: String, memberId: String) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            when (val result = repository.updateRequestMember(requestId, memberId)) {
                    is Result.Success -> {
                        _error.value = null
                        _status.value = LoadApiStatus.DONE
                    }
                    is Result.Fail -> {
                        _error.value = result.error
                        _status.value = LoadApiStatus.ERROR
                    }
                    is Result.Error -> {
                        _error.value = result.exception.toString()
                        _status.value = LoadApiStatus.ERROR
                    }
                    else -> {
                        _error.value = MyApplication.instance.getString(R.string.result_fail)
                        _status.value = LoadApiStatus.ERROR
                    }
                }
            _refreshStatus.value = false
        }
    }

    val decoration = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.left = 0
            } else {
                outRect.left =
                    MyApplication.instance.resources.getDimensionPixelSize(R.dimen.space_detail_circle)
            }
        }
    }


    /**
     * When the gallery scroll, at the same time circles design will switch.
     */
    fun onGalleryScrollChange(
        layoutManager: RecyclerView.LayoutManager?,
        linearSnapHelper: LinearSnapHelper
    ) {
        val snapView = linearSnapHelper.findSnapView(layoutManager)
        snapView?.let {
            layoutManager?.getPosition(snapView)?.let {
                if (it != _snapPosition.value) {
                    _snapPosition.value = it
                }
            }
        }
    }
}
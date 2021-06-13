package com.chloe.shopshare.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.*
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.network.LoadApiStatus
import com.chloe.shopshare.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChatViewModel(val repository: Repository): ViewModel() {

    private val  _chatRoom = MutableLiveData<List<ChatRoom>>()
    val chatRoom: LiveData<List<ChatRoom>>
        get() =  _chatRoom


    private val _chatDetail = MutableLiveData<List<ChatDetail>>()
    val chatDetail: LiveData<List<ChatDetail>>
        get() =  _chatDetail


    private val _getChatRoomDone = MutableLiveData<Boolean>()
    val getChatRoomDone: LiveData<Boolean>
        get() = _getChatRoomDone

    private val _getProfileDone = MutableLiveData<Boolean>()
    val getProfileDone: LiveData<Boolean>
        get() = _getProfileDone

    private val _getMessageDone = MutableLiveData<Boolean>()
    val getMessageDone: LiveData<Boolean>
        get() = _getMessageDone

    private val _isChatListEmpty = MutableLiveData<Boolean>()
    val isChatListEmpty: LiveData<Boolean>
        get() = _isChatListEmpty

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

    init {
        UserManager.userId?.let {
            getChatDetail(it)
//            getLiveChat(it)
        }
    }

    private val _navigateToChatRoom = MutableLiveData<ChatRoomKey>()
    val navigateToChatRoom: LiveData<ChatRoomKey>
        get() = _navigateToChatRoom

    fun navigateToChatRoom(chatDetail: ChatDetail) {
        Log.d("Chat","chatDetail on click is ${chatDetail}")
        _navigateToChatRoom.value = ChatRoomKey(myId = UserManager.userId!!,friendId = chatDetail.friendProfile!!.id,chatRoomId = chatDetail.chatRoom!!.id)
    }

    fun onChatRoomNavigated() {
        _navigateToChatRoom.value = null
    }

//    private fun getLiveChat(myId: String){
//        _status.value = LoadApiStatus.LOADING
//        chatRoom = repository.getMyAllChatRoom(myId)
//        Log.d("Chloe", "chatRoom = ${chatRoom.value}")
//        _status.value = LoadApiStatus.DONE
//    }

    private fun getChatDetail(myId: String) {
        var message = MutableLiveData<List<Message>>()
        val chatDetailList = mutableListOf<ChatDetail>()
        var friendProfile: User?

        coroutineScope.launch {

            //拿聊天列表的資料

            val chatResult = repository.getMyChatList(myId)
            _chatRoom.value =
                when (chatResult) {
                    is Result.Success -> {
                        _error.value = null
                        _status.value = LoadApiStatus.DONE
                        chatResult.data

                    }
                    is Result.Fail -> {
                        _error.value = chatResult.error
                        _status.value = LoadApiStatus.ERROR
                        null
                    }
                    is Result.Error -> {
                        _error.value = chatResult.exception.toString()
                        _status.value = LoadApiStatus.ERROR
                        null
                    }
                    else -> {
                        _error.value = MyApplication.instance.getString(R.string.result_fail)
                        _status.value = LoadApiStatus.ERROR
                        null
                    }
                }

            //用聊天列表來抓個人資訊
            if (_chatRoom.value != null) {
                val totalCount = _chatRoom.value?.size
                var count = 0
                for (chat in _chatRoom.value!!) {
                    val friendList = chat.talker.filterNot { it == UserManager.userId }

                    _status.value = LoadApiStatus.LOADING
                    val result = repository.getUserProfile(friendList[0])

                    when (result) {
                        is Result.Success -> {
                            _error.value = null
                            _status.value = LoadApiStatus.DONE
                            //抓聊天室的message
//                            message = getLiveMessage(chat.id)
                            friendProfile = result.data
                            chatDetailList.add(ChatDetail(chat, friendProfile, message.value))
//                            chatDetailList.add(ChatDetail(chat, friendProfile, message.value))
                            count++
                        }
                        is Result.Fail -> {
                            _error.value = result.error
                            _status.value = LoadApiStatus.ERROR
                            friendProfile = null
                            count++
                        }
                        is Result.Error -> {
                            _error.value = result.exception.toString()
                            _status.value = LoadApiStatus.ERROR
                            friendProfile = null
                            count++
                        }
                        else -> {
                            _error.value = MyApplication.instance.getString(R.string.result_fail)
                            _status.value = LoadApiStatus.ERROR
                            friendProfile = null
                            count++
                        }
                    }
                    if (count == totalCount) {
                        _chatDetail.value = chatDetailList
                        _status.value = LoadApiStatus.DONE
                    }
                }

            } else {
                _isChatListEmpty.value = true
                _status.value = LoadApiStatus.DONE
            }

        }


    }


//    private fun getMyChatRoom(myId: String) {
//
//        coroutineScope.launch {
//
//            _status.value = LoadApiStatus.LOADING
//            val result = repository.getMyAllChatRoom(myId)
//
//            _chatRoom.value =
//                when (result) {
//                    is Result.Success -> {
//                        _error.value = null
//                        _status.value = LoadApiStatus.DONE
//                        result.data
//
//                    }
//                    is Result.Fail -> {
//                        _error.value = result.error
//                        _status.value = LoadApiStatus.ERROR
//                        null
//                    }
//                    is Result.Error -> {
//                        _error.value = result.exception.toString()
//                        _status.value = LoadApiStatus.ERROR
//                        null
//                    }
//                    else -> {
//                        _error.value = MyApplication.instance.getString(R.string.result_fail)
//                        _status.value = LoadApiStatus.ERROR
//                        null
//                    }
//                }
//            _getChatRoomDone.value = true
//            Log.d("Chat","chatRoom.value = ${_chatRoom.value}")
//        }
//    }


//    fun getFriendProfile(friendId: String) : User? {
//        var friendProfile : User? = null
//        coroutineScope.launch {
//
//            _status.value = LoadApiStatus.LOADING
//            val result = repository.getUserProfile(friendId)
//              friendProfile =
//                  when (result) {
//                    is Result.Success -> {
//                        _error.value = null
//                        _status.value = LoadApiStatus.DONE
//                        result.data
//                    }
//                    is Result.Fail -> {
//                        _error.value = result.error
//                        _status.value = LoadApiStatus.ERROR
//                        null
//                    }
//                    is Result.Error -> {
//                        _error.value = result.exception.toString()
//                        _status.value = LoadApiStatus.ERROR
//                        null
//                    }
//                    else -> {
//                        _error.value = MyApplication.instance.getString(R.string.result_fail)
//                        _status.value = LoadApiStatus.ERROR
//                        null
//                    }
//                }
//            Log.d("Chat","friendProfile.value = ${friendProfile}")
//            _getProfileDone.value = true
//        }
//        return friendProfile
//    }

    fun getLiveMessage(chatRoomId: String):MutableLiveData<List<Message>> {
        _status.value = LoadApiStatus.LOADING
        val messageList = repository.getRoomMessage(chatRoomId)
        Log.d("Chat","getLiveMessage = ${messageList.value}")
        _status.value = LoadApiStatus.DONE
        return messageList
    }

}
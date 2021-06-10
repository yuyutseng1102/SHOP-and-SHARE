package com.chloe.shopshare.chatroom

import com.chloe.shopshare.util.UserManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.*
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.ext.toDisplayNotifyContent
import com.chloe.shopshare.network.LoadApiStatus
import com.chloe.shopshare.notify.NotifyType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChatRoomViewModel(
    private val repository: Repository, private val myIdArgs: String, private val friendIdArgs: String, private val chatRoomIdArgs: String
):ViewModel() {

    private val _myId = MutableLiveData<String>().apply {
        value = myIdArgs
    }
    val myId: LiveData<String>
        get() =  _myId

    private val _friendId = MutableLiveData<String>().apply {
        value = friendIdArgs
    }
    val friendId: LiveData<String>
        get() =  _friendId

    private val _chatRoomId = MutableLiveData<String>().apply {
        value = chatRoomIdArgs
    }
    val chatRoomId: LiveData<String>
        get() =  _chatRoomId

    private val _chatRoom = MutableLiveData<ChatRoom>()
    val chatRoom: LiveData<ChatRoom>
        get() =  _chatRoom

    private val _chatDetail = MutableLiveData<ChatDetail>()
    val chatDetail: LiveData<ChatDetail>
        get() =  _chatDetail

    private val _friendProfile = MutableLiveData<User>()
    val friendProfile: LiveData<User>
        get() =  _friendProfile
    var messageList = MutableLiveData<List<Message>>()

//    private var _messageList = MutableLiveData<List<Message>>()
//    val messageList: LiveData<List<Message>>
//        get() =  _messageList



//    private var _messageItem = MutableLiveData<List<MessageItem>>()
//    val messageItem: LiveData<List<MessageItem>>
//        get() =  _messageItem

    val editMessage = MutableLiveData<String>()

    private var _message = MutableLiveData<Message>()
    val message: LiveData<Message>
        get() =  _message

    private val _getChatRoomDone = MutableLiveData<Boolean>()
    val getChatRoomDone: LiveData<Boolean>
        get() = _getChatRoomDone

    private val _getProfileDone = MutableLiveData<Boolean>()
    val getProfileDone: LiveData<Boolean>
        get() = _getProfileDone

    private val _sendMessageDone = MutableLiveData<Boolean>()
    val sendMessageDone: LiveData<Boolean>
        get() = _sendMessageDone


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
        _friendId.value?.let {
            getChatRoom(_myId.value!!, it)
        }
    }


    private fun getChatRoom(myId: String, friendId: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING
            val result = repository.getChatRoom(myId, friendId)

            _chatRoom.value =
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
            _getChatRoomDone.value = true
            Log.d("Chat","chatRoom.value = ${_chatRoom.value}")
        }
    }


    fun getFriendProfile(friendId: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING
            val result = repository.getUserProfile(friendId)

            _friendProfile.value =
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
            Log.d("Chat","friendProfile.value = ${_friendProfile.value}")
            _getProfileDone.value = true
        }
    }

    fun getLiveMessage(chatRoomId: String) {
        _status.value = LoadApiStatus.LOADING
        messageList = repository.getRoomMessage(chatRoomId)
        Log.d("Chat","getLiveMessage = ${messageList.value}")
        _status.value = LoadApiStatus.DONE
    }

    fun editMessage(editMessage: String) {
        Log.d("Chat","editMessage = ${editMessage}")
        UserManager.userId?.let {
            _message.value = Message(
                talkerId = it ,
                message = editMessage
            )
        }
        if (_chatRoom.value!=null) {
            _message.value?.let {
                sendMessage(_chatRoom.value!!.id, it)
            }
        }
    }

    private fun sendMessage(chatRoomId: String, message: Message) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING
            val result = repository.sendMessage(chatRoomId, message)

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
            _sendMessageDone.value = true
        }
    }

//    fun toMessageItem(messageList:List<Message>?): List<MessageItem> {
//        val items = mutableListOf<MessageItem>()
//        messageList?.let {
//            Log.d("Chat","messageList = ${messageList}")
//            for (message in it) {
//                when (message.talkerId) {
//                    _friendId.value -> items.add(MessageItem.MessageByFriend(message))
//                    else -> items.add(MessageItem.MessageByMe(message))
//                }
//            }
//        }
//        return items
//    }



//    fun editChatDetail(chatRoom: ChatRoom, friendProfile: User, message: List<Message>) {
//        _chatDetail.value = ChatDetail(chatRoom, friendProfile, message)
//    }
    fun onGetChatRoomDone() {
        _getChatRoomDone.value = null
    }
    fun onSendMessageDone() {
        _sendMessageDone.value = null
    }



}
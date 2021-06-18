package com.chloe.shopshare.chat

import android.util.Log
import androidx.core.text.htmlEncode
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

class ChatViewModel(val repository: Repository) : ViewModel() {

    private var _chatRooms = MutableLiveData<List<ChatRoom>>()
    val chatRooms: LiveData<List<ChatRoom>>
        get() = _chatRooms



    private var _friends = MutableLiveData<List<String>>()
    val friends: LiveData<List<String>>
        get() = _friends

//    private val _chatRoom = MutableLiveData<List<ChatRoom>>()
//    val chatRoom: LiveData<List<ChatRoom>>
//        get() = _chatRoom



    private val _chatList = MutableLiveData<List<Chat>>()
    val chatList: LiveData<List<Chat>>
        get() = _chatList


    private val _getProfileDone = MutableLiveData<Boolean?>()
    val getProfileDone: LiveData<Boolean?>
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
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error


    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    lateinit var myId: String
    init {
        UserManager.userId?.let {
            myId = it
            getLiveChatList(it)
        }
    }

    private val _navigateToChatRoom = MutableLiveData<ChatRoom?>()
    val navigateToChatRoom: LiveData<ChatRoom?>
        get() = _navigateToChatRoom

    fun navigateToChatRoom(chatRoom: ChatRoom) {
        Log.d("ChatTag", "nav")
        _navigateToChatRoom.value = chatRoom
    }

    fun onChatRoomNavigated() {
        _navigateToChatRoom.value = null
    }

    private fun getLiveChatList(myId: String){
        _status.value = LoadApiStatus.LOADING
        _chatRooms = repository.getMyLiveChatList(myId)
        Log.d("ChatTag", "getLiveChatList = ${chatRooms.value}")
        _status.value = LoadApiStatus.DONE
    }

//    private fun getChatList(myId: String) {
//
//        val chatList = mutableListOf<Chat>()
//        var friend: User?
//
//        coroutineScope.launch {
//
//            val chatResult = repository.getMyChatList(myId)
//
//            _chatRoom.value =
//                when (chatResult) {
//                    is Result.Success -> {
//                        _error.value = null
//                        _status.value = LoadApiStatus.DONE
//                        chatResult.data
//                    }
//                    is Result.Fail -> {
//                        _error.value = chatResult.error
//                        _status.value = LoadApiStatus.ERROR
//                        null
//                    }
//                    is Result.Error -> {
//                        _error.value = chatResult.exception.toString()
//                        _status.value = LoadApiStatus.ERROR
//                        null
//                    }
//                    else -> {
//                        _error.value = MyApplication.instance.getString(R.string.result_fail)
//                        _status.value = LoadApiStatus.ERROR
//                        null
//                    }
//                }
//
//            //用聊天列表來抓個人資訊
//            if (_chatRoom.value != null) {
//                val totalCount = _chatRoom.value?.size
//                var count = 0
//                for (chat in _chatRoom.value!!) {
//                    val friendList = chat.talker.filterNot { it == UserManager.userId }
//
//                    _status.value = LoadApiStatus.LOADING
//                    val result = repository.getUserProfile(friendList[0])
//
//                    when (result) {
//                        is Result.Success -> {
//                            _error.value = null
//                            _status.value = LoadApiStatus.DONE
//                            //抓聊天室的message
////                            message = getLiveMessage(chat.id)
//                            friend = result.data
//                            chatList.add(Chat(chat, friend))
////                            chatDetailList.add(ChatDetail(chat, friendProfile, message.value))
//                            count++
//                        }
//                        is Result.Fail -> {
//                            _error.value = result.error
//                            _status.value = LoadApiStatus.ERROR
//                            friend = null
//                            count++
//                        }
//                        is Result.Error -> {
//                            _error.value = result.exception.toString()
//                            _status.value = LoadApiStatus.ERROR
//                            friend = null
//                            count++
//                        }
//                        else -> {
//                            _error.value = MyApplication.instance.getString(R.string.result_fail)
//                            _status.value = LoadApiStatus.ERROR
//                            friend = null
//                            count++
//                        }
//                    }
//                    if (count == totalCount) {
//                        _chatDetail.value = chatList
//                        _status.value = LoadApiStatus.DONE
//                    }
//                }
//
//            } else {
//                _isChatListEmpty.value = true
//                _status.value = LoadApiStatus.DONE
//            }
//
//        }
//
//
//    }


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



    fun getProfile(chatRooms: List<ChatRoom>){
        val list = mutableListOf<Chat>()
        val totalCount = chatRooms.size
        var count = 0

        chatRooms.forEach { room ->
            val userId = room.talker.filterNot { it == myId }[0]
            Log.d("ChatTag","userId = ${userId}")

            coroutineScope.launch {
                _status.value = LoadApiStatus.LOADING
                when (val result = repository.getUserProfile(userId)) {
                        is Result.Success -> {
                            _error.value = null
                            _status.value = LoadApiStatus.DONE
                            val profile = result.data
                            val chat = Chat(chatRoom = room, friendProfile = profile)
                            list.add(chat)
                            count ++
                        }
                        is Result.Fail -> {
                            _error.value = result.error
                            _status.value = LoadApiStatus.ERROR
                            count ++
                        }
                        is Result.Error -> {
                            _error.value = result.exception.toString()
                            _status.value = LoadApiStatus.ERROR
                            count ++
                        }
                        else -> {
                            _error.value = MyApplication.instance.getString(R.string.result_fail)
                            _status.value = LoadApiStatus.ERROR
                            count ++
                        }
                    }
                if (count == totalCount){
                    _chatList.value = list
                    Log.d("ChatTag","after getProfile, get chatList = ${_chatList.value}")
                }
            }
        }
    }


    fun getLiveMessage(chatRoomId: String): MutableLiveData<List<Message>> {
        _status.value = LoadApiStatus.LOADING
        val messageList = repository.getRoomMessage(chatRoomId)
        Log.d("Chat", "getLiveMessage = ${messageList.value}")
        _status.value = LoadApiStatus.DONE
        return messageList
    }

}
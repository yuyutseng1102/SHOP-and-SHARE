package com.chloe.shopshare.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.Chat
import com.chloe.shopshare.data.ChatRoom
import com.chloe.shopshare.data.Message
import com.chloe.shopshare.data.Result
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

    private val _chatList = MutableLiveData<List<Chat>>()
    val chatList: LiveData<List<Chat>>
        get() = _chatList

    private val _isChatListEmpty = MutableLiveData<Boolean?>()
    val isChatListEmpty: LiveData<Boolean?>
        get() = _isChatListEmpty

    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    private var viewModelJob = Job()

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

    fun isChatListEmpty(){
        _isChatListEmpty.value = true
    }

    fun isChatListNotEmpty(){
        _isChatListEmpty.value = null
    }

    fun getProfile(chatRooms: List<ChatRoom>){
        val list = mutableListOf<Chat>()
        val totalCount = chatRooms.size
        var count = 0

        chatRooms.forEach { room ->
            val userId = room.talker.filterNot { it == myId }[0]
            Log.d("ChatTag","userId = $userId")

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
package com.chloe.shopshare.chatroom

import android.net.Uri
import com.chloe.shopshare.util.UserManager
import android.util.Log
import androidx.core.net.toUri
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
    private val repository: Repository, private val args: ChatRoom
):ViewModel() {

    private val _chatRoom = MutableLiveData<ChatRoom>().apply {
        value = args
    }
    val chatRoom: LiveData<ChatRoom>
        get() =  _chatRoom

    val myId: String
        get() =  UserManager.userId?:""

    private val _image = MutableLiveData<String>()
    val image: LiveData<String>
        get() =  _image

    private val _friendProfile = MutableLiveData<User>()
    val friendProfile: LiveData<User>
        get() =  _friendProfile



    val editMessage = MutableLiveData<String?>()

    var messageList = MutableLiveData<List<Message>>()

    private var _message = MutableLiveData<Message>()
    val message: LiveData<Message>
        get() =  _message

    private val _uploadImageDone = MutableLiveData<Boolean>()
    val uploadImageDone: LiveData<Boolean>
        get() = _uploadImageDone

    private val _sendMessageDone = MutableLiveData<Boolean>()
    val sendMessageDone: LiveData<Boolean>
        get() = _sendMessageDone

    private val _navigateToDialog = MutableLiveData<String?>()
    val navigateToDialog: LiveData<String?>
        get() =  _navigateToDialog

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

    fun navigateToDialog(image: String){
        _navigateToDialog.value = image
    }

    fun onDialogNavigated(){
        _navigateToDialog.value = null
    }

    fun getFriendId(chatRoom: ChatRoom){
        val list = chatRoom.talker.filterNot { it == myId }
        getFriendProfile(list[0])
    }

    private fun getFriendProfile(friendId: String) {

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
        }
    }

    fun getLiveMessage(chatRoomId: String) {
        _status.value = LoadApiStatus.LOADING
        messageList = repository.getRoomMessage(chatRoomId)
        Log.d("Chat","getLiveMessage = ${messageList.value}")
        _status.value = LoadApiStatus.DONE
    }

    fun editMessage(editMessage: String) {
        Log.d("Chat","editMessage = $editMessage")
        _message.value = Message(
            talkerId = myId ,
            message = editMessage
        )

        _chatRoom.value?.let { chatRoom ->
            _message.value?.let { message ->
                sendMessage(chatRoom.id, message)
            }
        }
    }

    fun pickImages(uri: Uri){
        uploadImages(uri)
    }

    fun sendImages(image: String){

        Log.d("Chat","pickImages = $image")
            _message.value = Message(talkerId = myId, image = image)

        _chatRoom.value?.let { chatRoom ->
            _message.value?.let { message ->
                sendMessage(chatRoom.id, message)
            }
        }

    }

    private fun sendMessage(chatRoomId: String, message: Message) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING
            val result = repository.sendMessage(chatRoomId, message)
            _sendMessageDone.value =
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

    private fun uploadImages(imageUri : Uri) {

            coroutineScope.launch {
                _status.value = LoadApiStatus.LOADING
                val result = repository.uploadImage(imageUri, "message")
                _image.value =
                when (result) {
                    is Result.Success -> {
                        _error.value = null
                        _status.value = LoadApiStatus.DONE
                        Log.d("Chloe","download uri is ${result.data}")
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
                _uploadImageDone.value = true
            }
        }

    fun onUploadImageDone() {
        _uploadImageDone.value = null
    }

    fun onSendMessageDone() {
        _sendMessageDone.value = null
    }



}
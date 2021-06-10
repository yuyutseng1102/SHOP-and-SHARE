package com.chloe.shopshare.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chloe.shopshare.chatroom.ChatRoomViewModel
import com.chloe.shopshare.data.source.Repository

class ChatRoomViewModelFactory(
    private val repository: Repository,
    private val myId: String,
    private val friendId: String,
    private val chatRoomId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(ChatRoomViewModel::class.java) ->
                    ChatRoomViewModel(repository, myId, friendId, chatRoomId)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
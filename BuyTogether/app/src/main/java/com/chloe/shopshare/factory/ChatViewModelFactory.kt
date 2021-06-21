package com.chloe.shopshare.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chloe.shopshare.chatroom.ChatRoomViewModel
import com.chloe.shopshare.data.ChatRoom
import com.chloe.shopshare.data.source.Repository

@Suppress("UNCHECKED_CAST")
class ChatViewModelFactory(private val repository: Repository, private val chatRoom: ChatRoom) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(ChatRoomViewModel::class.java) -> ChatRoomViewModel(repository, chatRoom)
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
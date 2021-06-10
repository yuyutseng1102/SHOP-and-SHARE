package com.chloe.shopshare.chat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chloe.shopshare.data.ChatDetail
import com.chloe.shopshare.data.ChatRoom
import com.chloe.shopshare.data.Message
import com.chloe.shopshare.databinding.ItemChatBinding

class ChatAdapter(private val viewModel: ChatViewModel) : ListAdapter<ChatRoom, ChatAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: ItemChatBinding):
        RecyclerView.ViewHolder(binding.root) {
//
//        private var _messageList = MutableLiveData<List<Message>>()
//        val messageList: LiveData<List<Message>>
//            get() =  _messageList
//
//

        fun bind(item: ChatRoom,viewModel: ChatViewModel) {
//            binding.lifecycleOwner = this
            binding.item = item
//            _messageList = viewModel.getLiveMessage(item.chatRoom!!.id)
//            Log.d("Chat","_messageList first = ${_messageList.value}")
//            item.message = _messageList.value


//            _messageList.observe(this, Observer {
//                it.let {
//                    Log.d("Chat","_messageList = $it")
//                    item.message = it
//                    Log.d("Chat","message = ${item.message}")
//                    binding.item = item
//                }
//            })

//            binding.viewModel = viewModel
            binding.executePendingBindings()
        }


//        private val lifecycleRegistry = LifecycleRegistry(this)
//
//        init {
//            lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
//        }
//
//        fun markAttach() {
//            lifecycleRegistry.currentState = Lifecycle.State.STARTED
//        }
//
//        fun markDetach() {
//            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
//        }
//
//        override fun getLifecycle(): Lifecycle {
//            return lifecycleRegistry
//        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ChatRoom>() {
        override fun areItemsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,viewModel)

    }

}
package com.chloe.shopshare.chatroom

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chloe.shopshare.data.Message
import com.chloe.shopshare.databinding.ItemMessageLeftBinding
import com.chloe.shopshare.databinding.ItemMessageRightBinding
import com.chloe.shopshare.util.UserManager

class ChatRoomMessageAdapter(private val viewModel: ChatRoomViewModel):ListAdapter<Message, RecyclerView.ViewHolder>(DiffCallback)  {

    class RightBubbleViewHolder(private var binding: ItemMessageRightBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Message,viewModel: ChatRoomViewModel) {
            Log.d("Chat","RightBubbleViewHolder")
            binding.item = item
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

    class LeftBubbleViewHolder(private var binding: ItemMessageLeftBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Message,viewModel: ChatRoomViewModel) {
            Log.d("Chat","LeftBubbleViewHolder")
            binding.item = item
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.id == newItem.id
        }

        private const val ITEM_MESSAGE_BY_ME = 0
        private const val ITEM_MESSAGE_BY_FRIEND = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            ITEM_MESSAGE_BY_ME -> RightBubbleViewHolder(ItemMessageRightBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
            ITEM_MESSAGE_BY_FRIEND -> LeftBubbleViewHolder(ItemMessageLeftBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is RightBubbleViewHolder -> {
                holder.bind((getItem(position)),viewModel)
            }
            is LeftBubbleViewHolder -> {
                holder.bind((getItem(position)),viewModel)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)

        return when (item.talkerId) {
            UserManager.userId -> ITEM_MESSAGE_BY_ME
            else -> ITEM_MESSAGE_BY_FRIEND
        }
    }

}

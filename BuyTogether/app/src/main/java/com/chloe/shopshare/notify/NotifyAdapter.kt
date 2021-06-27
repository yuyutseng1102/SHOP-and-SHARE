package com.chloe.shopshare.notify

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chloe.shopshare.data.Notify
import com.chloe.shopshare.databinding.ItemNotifyBinding

class NotifyAdapter(private val viewModel: NotifyViewModel) :
    ListAdapter<Notify, NotifyAdapter.ViewHolder>(DiffCallback) {


    class ViewHolder(private var binding: ItemNotifyBinding) :

        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Notify, viewModel: NotifyViewModel) {
            binding.item = item
            binding.viewModel = viewModel
            binding.notifyExpandButton.setOnCheckedChangeListener { _, isChecked ->
                when (isChecked) {
                    true -> {
                        item.isExpand = true
                        binding.isExpand = isChecked
                        binding.messageBlock.visibility = View.VISIBLE
                        NotifyAdapter(viewModel).notifyDataSetChanged()
                    }
                    else -> {
                        item.isExpand = false
                        binding.isExpand = isChecked
                        binding.messageBlock.visibility = View.GONE
                        NotifyAdapter(viewModel).notifyDataSetChanged()
                    }
                }
            }
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Notify>() {
        override fun areItemsTheSame(oldItem: Notify, newItem: Notify): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Notify, newItem: Notify): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNotifyBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, viewModel)
    }

    fun getNotify(position: Int): Notify {
        return getItem(position)
    }

}
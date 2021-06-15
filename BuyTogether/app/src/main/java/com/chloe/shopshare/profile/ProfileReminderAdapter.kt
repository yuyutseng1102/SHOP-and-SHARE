package com.chloe.shopshare.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.databinding.ItemProfileReminderBinding

class ProfileReminderAdapter(val viewModel: ProfileViewModel)  : ListAdapter<Shop, ProfileReminderAdapter.ViewHolder>(
    DiffCallback
) {

    class ViewHolder(private var binding: ItemProfileReminderBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Shop, viewModel: ProfileViewModel) {
            binding.item = item
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Shop>() {
        override fun areItemsTheSame(oldItem: Shop, newItem: Shop): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Shop, newItem: Shop): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemProfileReminderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,viewModel)
    }

}

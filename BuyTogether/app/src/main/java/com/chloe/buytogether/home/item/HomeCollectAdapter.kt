package com.chloe.buytogether.home.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chloe.buytogether.data.Collections
import com.chloe.buytogether.databinding.ItemHomeCollectionBinding
import com.chloe.buytogether.databinding.ItemHomeHotsBinding

class HomeCollectAdapter(private val viewModel: HomeCollectViewModel)  : ListAdapter<Collections, HomeCollectAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: ItemHomeCollectionBinding):
            RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Collections,viewModel: HomeCollectViewModel) {
            binding.item = item
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Collections>() {
        override fun areItemsTheSame(oldItem: Collections, newItem: Collections): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Collections, newItem: Collections): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHomeCollectionBinding.inflate(
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

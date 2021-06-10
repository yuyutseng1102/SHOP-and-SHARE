package com.chloe.shopshare.detail.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chloe.shopshare.databinding.ItemDetailDeliveryBinding
import com.chloe.shopshare.databinding.ItemProductListBinding

class DetailDeliveryAdapter : ListAdapter<Int, DetailDeliveryAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: ItemDetailDeliveryBinding):
            RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Int) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                ItemDetailDeliveryBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                )
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}
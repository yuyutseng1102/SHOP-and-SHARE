package com.chloe.buytogether.collection.manage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chloe.buytogether.collection.CollectionAdapter
import com.chloe.buytogether.collection.CollectionViewModel
import com.chloe.buytogether.data.Collections
import com.chloe.buytogether.data.Order
import com.chloe.buytogether.databinding.ItemCollectionListBinding
import com.chloe.buytogether.databinding.ItemCollectionManageMemberBinding

class MemberAdapter  : ListAdapter<Order, MemberAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: ItemCollectionManageMemberBinding):
            RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Order,position: Int) {
            binding.item = item
            binding.memberNumber.text = (position+1).toString()
            binding.recyclerProduct.adapter = MemberProductAdapter()
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                ItemCollectionManageMemberBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false))
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,position)
    }

}
package com.chloe.shopshare.manage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chloe.shopshare.data.Product
import com.chloe.shopshare.databinding.ItemManageProductBinding

class MemberProductAdapter: ListAdapter<Product, MemberProductAdapter.ViewHolder>(
    DiffCallback
) {

    class ViewHolder(private var binding: ItemManageProductBinding):
            RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product,position: Int) {
            binding.item = item
            binding.position = position+1
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemManageProductBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,position)
    }

}
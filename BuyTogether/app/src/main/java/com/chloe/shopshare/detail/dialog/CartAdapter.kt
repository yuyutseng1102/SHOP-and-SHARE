package com.chloe.shopshare.detail.dialog

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chloe.shopshare.data.Product
import com.chloe.shopshare.databinding.ItemCartBinding

class CartAdapter(private val viewModel: CartViewModel) : ListAdapter<Product, CartAdapter.ViewHolder>(
    DiffCallback
) {

    class ViewHolder(private var binding: ItemCartBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product,viewModel: CartViewModel) {
            binding.item = item
            binding.viewModel = viewModel
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
            ItemCartBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        Log.d("Chloe","item = ${item.quantity}")
        holder.bind(item,viewModel)
    }

}
package com.chloe.buytogether.detail.dialog

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chloe.buytogether.data.Product
import com.chloe.buytogether.databinding.ItemGatherOptionBinding
import com.chloe.buytogether.databinding.ItemProductListBinding

class ProductListAdapter(private val viewModel: ProductListViewModel) : ListAdapter<Product, ProductListAdapter.ViewHolder>(
    DiffCallback
) {

    class ViewHolder(private var binding: ItemProductListBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product,viewModel: ProductListViewModel) {
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
            ItemProductListBinding.inflate(
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
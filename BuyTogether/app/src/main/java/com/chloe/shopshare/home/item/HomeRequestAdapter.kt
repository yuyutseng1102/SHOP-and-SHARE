package com.chloe.shopshare.home.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chloe.shopshare.data.Request
import com.chloe.shopshare.databinding.ItemHomeRequestBinding

class HomeRequestAdapter(private val viewModel: HomeRequestViewModel)  : ListAdapter<Request, HomeRequestAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: ItemHomeRequestBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Request, viewModel: HomeRequestViewModel) {
            binding.item = item
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Request>() {
        override fun areItemsTheSame(oldItem: Request, newItem: Request): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Request, newItem: Request): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHomeRequestBinding.inflate(
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
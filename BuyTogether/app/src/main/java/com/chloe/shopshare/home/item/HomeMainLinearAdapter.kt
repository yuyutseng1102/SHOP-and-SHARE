package com.chloe.shopshare.home.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.databinding.ItemHomeLinearBinding

class HomeMainLinearAdapter(private val  viewModel: HomeMainViewModel): ListAdapter<Shop, HomeMainLinearAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: ItemHomeLinearBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Shop, position: Int, viewModel: HomeMainViewModel) {
            binding.item = item
            if (position<=3){
                binding.markText.text = position.toString()
            }else{
                binding.markText.visibility = View.GONE
                binding.markImage.visibility = View.GONE
            }
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
        return ViewHolder(ItemHomeLinearBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,position+1,viewModel)
    }
}

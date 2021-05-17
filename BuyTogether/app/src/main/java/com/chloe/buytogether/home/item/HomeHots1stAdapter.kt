package com.chloe.buytogether.home.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chloe.buytogether.data.Collections
import com.chloe.buytogether.databinding.ItemHomeHotsBinding

class HomeHots1stAdapter(private val onClickListener: HomeCollectAdapter.OnClickListener): ListAdapter<Collections, HomeHots1stAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: ItemHomeHotsBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Collections,position: Int) {
            binding.item = item
            if (position<=3){
                binding.markText.text = position.toString()
            }else{
                binding.markText.visibility = View.GONE
                binding.markImage.visibility = View.GONE
            }
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
        return ViewHolder(ItemHomeHotsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(item)
        }
        holder.bind(item,position+1)
    }
}

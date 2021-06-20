package com.chloe.shopshare.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chloe.shopshare.data.MyOrder
import com.chloe.shopshare.databinding.ItemMyOrderBinding
import com.chloe.shopshare.databinding.ItemProfileShopBinding

class ProfileOrderAdapter(val onClickListener: OnClickListener) : ListAdapter<MyOrder, ProfileOrderAdapter.ViewHolder>(
    DiffCallback
) {

    class ViewHolder(private var binding: ItemProfileShopBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(item: MyOrder) {
            binding.item = item.shop
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<MyOrder>() {
        override fun areItemsTheSame(oldItem: MyOrder, newItem: MyOrder): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: MyOrder, newItem: MyOrder): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemProfileShopBinding.inflate(
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
        holder.itemView.setOnClickListener {
            onClickListener.onClick(item)
        }

    }

    class OnClickListener(val clickListener: (item: MyOrder) -> Unit) {
        fun onClick(item: MyOrder) = clickListener(item)
    }

}

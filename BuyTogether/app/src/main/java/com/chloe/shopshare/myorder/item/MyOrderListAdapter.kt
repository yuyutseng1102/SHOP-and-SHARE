package com.chloe.shopshare.myorder.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chloe.shopshare.data.MyOrder
import com.chloe.shopshare.data.MyOrderDetailKey
import com.chloe.shopshare.databinding.ItemMyOrderBinding

class MyOrderListAdapter(private val viewModel: MyOrderListViewModel) : ListAdapter<MyOrder, MyOrderListAdapter.ViewHolder>(
    DiffCallback
) {

    class ViewHolder(private var binding: ItemMyOrderBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(item: MyOrder, viewModel: MyOrderListViewModel) {
            binding.item = item
            binding.viewModel = viewModel
            binding.orderDetail = MyOrderDetailKey(shopId = item.shop.id, orderId = item.order.id)
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
            ItemMyOrderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,viewModel)

    }

}

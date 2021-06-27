package com.chloe.shopshare.home.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.databinding.ItemHomeHostBinding

class HomeHostingAdapter(private val viewModel: HomeHostViewModel)  : ListAdapter<Shop, HomeHostingAdapter.ViewHolder>(DiffCallback) {


    class ViewHolder(private var binding: ItemHomeHostBinding):
            RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Shop, viewModel: HomeHostViewModel) {

            binding.item = item

            binding.viewModel = viewModel

            binding.isShopLiked = isShopLiked(item, viewModel.likes.value?: listOf())

            HomeHostingAdapter(viewModel).notifyDataSetChanged()

            binding.checkBoxLike.setOnCheckedChangeListener { _, isChecked ->
                when (isChecked) {
                    true -> viewModel.likeShop(viewModel.userId, item.id)
                    else -> viewModel.dislikeShop(viewModel.userId, item.id)
                }
            }

            binding.executePendingBindings()
        }

        private fun isShopLiked(item: Shop, likeList: List<String>): Boolean {
            val like = likeList.filter { it == item.id }
            return !like.isNullOrEmpty()
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
        return ViewHolder(ItemHomeHostBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,viewModel)
    }

}

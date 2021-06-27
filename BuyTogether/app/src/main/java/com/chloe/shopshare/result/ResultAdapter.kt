package com.chloe.shopshare.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.databinding.ItemResultBinding

class ResultAdapter(private val viewModel: ResultViewModel) :
    ListAdapter<Shop, ResultAdapter.ViewHolder>(DiffCallback) {


    class ViewHolder(private var binding: ItemResultBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: Shop, viewModel: ResultViewModel) {
            binding.item = item
            binding.viewModel = viewModel
            binding.isShopLiked = isShopLiked(item, viewModel.shopLikedList.value ?: listOf())
            ResultAdapter(viewModel).notifyDataSetChanged()
            binding.checkBoxLike.setOnCheckedChangeListener { _, isChecked ->
                when (isChecked) {
                    true -> viewModel.addShopLiked(viewModel.userId, item.id)
                    else -> viewModel.removeShopLiked(viewModel.userId, item.id)
                }
            }
            binding.executePendingBindings()
        }

        private fun isShopLiked(item: Shop, likeList: List<String>): Boolean {
            var like = ""
            for (shop in likeList) {
                if (shop == item.id) {
                    like = shop
                }
            }
            return like.isNotEmpty()
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
        return ViewHolder(
            ItemResultBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, viewModel)
    }
}

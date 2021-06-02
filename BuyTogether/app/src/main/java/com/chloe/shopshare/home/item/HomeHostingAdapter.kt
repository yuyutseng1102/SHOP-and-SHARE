package com.chloe.shopshare.home.item

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.ShopItem
import com.chloe.shopshare.databinding.ItemHomeCollectionBinding

class HomeHostingAdapter(private val viewModel: HomeHostViewModel)  : ListAdapter<Shop, HomeHostingAdapter.ViewHolder>(DiffCallback) {


    class ViewHolder(private var binding: ItemHomeCollectionBinding):
            RecyclerView.ViewHolder(binding.root) {



        fun bind(item: Shop, viewModel: HomeHostViewModel) {
            binding.item = item
            binding.viewModel = viewModel
            binding.isShopLiked = isShopLiked(item, viewModel.shopLikedList.value?: listOf())
            HomeHostingAdapter(viewModel).notifyDataSetChanged()
            binding.checkBoxLike.setOnCheckedChangeListener { _, isChecked ->
                Log.d("LikeTag", "check")
                when (isChecked) {
                    true -> {
//                        binding.isShopLiked = isChecked
                        Log.d("LikeTag", "add = viewModel.userId=${viewModel.userId}, item.id = ${item.id}")
                        viewModel.addShopLiked(viewModel.userId, item.id)
                    }
                    else -> {
//                        binding.isShopLiked = isChecked
                        Log.d("LikeTag", "remove = viewModel.userId=${viewModel.userId}, item.id = ${item.id}")
                        viewModel.removeShopLiked(viewModel.userId, item.id)
                    }
                }
            }
            binding.executePendingBindings()
        }

        private fun isShopLiked(item: Shop, likeList: List<String>):Boolean{
            var like  = ""
            for (shop in likeList){
                Log.d("LikeTag","shop loop = ${shop}")
                if (shop == item.id){
                    like =  shop
                }
            }
            Log.d("LikeTag","like checked = ${like.isNotEmpty()}")
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
        return ViewHolder(ItemHomeCollectionBinding.inflate(
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

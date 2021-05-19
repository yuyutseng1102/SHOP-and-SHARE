package com.chloe.buytogether.collection.manage


import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chloe.buytogether.bindEditorMemberChecked
import com.chloe.buytogether.data.Collections
import com.chloe.buytogether.data.Order
import com.chloe.buytogether.databinding.ItemCollectionManageMemberBinding
import com.chloe.buytogether.home.item.HomeCollectAdapter
import okhttp3.internal.notify

class MemberAdapter(private val viewModel: CollectionManageViewModel) : ListAdapter<Order, MemberAdapter.ViewHolder>(DiffCallback) {



    class ViewHolder(private var binding: ItemCollectionManageMemberBinding):
            RecyclerView.ViewHolder(binding.root) {


val checkButton = binding.buttonMemberCheck

        @SuppressLint("SetTextI18n")
        fun bind(item: Order, viewModel: CollectionManageViewModel, position:Int) {
            binding.item = item
            binding.isChecked = item.isCheck
            Log.d("checkChloe","binding.isChecked = ${item.isCheck}")
            binding.memberNumber.text = (position + 1).toString()
            binding.recyclerProduct.adapter = MemberProductAdapter()
            binding.viewModel = viewModel
            binding.viewHolder = this
            checkButton.setOnCheckedChangeListener { _, isChecked ->
                binding.isChecked = isChecked
                item.isCheck = isChecked
                viewModel.checkAgain(item, position)
                Log.d("checkChloe", "isChecked")
            }
            binding.executePendingBindings()
            }
        }


    companion object DiffCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                ItemCollectionManageMemberBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false))
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,viewModel,position)
        }

}


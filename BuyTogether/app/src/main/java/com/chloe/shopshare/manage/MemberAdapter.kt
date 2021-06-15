package com.chloe.shopshare.manage


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chloe.shopshare.data.Order
import com.chloe.shopshare.databinding.ItemManageMemberBinding
import com.chloe.shopshare.util.UserManager

class MemberAdapter(private val viewModel: ManageViewModel) : ListAdapter<Order, MemberAdapter.ViewHolder>(
    DiffCallback
) {

    private lateinit var context: Context

    class ViewHolder(private var binding: ItemManageMemberBinding):
            RecyclerView.ViewHolder(binding.root), LifecycleOwner {


        val livePosition = MutableLiveData<Int>()
        val button = binding.buttonMemberCheck



        fun bind(item: Order, viewModel: ManageViewModel, position:Int) {
            binding.lifecycleOwner = this
            binding.item = item
            binding.isChecked = item.isCheck
            livePosition.value = position
            Log.d("checkChloe","binding.isChecked = ${item.isCheck}")
//            binding.memberNumber.text = (position + 1).toString()
            binding.messageIcon.isEnabled = item.userId != UserManager.userId
            binding.recyclerProduct.adapter =
                MemberProductAdapter()

            binding.viewModel = viewModel
            binding.viewHolder = this

            binding.buttonMemberCheck.setOnCheckedChangeListener { _, isChecked ->
                livePosition.value = position
                Log.d("checkChloe", "livePosition = ${livePosition.value}")
                binding.isChecked = isChecked
                item.isCheck = isChecked

                viewModel.checkAgain(item, livePosition.value!!)
                Log.d("checkChloe", "isChecked")

            }
            binding.executePendingBindings()
            }

        private val lifecycleRegistry = LifecycleRegistry(this)

        init {
            lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
            Log.d("Life","lifecycleRegistry.currentState  is ${lifecycleRegistry.currentState }")
        }

        fun onAttach() {
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
            Log.d("Life","lifecycleRegistry.currentState  is ${lifecycleRegistry.currentState }")
        }

        fun onDetach() {
            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
            Log.d("Life","lifecycleRegistry.currentState  is ${lifecycleRegistry.currentState }")
        }

        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onAttach()

    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.onDetach()
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
        context = parent.context
        return ViewHolder(
            ItemManageMemberBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,viewModel,position)

//        holder.button.setOnCheckedChangeListener { _, isChecked ->
//            holder.check = isChecked
//            item.isCheck = isChecked
//            viewModel.checkAgain(item, position)
//            Log.d("checkChloe", "isChecked")
//
//        }


    }

}


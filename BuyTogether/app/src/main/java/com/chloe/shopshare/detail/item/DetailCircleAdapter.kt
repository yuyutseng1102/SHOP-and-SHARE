package com.chloe.shopshare.detail.item

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.chloe.shopshare.databinding.ItemDetailCircleBinding

class DetailCircleAdapter : RecyclerView.Adapter<DetailCircleAdapter.ImageViewHolder>() {

    private var count = 0
    var selectedPosition = MutableLiveData<Int>()

    class ImageViewHolder(val binding: ItemDetailCircleBinding) :
        RecyclerView.ViewHolder(binding.root),
        LifecycleOwner {

        fun bind(selectedPosition: MutableLiveData<Int>) {
            selectedPosition.observe(this, Observer {
                binding.lifecycleOwner = this
                binding.isSelected = it == adapterPosition
                Log.d("CircleTag", "binding.isSelected = ${binding.isSelected}")
                binding.executePendingBindings()
            })
        }

        private val lifecycleRegistry = LifecycleRegistry(this)

        init {
            lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
        }

        fun onAttach() {
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
        }

        fun onDetach() {
            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        }

        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemDetailCircleBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(selectedPosition)
    }

    override fun onViewAttachedToWindow(holder: ImageViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onAttach()
    }

    override fun onViewDetachedFromWindow(holder: ImageViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.onDetach()
    }

    override fun getItemCount(): Int {
        return count
    }

    fun submitCount(count: Int) {
        this.count = count
        notifyDataSetChanged()
    }
}

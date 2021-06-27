package com.chloe.shopshare.detail.item

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.chloe.shopshare.R
import com.chloe.shopshare.databinding.ItemDetailImageBinding

class DetailImageAdapter : RecyclerView.Adapter<DetailImageAdapter.ImageViewHolder>() {

    private lateinit var context: Context
    private var images: List<String>? = null

    class ImageViewHolder(private var binding: ItemDetailImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, imageUrl: String) {

            imageUrl.let {
                binding.imageUrl = it

                // Make sure the size of each image item can display correct
                val displayMetrics = DisplayMetrics()
                (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
                binding.imageDetail.layoutParams = ConstraintLayout.LayoutParams(
                    displayMetrics.widthPixels,
                    context.resources.getDimensionPixelSize(R.dimen.height_detail_gallery)
                )

                binding.executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        context = parent.context
        return ImageViewHolder(
            ItemDetailImageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        images?.let {
            holder.bind(context, it[getRealPosition(position)])
        }
    }

    override fun getItemCount(): Int {
        return images?.let { Int.MAX_VALUE } ?: 0
    }

    private fun getRealPosition(position: Int): Int = images?.let {
        position % it.size
    } ?: 0

    fun submitImages(images: List<String>) {
        this.images = images
        notifyDataSetChanged()
    }
}

package com.chloe.buytogether

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chloe.buytogether.data.Collections
import com.chloe.buytogether.home.item.HomeCollectAdapter
import com.chloe.buytogether.home.item.HomeGridAdapter
import com.chloe.buytogether.home.item.HomeHots1stAdapter
import com.chloe.buytogether.home.item.HomeHots2ndAdapter

/**
 * According to [LoadApiStatus] to decide the visibility of [ProgressBar]
 */
//@BindingAdapter("setupApiStatus")
//fun bindApiStatus(){
//}

/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder))
            .into(imgView)
    }
}

@BindingAdapter("collections")
fun bindRecyclerViewWithProducts(recyclerView: RecyclerView, collections: List<Collections>?) {
    collections?.let {
        recyclerView.adapter?.apply {
            when (this) {
                is HomeHots1stAdapter -> submitList(it)
                is HomeHots2ndAdapter -> submitList(it)
                is HomeCollectAdapter -> submitList(it)
                is HomeGridAdapter -> submitList(it)
            }
        }
    }
}



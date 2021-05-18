package com.chloe.buytogether

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chloe.buytogether.collection.CollectionAdapter
import com.chloe.buytogether.collection.manage.MemberAdapter
import com.chloe.buytogether.data.Collections
import com.chloe.buytogether.data.Order
import com.chloe.buytogether.ext.toDisplayFormat
import com.chloe.buytogether.gather.item.GatherOptionAdapter
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
                is CollectionAdapter -> submitList(it)
            }
        }
    }
}

@BindingAdapter("options")
fun bindRecyclerViewWithStrings(recyclerView: RecyclerView, options: List<String>?) {
    options?.let {
        recyclerView.adapter?.apply {
            Log.d("Chloe","summit the option list is ${options}")
            when (this) {
                is GatherOptionAdapter -> submitList(it)
            }
        }
    }
}

@BindingAdapter("orders")
fun bindRecyclerViewWithOrders(recyclerView: RecyclerView, orders: List<Order>) {
    orders?.let {
        recyclerView.adapter?.apply {
            Log.d("Chloe","summit the option list is ${orders}")
            when (this) {
                is MemberAdapter -> submitList(it)
            }
        }
    }
}

@BindingAdapter("timeToDisplayFormat")
fun bindDisplayFormatTime(textView: TextView, time: Long?) {
    textView.text = time?.toDisplayFormat()
}


@BindingAdapter("deadLineToDisplay","conditionType","conditionToDisplay")
fun bindDisplayCondition(textView: TextView,deadLine:Long?,conditionType:Int?,condition:Int?) {

    val deadLineToDisplay: String? = "預計${deadLine?.toDisplayFormat()}收團"

    val conditionToDisplay: String? =
        when (conditionType) {
            0 -> "滿額NT$${condition}止"
            1 -> "徵滿${condition}份止"
            2 -> "徵滿${condition}人止"
            else -> ""
        }

    textView.text =
        if (deadLine == null) {
            conditionToDisplay
        } else if (condition == null) {
            deadLineToDisplay
        } else {
            deadLineToDisplay + "或" + conditionToDisplay
        }
}

@BindingAdapter("statusToDisplay")
fun bindDisplayStatus(textView:TextView,status:Int) {

    textView.text =
        when (status) {
            0 -> MyApplication.instance.getString(R.string.gathering)
            1 -> MyApplication.instance.getString(R.string.gather_success)
            2 -> MyApplication.instance.getString(R.string.order_success)
            3 -> MyApplication.instance.getString(R.string.shop_shipment)
            4 -> MyApplication.instance.getString(R.string.shipment_success)
            5 -> MyApplication.instance.getString(R.string.packaging)
            6 -> MyApplication.instance.getString(R.string.shipment)
            7 -> MyApplication.instance.getString(R.string.finish)
            else -> ""
        }

}



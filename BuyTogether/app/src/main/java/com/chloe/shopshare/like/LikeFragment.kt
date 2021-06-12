package com.chloe.shopshare.like

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.R
import com.chloe.shopshare.databinding.FragmentLikeBinding
import com.chloe.shopshare.ext.getVmFactory
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class LikeFragment : Fragment() {

    private val viewModel by viewModels<LikeViewModel> { getVmFactory() }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLikeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = LikeAdapter(viewModel)
        binding.recyclerLike.adapter = adapter

        viewModel.likeList.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isNotEmpty()){
                    Log.d("LikeTag","likeList observe= ${viewModel.likeList.value}")
                    viewModel.getShopLikedDetail()
                }
            }
        })

        viewModel.successGetShop.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.shop.observe(viewLifecycleOwner, Observer {
                    it?.let {
                        Log.d("LikeTag", "submitList shop to like recycler")
                        adapter.submitList(it)
                    }
                }
                )
                viewModel.onShopLikedDetailGet()
            }
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(
                    NavigationDirections.navigateToDetailFragment(it)
                )
                viewModel.onDetailNavigated()
            }
        })
//
//        var deleteIcon = MyApplication.instance.getDrawable(R.drawable.ic_baseline_delete_24)
//        val intrinsicWidth = deleteIcon?.intrinsicWidth
//        val intrinsicHeight = deleteIcon?.intrinsicHeight
//        var background = ColorDrawable()
//        var backgroundColor = MyApplication.instance.getColor(R.color.textColorError)
//        val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
//
//
//        val itemTouchHelperCallback =
//            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
//                    return false
//                }
//
//                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                    Log.d("Notify","onSwiped at ${adapter.getShopLiked(viewHolder.adapterPosition)}")
//                    if (adapter.itemCount == 0){
//                        backgroundColor = MyApplication.instance.getColor(R.color.transparent)
//                        deleteIcon?.setTint(MyApplication.instance.getColor(R.color.transparent))
//                    }
//                    viewModel.removeShopLiked(viewModel.userId,adapter.getShopLiked(viewHolder.adapterPosition))
//                }
//
//
//
//                override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
//
//                    val itemView = viewHolder.itemView
//                    val itemHeight = itemView.bottom - itemView.top
//                    val isCanceled = dX == 0f && !isCurrentlyActive
//
//                    if (isCanceled) {
//                        clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
//                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//                        return
//                    }
//
//
//                    // Draw the red delete background
//                    background.color = backgroundColor
//                    background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
//                    background.draw(c)
//
//                    // Calculate position of delete icon
//                    val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight!!) / 2
//                    val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
//                    val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth!!
//                    val deleteIconRight = itemView.right - deleteIconMargin
//                    val deleteIconBottom = deleteIconTop + intrinsicHeight
//
//                    // Draw the delete icon
//                    deleteIcon?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
//                    deleteIcon?.draw(c)
//
//                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//                }
//
//                private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
//                    c?.drawRect(left, top, right, bottom, clearPaint)
//                }
//
//            }
//
//        var itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
//        itemTouchHelper.attachToRecyclerView(binding.recyclerLike)
//



        return binding.root
    }

}
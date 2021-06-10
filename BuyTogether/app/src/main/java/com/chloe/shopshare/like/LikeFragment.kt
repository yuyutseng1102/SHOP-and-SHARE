package com.chloe.shopshare.like

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.databinding.FragmentLikeBinding
import com.chloe.shopshare.ext.getVmFactory

class LikeFragment : Fragment() {

    private val viewModel by viewModels<LikeViewModel> { getVmFactory() }

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


        return binding.root
    }

}
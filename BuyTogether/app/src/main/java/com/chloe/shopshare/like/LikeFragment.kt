package com.chloe.shopshare.like

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.databinding.FragmentLikeBinding
import com.chloe.shopshare.ext.getVmFactory


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
                if (it.isNotEmpty()) {
                    viewModel.getShopLikedDetail()
                }
            }
        })

        viewModel.successGetShop.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.shop.observe(viewLifecycleOwner, Observer { shop ->
                    shop?.let {
                        adapter.submitList(shop)
                    }
                })
                viewModel.onShopLikedDetailGet()
            }
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToDetailFragment(it))
                viewModel.onDetailNavigated()
            }
        })

        return binding.root
    }
}
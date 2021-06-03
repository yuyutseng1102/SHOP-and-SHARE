package com.chloe.shopshare.myrequest

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
import com.chloe.shopshare.R
import com.chloe.shopshare.databinding.FragmentLikeBinding
import com.chloe.shopshare.databinding.FragmentMyRequestBinding
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.like.LikeAdapter
import com.chloe.shopshare.like.LikeViewModel


class MyRequestFragment : Fragment() {

    private val viewModel by viewModels<MyRequestViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMyRequestBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val requestAdapter = MyRequestAdapter(viewModel)
        binding.recyclerMyRequest.adapter = requestAdapter

        binding.layoutSwipeRefreshMyRequest.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.refreshStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.layoutSwipeRefreshMyRequest.isRefreshing = it
            }
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(
                    NavigationDirections.navigateToRequestDetailFragment(it)
                )
                viewModel.onDetailNavigated()
            }
        })

        return binding.root
    }

}
package com.chloe.shopshare.myrequest.item

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.databinding.FragmentMyRequestListBinding
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.myrequest.MyRequestType


class MyRequestListFragment(private val myRequestType: MyRequestType) : Fragment() {

    private val viewModel by viewModels<MyRequestListViewModel> { getVmFactory(myRequestType) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMyRequestListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val requestAdapter =
            MyRequestListAdapter(viewModel)
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
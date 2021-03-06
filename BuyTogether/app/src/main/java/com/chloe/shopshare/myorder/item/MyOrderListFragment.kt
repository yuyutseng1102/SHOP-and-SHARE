package com.chloe.shopshare.myorder.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.databinding.FragmentMyOrderListBinding
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.myorder.MyOrderType

class MyOrderListFragment(private val myOrderType: MyOrderType) : Fragment() {

    private val viewModel by viewModels<MyOrderListViewModel> { getVmFactory(myOrderType) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMyOrderListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val orderAdapter = MyOrderListAdapter(viewModel)

        binding.recyclerMyOrder.adapter = orderAdapter

        binding.layoutSwipeRefreshMyRequest.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.refreshStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.layoutSwipeRefreshMyRequest.isRefreshing = it
            }
        })

        viewModel.navigateToOrderDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToOrderDetailFragment(it))
                viewModel.onOrderDetailNavigated()
            }
        })


        return binding.root
    }

}
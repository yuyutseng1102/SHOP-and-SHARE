package com.chloe.shopshare.myhost.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.databinding.FragmentMyHostListBinding
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.myhost.MyHostType


class MyHostListFragment(private val myHostType: MyHostType) : Fragment() {

    private val viewModel by viewModels<MyHostListViewModel> { getVmFactory(myHostType) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMyHostListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = MyHostListAdapter(viewModel)
        binding.recyclerCollection.adapter = adapter

        binding.layoutSwipeRefreshShop.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.refreshStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.layoutSwipeRefreshShop.isRefreshing = it
            }
        })

        viewModel.navigateToManage.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToManageFragment(it))
                viewModel.onManageNavigated()
            }
        })

        return binding.root
    }
}
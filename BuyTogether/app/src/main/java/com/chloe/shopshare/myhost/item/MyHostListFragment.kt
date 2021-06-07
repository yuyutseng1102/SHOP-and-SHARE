package com.chloe.shopshare.myhost.item

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chloe.shopshare.NavigationDirections
import androidx.lifecycle.Observer
import com.chloe.shopshare.databinding.FragmentMyHostListBinding
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.myhost.MyHostType
import com.chloe.shopshare.myorder.MyOrderType


class MyHostListFragment(private val myHostType: MyHostType) : Fragment() {

    private val viewModel by viewModels<MyHostListViewModel> {getVmFactory(myHostType)}


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMyHostListBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


//        viewModel.addMockData()

        binding.layoutSwipeRefreshShop.setOnRefreshListener {
            viewModel.refresh()
            Log.d("Chloe", "home status = ${viewModel.status.value}")
        }

        viewModel.refreshStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.layoutSwipeRefreshShop.isRefreshing = it
            }
        })


        val adapter = MyHostListAdapter(viewModel)
        binding.recyclerCollection.adapter = adapter

        viewModel.navigateToManage.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("navigate", "navigateToManage_01 = ${viewModel.navigateToManage}")
                findNavController().navigate(NavigationDirections.navigateToManageFragment(it))
                viewModel.onManageNavigated()
                Log.d("navigate", "navigateToDetail_02 = ${viewModel.navigateToManage}")
            }
        })


        return binding.root
    }



}
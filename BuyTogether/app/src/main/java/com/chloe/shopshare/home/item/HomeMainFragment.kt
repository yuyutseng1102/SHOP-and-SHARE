package com.chloe.shopshare.home.item

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
import com.chloe.shopshare.databinding.FragmentHomeMainBinding
import com.chloe.shopshare.ext.getVmFactory


class HomeMainFragment : Fragment() {


    private val viewModel by viewModels<HomeMainViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeMainBinding.inflate(inflater,container,false)

        val linearAdapter = HomeMainLinearAdapter(viewModel)

        val adapter2nd = HomeHots2ndAdapter(viewModel)

        val gridAdapter = HomeMainGridAdapter(viewModel)

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToDetailFragment(it))
                viewModel.onDetailNavigated()
            }
        })

        binding.layoutSwipeRefreshHome.setOnRefreshListener {
            viewModel.refresh()
            Log.d("Chloe", "home status = ${viewModel.status.value}")
        }

        viewModel.refreshStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.layoutSwipeRefreshHome.isRefreshing = it
            }
        })


        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.recyclerHots1st.adapter = linearAdapter
        binding.recyclerHots2nd.adapter = adapter2nd
        binding.recyclerNew.adapter = gridAdapter


        return binding.root
    }

}
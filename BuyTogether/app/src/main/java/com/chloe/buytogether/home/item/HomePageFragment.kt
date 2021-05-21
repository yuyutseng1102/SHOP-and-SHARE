package com.chloe.buytogether.home.item

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chloe.buytogether.NavigationDirections
import com.chloe.buytogether.R
import com.chloe.buytogether.data.Product
import com.chloe.buytogether.databinding.FragmentHomePageBinding
import com.chloe.buytogether.detail.OptionSelector
import com.chloe.buytogether.ext.getVmFactory
import com.chloe.buytogether.home.HomeAdapter
import com.google.android.material.tabs.TabLayout


class HomePageFragment : Fragment() {


    private val viewModel by viewModels<HomePageViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomePageBinding.inflate(inflater,container,false)

        val adapter1st = HomeHots1stAdapter(viewModel)

        val adapter2nd = HomeHots2ndAdapter(viewModel)

        val gridAdapter = HomeGridAdapter(viewModel)

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(
                    NavigationDirections.navigateToDetailFragment(it)
                )
            }
        })


        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.recyclerHots1st.adapter = adapter1st
        binding.recyclerHots2nd.adapter = adapter2nd
        binding.recyclerNew.adapter = gridAdapter

        viewModel.addMockData()

        return binding.root
    }

}
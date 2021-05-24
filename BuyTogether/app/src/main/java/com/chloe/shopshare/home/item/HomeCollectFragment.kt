package com.chloe.shopshare.home.item

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.R
import com.chloe.shopshare.databinding.FragmentHomeCollectBinding
import com.chloe.shopshare.databinding.FragmentHomePageBinding
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.home.HomeType


class HomeCollectFragment(private val collectType: HomeType) : Fragment() {

    private val viewModel by viewModels<HomeCollectViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeCollectBinding.inflate(inflater,container,false)
        val adapter = HomeCollectAdapter(viewModel)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.recyclerCollection.adapter = adapter

        viewModel.addMockData(collectType)

        binding.spinnerHome.adapter = HomeSpinnerAdapter(
                MyApplication.instance.resources.getStringArray(R.array.sort_method_list))

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(
                    NavigationDirections.navigateToDetailFragment(it)
                )
            }
        })


        return binding.root
    }

}
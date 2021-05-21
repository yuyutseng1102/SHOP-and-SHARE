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
import com.chloe.buytogether.MyApplication
import com.chloe.buytogether.NavigationDirections
import com.chloe.buytogether.R
import com.chloe.buytogether.databinding.FragmentHomeCollectBinding
import com.chloe.buytogether.databinding.FragmentHomePageBinding
import com.chloe.buytogether.ext.getVmFactory
import com.chloe.buytogether.home.HomeType


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
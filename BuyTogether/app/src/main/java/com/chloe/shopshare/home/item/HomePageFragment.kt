package com.chloe.shopshare.home.item

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.databinding.FragmentHomePageBinding
import com.chloe.shopshare.ext.getVmFactory


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
//                findNavController().navigate(
//                    NavigationDirections.navigateToDetailFragment(it)
//                )
                viewModel.onDetailNavigated()
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
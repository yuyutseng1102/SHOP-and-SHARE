package com.chloe.shopshare.home.item

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.chloe.shopshare.databinding.FragmentHomeMainBinding
import com.chloe.shopshare.ext.getVmFactory


class HomeMainFragment : Fragment() {


    private val viewModel by viewModels<HomeMainViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeMainBinding.inflate(inflater,container,false)

        val adapter1st = HomeMainLinearAdapter(viewModel)

        val adapter2nd = HomeHots2ndAdapter(viewModel)

        val gridAdapter = HomeMainGridAdapter(viewModel)

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
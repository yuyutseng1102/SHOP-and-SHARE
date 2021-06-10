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
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.R
import com.chloe.shopshare.databinding.FragmentHomeHostBinding
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.home.HomeType


class HomeHostFragment() : Fragment() {

    private val viewModel by viewModels<HomeHostViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeHostBinding.inflate(inflater,container,false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val adapter = HomeHostingAdapter(viewModel)

        binding.recyclerCollection.adapter = adapter

        binding.isLiveDataDesign = MyApplication.instance.isLiveDataDesign()
        binding.layoutSwipeRefreshCollectionItem.setOnRefreshListener {
            viewModel.refresh()
            Log.d("Chloe", "home status = ${viewModel.status.value}")
        }

        viewModel.refreshStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.layoutSwipeRefreshCollectionItem.isRefreshing = it
            }
        })

        viewModel.refreshProfile.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.refreshProfile()
            }
        })

        viewModel.shop.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("LikeTag","get shop ready")
                viewModel.getLikeList()
            }
        })

        viewModel.successGetLikeList.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("LikeTag","successGetLikeList observe = ${viewModel.successGetLikeList.value}")
                viewModel.shop.value?.let {
                        Log.d("LikeTag","submitList shop to home recycler")
                        adapter.submitList(it)
                    }
                viewModel.onLikeListGet()
            }
        })

        binding.checkBoxHostFilter.setOnCheckedChangeListener { _, isChecked ->
            viewModel.displayOpeningShop.value = isChecked
        }

        viewModel.displayOpeningShop.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.getShopList()
            }
        })




        binding.spinnerHome.adapter = HomeSpinnerAdapter(
                MyApplication.instance.resources.getStringArray(R.array.sort_method_list))

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(
                    NavigationDirections.navigateToDetailFragment(it)
                )
                viewModel.onDetailNavigated()
            }
        })


        return binding.root
    }

}
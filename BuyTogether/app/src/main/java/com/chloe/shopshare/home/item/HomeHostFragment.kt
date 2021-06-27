package com.chloe.shopshare.home.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.R
import com.chloe.shopshare.databinding.FragmentHomeHostBinding
import com.chloe.shopshare.ext.getVmFactory


class HomeHostFragment : Fragment() {

    private val viewModel by viewModels<HomeHostViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentHomeHostBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = HomeHostingAdapter(viewModel)
        binding.recyclerCollection.adapter = adapter

        binding.layoutSwipeRefreshCollectionItem.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.refreshStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.layoutSwipeRefreshCollectionItem.isRefreshing = it
            }
        })

        viewModel.shops.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.getLikes()
            }
        })

        viewModel.getLikesDone.observe(viewLifecycleOwner, Observer {
            it?.let {

                viewModel.shops.value?.let { shop ->
                    adapter.submitList(shop)
                }
                viewModel.onLikesGetDone()
            }
        })

        viewModel.refreshProfile.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.refreshProfile()
            }
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(
                    NavigationDirections.navigateToDetailFragment(it)
                )
                viewModel.onDetailNavigated()
            }
        })

        binding.checkBoxHostFilter.setOnCheckedChangeListener { _, isChecked ->
            viewModel.filterOpeningShops.value = isChecked
        }

        viewModel.filterOpeningShops.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.getShops()
            }
        })

        binding.spinnerHome.adapter = HomeSpinnerAdapter(
            MyApplication.instance.resources.getStringArray(R.array.sort_method_list)
        )

        return binding.root
    }

}
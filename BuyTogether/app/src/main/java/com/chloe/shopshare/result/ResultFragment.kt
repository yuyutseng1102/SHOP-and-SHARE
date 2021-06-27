package com.chloe.shopshare.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.databinding.FragmentResultBinding
import com.chloe.shopshare.ext.getVmFactory

class ResultFragment : Fragment() {

    private val args: ResultFragmentArgs by navArgs()

    private val viewModel by viewModels<ResultViewModel> {
        getVmFactory(args.categoryKey, args.countryKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentResultBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val adapter = ResultAdapter(viewModel)

        binding.recyclerResult.adapter = adapter

        binding.layoutSwipeRefreshResult.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.refreshStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.layoutSwipeRefreshResult.isRefreshing = it
            }
        })

        viewModel.refreshProfile.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.refreshProfile()
            }
        })

        viewModel.shop.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.getLikeList()
            }
        })

        viewModel.successGetLikeList.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.shop.value?.let { shop -> adapter.submitList(shop) }
                viewModel.onLikeListGet()
            }
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToDetailFragment(it))
                viewModel.onDetailNavigated()
            }
        })

        return binding.root
    }

}
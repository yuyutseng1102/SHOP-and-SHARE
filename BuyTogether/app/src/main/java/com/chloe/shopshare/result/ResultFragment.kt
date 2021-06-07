package com.chloe.shopshare.result

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.R
import com.chloe.shopshare.databinding.FragmentHomeHostBinding
import com.chloe.shopshare.databinding.FragmentResultBinding
import com.chloe.shopshare.detail.DetailFragmentArgs
import com.chloe.shopshare.detail.DetailViewModel
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.home.item.HomeHostingAdapter
import com.chloe.shopshare.home.item.HomeSpinnerAdapter

class ResultFragment : Fragment() {

    private val args: ResultFragmentArgs by navArgs()

    private val viewModel by viewModels<ResultViewModel> { getVmFactory(args.categoryKey,args.countryKey) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentResultBinding.inflate(inflater,container,false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val adapter = ResultAdapter(viewModel)

        binding.recyclerResult.adapter = adapter


        binding.layoutSwipeRefreshResult.setOnRefreshListener {
            viewModel.refresh()
            Log.d("Chloe", "home status = ${viewModel.status.value}")
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

//        binding.checkBoxHostFilter.setOnCheckedChangeListener { _, isChecked ->
//            viewModel.displayOpeningShop.value = isChecked
//        }
//
//        viewModel.displayOpeningShop.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                viewModel.getShopList()
//            }
//        })
//

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
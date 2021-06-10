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
import com.chloe.shopshare.databinding.FragmentHomeRequestBinding
import com.chloe.shopshare.ext.getVmFactory


class HomeRequestFragment : Fragment() {

    private val viewModel by viewModels<HomeRequestViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeRequestBinding.inflate(inflater,container,false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.recyclerRequest.adapter = HomeRequestingAdapter(viewModel)

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


        binding.spinnerHome.adapter = HomeSpinnerAdapter(
            MyApplication.instance.resources.getStringArray(R.array.sort_method_list))

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(
                    NavigationDirections.navigateToRequestDetailFragment(it)
                )
                viewModel.onDetailNavigated()
            }
        })


        return binding.root
    }

}
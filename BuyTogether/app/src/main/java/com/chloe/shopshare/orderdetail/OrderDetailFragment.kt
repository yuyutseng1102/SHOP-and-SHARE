package com.chloe.shopshare.orderdetail

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
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.R
import com.chloe.shopshare.databinding.FragmentManageBinding
import com.chloe.shopshare.databinding.FragmentOrderDetailBinding
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.manage.ManageFragmentArgs
import com.chloe.shopshare.manage.ManageViewModel
import com.chloe.shopshare.manage.MemberAdapter

class OrderDetailFragment : Fragment() {

    private val args: OrderDetailFragmentArgs by navArgs()

    private val viewModel by viewModels<OrderDetailViewModel> { getVmFactory(args.myOrderDetailKey) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentOrderDetailBinding.inflate(inflater,container,false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = OrderDetailProductAdapter()
        binding.recyclerProduct.adapter = adapter

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToDetailFragment(it))
                viewModel.onDetailNavigate()
            }
        })


        return binding.root
    }

}
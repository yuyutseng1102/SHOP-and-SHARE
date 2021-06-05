package com.chloe.shopshare.myorder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chloe.shopshare.R
import com.chloe.shopshare.databinding.FragmentMyOrderBinding
import com.chloe.shopshare.databinding.FragmentMyRequestBinding
import com.chloe.shopshare.myrequest.MyRequestAdapter
import com.google.android.material.tabs.TabLayout


class MyOrderFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMyOrderBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewpagerMyOrder.let{
            binding.tabsMyOrder.setupWithViewPager(it)
            it.adapter = MyOrderAdapter(childFragmentManager)
            it.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabsMyOrder))
        }

        return binding.root
    }

}
package com.chloe.shopshare.myrequest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chloe.shopshare.databinding.FragmentMyRequestBinding
import com.google.android.material.tabs.TabLayout


class MyRequestFragment() : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMyRequestBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewpagerMyRequest.let{
            binding.tabsMyRequest.setupWithViewPager(it)
            it.adapter = MyRequestAdapter(childFragmentManager)
            it.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabsMyRequest))
        }

        return binding.root
    }

}
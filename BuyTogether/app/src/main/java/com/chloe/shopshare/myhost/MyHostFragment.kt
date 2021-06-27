package com.chloe.shopshare.myhost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chloe.shopshare.databinding.FragmentMyHostBinding
import com.google.android.material.tabs.TabLayout


class MyHostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMyHostBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewpagerMyHost.let {
            binding.tabsMyHost.setupWithViewPager(it)
            it.adapter = MyHostAdapter(childFragmentManager)
            it.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabsMyHost))
        }

        return binding.root
    }
}
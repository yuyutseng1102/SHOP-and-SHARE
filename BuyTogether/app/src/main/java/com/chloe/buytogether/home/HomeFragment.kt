package com.chloe.buytogether.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chloe.buytogether.NavigationDirections
import com.chloe.buytogether.R
import com.chloe.buytogether.databinding.FragmentHomeBinding
import com.chloe.buytogether.home.item.HomePageViewModel
import com.google.android.material.tabs.TabLayout

class HomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewpagerHome.let{
            binding.tabsHome.setupWithViewPager(it)
            it.adapter = HomeAdapter(childFragmentManager)
            it.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabsHome))
        }

        binding.floatingCollect.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToHostFragment())
        }



        return binding.root
    }

}
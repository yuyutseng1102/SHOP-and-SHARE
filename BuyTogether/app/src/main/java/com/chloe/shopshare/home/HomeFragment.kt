package com.chloe.shopshare.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.R
import com.chloe.shopshare.databinding.FragmentHomeBinding
import com.google.android.material.internal.NavigationMenu
import com.google.android.material.tabs.TabLayout
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter


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

        binding.floatingActionButton.setMenuListener(object : SimpleMenuListenerAdapter() {

            override fun onPrepareMenu(navigationMenu: NavigationMenu?): Boolean {
                return true
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.navigate_to_host -> findNavController().navigate(NavigationDirections.navigateToHostFragment(null))
                    R.id.navigate_to_request -> findNavController().navigate(NavigationDirections.navigateToRequestFragment())
                }
                return true
            }
        })


        return binding.root
    }

}
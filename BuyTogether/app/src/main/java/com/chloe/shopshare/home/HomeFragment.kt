package com.chloe.shopshare.home

import android.graphics.Color
import android.os.Bundle
import android.transition.Slide
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.R
import com.chloe.shopshare.databinding.FragmentHomeBinding
import com.google.android.material.internal.NavigationMenu
import com.google.android.material.tabs.TabLayout
import com.google.android.material.transition.MaterialContainerTransform
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter
import kotlinx.android.synthetic.main.activity_main.*


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

//        enterTransition = MaterialContainerTransform().apply {
//            startView = requireActivity().findViewById(R.id.floatingActionButton)
//            endView = requireActivity().findViewById(R.id.floatingActionButton)
//            duration = 1000
//            scrimColor = Color.TRANSPARENT
//            containerColor = Color.GREEN
//            startContainerColor = Color.GREEN
//            endContainerColor = Color.GREEN
//        }
//        returnTransition = Slide().apply {
//            duration = 1000
//            addTarget(R.id.hostFragment)
//        }


        return binding.root
    }

}
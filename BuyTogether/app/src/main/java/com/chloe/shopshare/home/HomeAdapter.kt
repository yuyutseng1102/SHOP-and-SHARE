package com.chloe.shopshare.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.chloe.shopshare.home.item.HomeHostFragment
import com.chloe.shopshare.home.item.HomeMainFragment
import com.chloe.shopshare.home.item.HomeRequestFragment

class HomeAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> HomeMainFragment()
            1 -> HomeHostFragment()
            2 -> HomeRequestFragment()
            else -> HomeHostFragment()
        }

        }


    override fun getCount() = HomeType.values().size


    override fun getPageTitle(position: Int): CharSequence? {
        return HomeType.values()[position].value
    }
}

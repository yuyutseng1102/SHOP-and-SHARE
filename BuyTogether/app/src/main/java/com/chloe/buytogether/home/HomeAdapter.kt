package com.chloe.buytogether.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.chloe.buytogether.home.item.HomeCollectFragment
import com.chloe.buytogether.home.item.HomePageFragment

class HomeAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> HomePageFragment()
            1 -> HomeCollectFragment(HomeType.values()[position])
            else -> HomeCollectFragment(HomeType.values()[position])
        }

        }


    override fun getCount() = HomeType.values().size


    override fun getPageTitle(position: Int): CharSequence? {
        return HomeType.values()[position].value
    }
}

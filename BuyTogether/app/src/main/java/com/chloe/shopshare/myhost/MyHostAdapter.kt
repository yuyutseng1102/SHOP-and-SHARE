package com.chloe.shopshare.myhost

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.chloe.shopshare.myhost.item.MyHostListFragment

class MyHostAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return MyHostListFragment(MyHostType.values()[position])
    }

    override fun getCount() = MyHostType.values().size

    override fun getPageTitle(position: Int): CharSequence? {
        return MyHostType.values()[position].title
    }
}

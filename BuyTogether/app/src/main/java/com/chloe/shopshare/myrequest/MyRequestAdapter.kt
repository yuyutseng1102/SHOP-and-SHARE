package com.chloe.shopshare.myrequest

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.chloe.shopshare.myrequest.item.MyRequestListFragment

class MyRequestAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return MyRequestListFragment(MyRequestType.values()[position])
    }

    override fun getCount() = MyRequestType.values().size

    override fun getPageTitle(position: Int): CharSequence? {
        return MyRequestType.values()[position].title
    }
}

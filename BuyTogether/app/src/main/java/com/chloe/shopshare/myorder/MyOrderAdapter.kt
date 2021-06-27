package com.chloe.shopshare.myorder

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.chloe.shopshare.myorder.item.MyOrderListFragment

class MyOrderAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return MyOrderListFragment(MyOrderType.values()[position])
    }

    override fun getCount() = MyOrderType.values().size

    override fun getPageTitle(position: Int): CharSequence? {
        return MyOrderType.values()[position].title
    }
}

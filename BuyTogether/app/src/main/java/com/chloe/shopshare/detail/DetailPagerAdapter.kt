package com.chloe.shopshare.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.chloe.shopshare.detail.item.DetailBoardFragment
import com.chloe.shopshare.detail.item.DetailDescriptionFragment

class DetailPagerAdapter(fragmentManager: FragmentManager, val description: String) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> DetailDescriptionFragment(description)
            1 -> DetailBoardFragment()
            else -> DetailDescriptionFragment(description)
        }
    }

    override fun getCount() = DetailContentType.values().size

    override fun getPageTitle(position: Int): CharSequence? {
        return DetailContentType.values()[position].title
    }
}
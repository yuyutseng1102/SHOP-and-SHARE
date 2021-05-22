package com.chloe.buytogether.util

import com.chloe.buytogether.R
import com.chloe.buytogether.util.Util.getString

enum class CurrentFragmentType(val value: String) {
    HOME(getString(R.string.app_name)),
    PROFILE(getString(R.string.profile)),
    GATHER(getString(R.string.gather)),
    COLLECTION(getString(R.string.collection)),
    COLLECTION_MANAGE(getString(R.string.collection_manage)),
    DETAIL("")
}
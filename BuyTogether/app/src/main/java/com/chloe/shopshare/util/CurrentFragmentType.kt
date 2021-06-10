package com.chloe.shopshare.util

import com.chloe.shopshare.R
import com.chloe.shopshare.util.Util.getString

enum class CurrentFragmentType(val value: String) {
    HOME(getString(R.string.app_name)),
    PROFILE(getString(R.string.profile)),
    HOST(getString(R.string.gather)),
    SHOP(getString(R.string.collection)),
    MANAGE(getString(R.string.collection_manage)),
    DETAIL(""),
    PARTICIPATE(getString(R.string.participate)),
    NOTIFY(getString(R.string.notify)),
    LOGIN("")
}
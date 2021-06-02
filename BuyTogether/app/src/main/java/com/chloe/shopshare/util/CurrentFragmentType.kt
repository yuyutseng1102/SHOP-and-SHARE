package com.chloe.shopshare.util

import android.graphics.Color
import com.chloe.shopshare.R
import com.chloe.shopshare.util.Util.getString

enum class CurrentFragmentType(val value: String) {
    HOME(""),
    PROFILE(getString(R.string.profile)),
    HOST(getString(R.string.gather)),
    SHOP(getString(R.string.collection)),
    MANAGE(getString(R.string.collection_manage)),
    DETAIL(""),
    PARTICIPATE(getString(R.string.participate)),
    NOTIFY(getString(R.string.notify)),
    REQUEST(getString(R.string.request_title)),
    REQUEST_DETAIL(""),
    LOGIN("")
}
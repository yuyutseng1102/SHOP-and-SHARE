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
    LIKE(getString(R.string.like)),
    MY_ORDER(getString(R.string.my_order)),
    ORDER_DETAIL("訂單詳情"),
    MY_REQUEST(getString(R.string.my_request)),
    SEARCH("選擇分類"),
    CHAT("聊天列表"),
    CHAT_ROOM(""),
    RESULT("搜尋結果"),
    LOGIN("")
}
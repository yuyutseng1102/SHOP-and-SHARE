package com.chloe.shopshare.myrequest

import com.chloe.shopshare.R
import com.chloe.shopshare.util.Util

enum class MyRequestType(val position: Int, val title: String) {
    ALL_REQUEST(0, Util.getString(R.string.all_request)),
    ONGOING_REQUEST(1, Util.getString(R.string.ongoing_request)),
    FINISHED_REQUEST(2, Util.getString(R.string.finished_request))
}
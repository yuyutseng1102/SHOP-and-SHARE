package com.chloe.shopshare.detail

import com.chloe.shopshare.R
import com.chloe.shopshare.util.Util.getString

enum class DetailContentType(val title: String) {
    DESCRIPTION(getString(R.string.detail_description)),
    BOARD(getString(R.string.Message_board))
}
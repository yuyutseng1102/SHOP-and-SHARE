package com.chloe.buytogether.detail

import androidx.fragment.app.Fragment
import com.chloe.buytogether.R
import com.chloe.buytogether.detail.item.DetailDescriptionFragment
import com.chloe.buytogether.util.Util.getString

enum class DetailType(val title: String) {
    DESCRIPTION(getString(R.string.detail_description)),
    BOARD(getString(R.string.Message_board))
}
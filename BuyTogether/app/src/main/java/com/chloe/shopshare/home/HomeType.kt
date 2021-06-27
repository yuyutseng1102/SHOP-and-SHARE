package com.chloe.shopshare.home

enum class HomeType (val position: Int, val value: String) {
    MAIN(0, "首頁"),
    HOST(1, "開團中"),
    REQUEST(2, "徵團中")
}

enum class SortMethod(val positionOnSpinner: Int) {
    BY_ID(0),
    BY_TIME(1),
    BY_HOTS(2)
}
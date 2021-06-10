package com.chloe.buytogether.home

enum class HomeType (val value: String) {
    HOME("首頁"),
    COLLECTIVE("開團中"),
    SEEK("徵團中")
}

enum class SortMethod(val positionOnSpinner: Int) {
    BY_ID(0),
    BY_TIME(1),
    BY_HOTS(2)
}
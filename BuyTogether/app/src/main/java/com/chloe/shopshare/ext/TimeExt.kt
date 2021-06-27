package com.chloe.shopshare.ext

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import java.util.*

const val secondsInMilli = 1000L
const val minutesInMilli = secondsInMilli * 60
const val hoursInMilli = minutesInMilli * 60
const val daysInMilli = hoursInMilli * 24
const val mothsInMilli = daysInMilli * 30
const val yearInMilli = mothsInMilli * 12

fun Long.toDisplayDateTimeFormat(): String {
    return SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.TAIWAN).format(this)
}

fun Long.toDisplayYearDateFormat(): String {
    return SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN).format(this)
}

fun Long.toDisplayMonthDateFormat(): String {
    return SimpleDateFormat("MM/dd", Locale.TAIWAN).format(this)
}

fun Long.toDisplayMonthDateTimeFormat(): String {
    return SimpleDateFormat("MM/dd HH:mm", Locale.TAIWAN).format(this)
}

fun Long.toDisplayTimeFormat(): String {
    return SimpleDateFormat("HH:mm", Locale.TAIWAN).format(this)
}

fun Long.toDisplayDateWeekFormat(): String {
    val date = this.toDisplayYearDateFormat()
    val week = this.toDisplayWeekDayFormat()
    return "$date $week"
}


@SuppressLint("SimpleDateFormat")
fun Long.getTimeGap(): Long {
    val dayFormat = SimpleDateFormat("yyyy-MM-dd")
    val todayDate = dayFormat.format(Date(System.currentTimeMillis()))
    val targetDate = dayFormat.format(Date(this))
    val today = dayFormat.parse(todayDate).time
    val target = dayFormat.parse(targetDate).time
    return today - target
}

fun Long.isShopExpiredToday(): Boolean {
    return when (this.getTimeGap()) {
        0L -> true
        else -> false
    }
}

fun Long.toDisplayTimeGapWithoutWeek(): String {
    return when (this.getTimeGap()) {
        0L -> this.toDisplayTimeFormat()
        daysInMilli -> "昨天 ${this.toDisplayTimeFormat()}"
        in daysInMilli * 2 until yearInMilli -> this.toDisplayMonthDateTimeFormat()
        else -> this.toDisplayDateTimeFormat()
    }
}

fun Long.toDisplayTimeGap(): String {
    return when (this.getTimeGap()) {
        0L -> this.toDisplayTimeFormat()
        daysInMilli -> "昨天"
        in daysInMilli * 2 until daysInMilli * 7 -> this.toDisplayWeekDayFormat()
        in daysInMilli * 7 until yearInMilli -> this.toDisplayMonthDateFormat()
        else -> this.toDisplayYearDateFormat()
    }
}

fun Long.toDisplayWeekDayFormat(): String {
    val calendar = Calendar.getInstance()
    calendar.time = Date(this)
    return when (calendar.get(Calendar.DAY_OF_WEEK)) {
        Calendar.MONDAY -> "星期一"
        Calendar.TUESDAY -> "星期二"
        Calendar.WEDNESDAY -> "星期三"
        Calendar.THURSDAY -> "星期四"
        Calendar.FRIDAY -> "星期五"
        Calendar.SATURDAY -> "星期六"
        Calendar.SUNDAY -> "星期日"
        else -> ""
    }
}




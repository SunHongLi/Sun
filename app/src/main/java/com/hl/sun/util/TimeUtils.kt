package com.hl.sun.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Function:时间相关工具类
 * Date:2023/2/8
 * Author: sunHL
 */
object TimeUtils {
    /**
     * 通过时间戳获取是周几
     * @param time 毫秒时间戳
     */
    fun getWeek(time: Long): String? {
        val calendar = Calendar.getInstance()
        val date = Date(time)
        calendar.time = date
        return numberToWeek(calendar.get(Calendar.DAY_OF_WEEK))
    }

    fun numberToWeek(week: Int): String? {
        val weekString: String = when (week) {
            Calendar.SUNDAY -> "周日"
            Calendar.MONDAY -> "周一"
            Calendar.TUESDAY -> "周二"
            Calendar.WEDNESDAY -> "周三"
            Calendar.THURSDAY -> "周四"
            Calendar.FRIDAY -> "周五"
            else -> "周六"
        }
        return weekString
    }


    /**
     * @param timestamp 毫秒时间戳
     */
    fun formatTimeStamp(timestamp: Long, pattern: String): String? {
        //时间格式,HH是24小时制，hh是AM PM12小时制
        val sdf = SimpleDateFormat(pattern)
        return sdf.format(Date(timestamp))
    }
}
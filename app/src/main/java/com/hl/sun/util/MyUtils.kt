package com.hl.sun.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Function:
 * Date:2022/4/17
 * Author: sunHL
 */
class MyUtils {
    companion object {
        /**
         * 获取明天的日期字符串
         * https://www.cnblogs.com/pxblog/p/13182654.html
         * @return  2000-01-01（有补0）
         */
        @JvmStatic
        fun tomorrowDateStr(): String? {
            var date = Date() //取时间
            val calendar: Calendar =
                Calendar.getInstance()
            calendar.setTime(date)
            //把日期往后增加一天.整数往后推,负数往前移动(1:表示明天、-1：表示昨天，0：表示今天)
            calendar.add(Calendar.DATE, 1)

            //这个时间就是日期往后推一天的结果
            date = calendar.time
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            return formatter.format(date)
        }
    }
}
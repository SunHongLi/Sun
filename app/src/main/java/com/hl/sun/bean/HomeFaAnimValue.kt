package com.hl.sun.bean

import android.view.View
import com.hl.sun.util.ScreenUtils

/**
 * Function:fa滚动动效 值存储
 * Date:2022/4/17
 * Author: sunHL
 */
class HomeFaAnimValue(var iv: View) {
    //滚出屏幕需要的时长
    val scrollDuration: Long = 2000L

    //fa宽度
    val ivWidth = iv.width

    //滚出屏幕需要滚多少距离
//    val scrollOutLength = (ScreenUtils.getScreenWidth() + iv.width + 2) / 2.0F  //刚好滚出屏幕的滚动距离
    val scrollOutLength = (ScreenUtils.getScreenWidth() + iv.width + 2) / 1.3F

    //滚进屏幕需要滚多少距离
    val scrollInLength = ScreenUtils.getScreenWidth() / 1.5F + iv.width / 2.0F
}
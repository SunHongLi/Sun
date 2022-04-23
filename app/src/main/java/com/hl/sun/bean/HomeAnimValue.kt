package com.hl.sun.bean

import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar

/**
 * Function:进度条相关动效 值存储
 * Date:2022/4/17
 * Author: sunHL
 */
class HomeAnimValue(
    var iv_icon: View,
    var iv_icon_2: View,
    var pb: ProgressBar,
    var pb_2: View
) {
    //放大动效时长
    var scaleEnargeDuration = 800L

    //缩小动效时长
    var scaleShrinkDuration = 800L

    //进度条动效时长
    var progressAnimDuration = 1000L

    ///图标
    val params = iv_icon.layoutParams
    val iconParams =
        params as? ViewGroup.MarginLayoutParams ?: ViewGroup.MarginLayoutParams(params)

    //宽高
    val iconWidth = iv_icon.width
    val iconWidthDiff = iv_icon_2.width - iconWidth

    //左间距
    val iconLeft = iv_icon.left
    val iconLeftDiffer = iv_icon_2.left - iconLeft

    //上间距
    val iconTop = iv_icon.top
    val iconTopDiffer = iv_icon_2.top - iconTop

    ///进度条
    val pbParams = pb.layoutParams
    val pbMParams =
        pbParams as? ViewGroup.MarginLayoutParams ?: ViewGroup.MarginLayoutParams(pbParams)

    //宽
    val pbWidth = pb.width
    val pbWidthDiffer = pb_2.width - pbWidth

    //高
    val pbHeight = pb.height
    val pbHeightDiffer = pb_2.height - pbHeight

    //左间距
    val pbLeft = pb.left
    val pbLeftDiffer = pb_2.left - pbLeft

    //上间距
    val pbTop = pb.top
    val pbTopDiffer = pb_2.top - pbTop

    //进度条初始值
    var progressFrom: Int = 0

    //进度条end值
    var progressTo: Int = 0
}
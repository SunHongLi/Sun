package com.hl.sun.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.Scroller

/**
 * Function:
 * Date:2021/9/3
 * Author: sunHL
 */
class ScrollerImageView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImageView(context, attributeSet, defStyleAttr) {
    private var scroller: Scroller = Scroller(context)

    fun smoothScrollTo(fx: Int, fy: Int) {
        val dx: Int = fx - scroller.finalX
        val dy: Int = fy - scroller.finalY
        smoothScrollBy(dx, dy)
    }

    //调用此方法设置滚动的相对偏移
    fun smoothScrollBy(dx: Int, dy: Int) {
        //设置mScroller的滚动偏移量
        scroller.startScroll(scroller.finalX, scroller.finalY, dx, dy)
        invalidate() //这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY);
            postInvalidate();
        }
        super.computeScroll()
    }
}
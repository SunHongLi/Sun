package com.hl.sun.ui.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.LinearLayout

/**
 * 四角都为圆角的LinearLayout
 * 裁切子控件
 */
class RoundLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mBgRect: RectF = RectF()
    private var viewWidth = 0
    private var viewHeight = 0
    private var halfH = 0F

    //圆角矩形路径
    private var circleRectPath = Path()

    init {
        mPaint.color = Color.BLUE
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = 10f

        //不设置setWillNotDraw=false,viewGroup不执行onDraw()
        setWillNotDraw(false)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
        halfH = viewHeight / 2F
        mBgRect.set(0f, 0f, w * 1f, h * 1f)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //圆角矩形path
        circleRectPath.addRoundRect(
            mBgRect,
            floatArrayOf(
                halfH, halfH,
                halfH, halfH,
                halfH, halfH,
                halfH, halfH
            ),
            Path.Direction.CCW
        )
        //裁切画布
        canvas?.clipPath(circleRectPath)
    }
}
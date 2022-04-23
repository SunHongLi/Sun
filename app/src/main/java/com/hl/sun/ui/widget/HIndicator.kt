package com.hl.sun.ui.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.hl.sun.R

/**
 * 横向滑动的指示器
 * 做了基于recyclerView的联动[bindRecyclerView]，其余的需要自己适配
 *
 * 设置指示器颜色：
 * hIndicator.setIndicatorColor(Color.parseColor("#ffffff"))
 *
 * 设置指示器背景颜色：
 * hIndicator.setBgColor(Color.parseColor("#333333"))
 *
 * @author https://github.com/leiyun1993/HIndicator.git
 */
class HIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mBgPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mBgRect: RectF = RectF()
    private var mRadius: Float = 0f
    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mRect: RectF = RectF()
    private var viewWidth: Int = 0
    private var mBgColor = Color.parseColor("#e5e5e5")
    private var mIndicatorColor = Color.parseColor("#ff4646")
    var ratio = 0.5f        //长度比例
        set(value) {
            field = value
            invalidate()
        }
    var progress: Float = 0f    //滑动进度比例
        set(value) {
            field = value
            invalidate()
        }

    init {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.HIndicator)
        mBgColor = typedArray.getColor(R.styleable.HIndicator_hi_bgColor, mBgColor)
        mIndicatorColor =
            typedArray.getColor(R.styleable.HIndicator_hi_indicatorColor, mIndicatorColor)
        typedArray.recycle()

        mBgPaint.color = mBgColor
        mBgPaint.style = Paint.Style.FILL
        mPaint.color = mIndicatorColor
        mPaint.style = Paint.Style.FILL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        mBgRect.set(0f, 0f, w * 1f, h * 1f)
        mRadius = h / 2f
    }

    /**
     * 设置指示器背景进度条的颜色
     * @param color 背景色
     */
    fun setBgColor(@ColorInt color: Int) {
        mBgPaint.color = color
        invalidate()
    }

    /**
     * 设置指示器的颜色
     * @param color 指示器颜色
     */
    fun setIndicatorColor(@ColorInt color: Int) {
        mPaint.color = color
        invalidate()
    }

    /**
     * 绑定recyclerView
     */
    fun bindRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //已经滚动的距离，为0时表示已处于顶部。
                val offsetX = recyclerView.computeHorizontalScrollOffset()
                //整体的高度，注意是整体，包括在显示区域之外的。
                val range = recyclerView.computeHorizontalScrollRange()
                //整体的高度，注意是整体，包括在显示区域之外的。
                val extend = recyclerView.computeHorizontalScrollExtent()
                val progress: Float = offsetX * 1.0f / (range - extend)
                this@HIndicator.progress = progress     //设置滚动距离所占比例
            }
        })

        recyclerView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            val range = recyclerView.computeHorizontalScrollRange()
            val extend = recyclerView.computeHorizontalScrollExtent()
            val ratio = extend * 1f / range
            this@HIndicator.ratio = ratio       //设置指示器所占的长度比例
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //绘制背景
        canvas?.drawRoundRect(mBgRect, mRadius, mRadius, mBgPaint)

        //计算指示器的长度和位置
        val leftOffset = viewWidth * (1f - ratio) * progress
        val left = mBgRect.left + leftOffset
        val right = left + viewWidth * ratio
        mRect.set(left, mBgRect.top, right, mBgRect.bottom)

        //绘制指示器
        canvas?.drawRoundRect(mRect, mRadius, mRadius, mPaint)
    }

}
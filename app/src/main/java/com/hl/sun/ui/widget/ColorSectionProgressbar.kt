package com.hl.sun.ui.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.hl.sun.R


/**
 * 不同颜色分段进度条
 * 绘制居中文字  参考https://cloud.tencent.com/developer/article/1735788
 */
class ColorSectionProgressbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val colorList = listOf(
        Color.parseColor("#4C4C4C"),
        Color.parseColor("#86C2B8"),
        Color.parseColor("#AFDCD6"),
        Color.parseColor("#AFCAD9"),
        Color.parseColor("#C9DCA8")
    )

    //颜色块个数由percentList的size决定
    private var percentList = mutableListOf(0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.2F, 0.2F)
    private var txtList = mutableListOf("10", "11", "12", "13", "14", "15", "16")
    private val mBgRect: RectF = RectF()
    private var mRadius: Float = 0f

    //设置无锯齿 也可以使用setAntiAlias(true)
    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTxtPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mRect: RectF = RectF()
    private var viewWidth: Int = 0
    private var viewHeight: Int = 0

    //文字中心y坐标
    private var centerY: Float = 0F
    private var mPaintColor = colorList[0]

    init {
        initTxtPaint()
        mPaint.style = Paint.Style.FILL
        mPaint.color = mPaintColor
        mPaint.strokeWidth = 2F
    }

    private var baseLineY = 0F
    private fun initTxtPaint() {
        //FILL表示颜色填充整个；STROKE表示空心
        mTxtPaint.style = Paint.Style.FILL
        mTxtPaint.color = Color.WHITE
        //设置字体加粗
        mTxtPaint.typeface = Typeface.DEFAULT_BOLD
        mTxtPaint.textSize = context.resources.getDimension(R.dimen.dp_10)
        //该方法即为设置基线上那个点究竟是left,center,还是right 这里我设置为center
        mTxtPaint.textAlign = Paint.Align.CENTER
        //baseLineY：基线中间点的y轴计算公式  onSizeChange()里计算，因为和控件的高度相关
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mBgRect.set(0f, 0f, w * 1f, h * 1f)
        viewHeight = h
        viewWidth = w
        mRadius = h / 2f

        //计算文字基线Y坐标baseLineY
        centerY = h / 2f
        val fontMetrics = mTxtPaint.fontMetrics
        val top = fontMetrics.top //为基线到字体上边框的距离,即上图中的top
        val bottom = fontMetrics.bottom //为基线到字体下边框的距离,即上图中的bottom
        baseLineY = centerY - top / 2 - bottom / 2 //基线中间点的y轴计算公式
    }

    var lastLeft = 0F

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        lastLeft = 0F

        for (i in percentList.indices) {
            val ratio = percentList[i]
            //进度<=0不绘制
            if (ratio == 0F) continue

            //计算每个色块长度和位置
            val left = lastLeft
            val right = left + viewWidth * ratio
            mRect.set(left, mBgRect.top, right, mBgRect.bottom)
            mPaint.color = colorList[i % colorList.size]
            //绘制圆角矩形
            //drawRoundRectcanvas?.drawRoundRect(mRect, mRadius, mRadius, mPaint)
            when (i) {
                0 -> {
                    //绘制左边圆角
                    drawLeftCircle(canvas, mRect)
                }
                percentList.size - 1 -> {
                    //绘制右边圆角
                    drawRightCircle(canvas, mRect)
                }
                else -> {
                    //绘制矩形
                    canvas?.drawRect(mRect, mPaint)
                }
            }
            //绘制文字
            val txt = txtList[i % txtList.size]
            drawCenterTxt(txt, canvas, (left + right) / 2)

            lastLeft = right
        }
    }

    //两个圆角的矩形路径
    var circleRectPath = Path()

    //画一整个圆要用的矩形
    var circleRectF = RectF()

    /**
     * 绘制 两个圆角的矩形-左边
     * https://blog.csdn.net/u013700502/article/details/114494273
     */
    private fun drawLeftCircle(canvas: Canvas?, rect: RectF) {
        circleRectPath.reset()
//        canvas?.translate(rect.left,rect.top)
//        canvas?.drawRect(rect, paint)//测试用
        //圆中心X坐标
        val circleCentX = viewHeight / 2F

        //float[] radii中有8个值，依次为左上角，右上角，右下角，左下角的rx,ry
        circleRectPath.addRoundRect(
            rect,
            floatArrayOf(
                circleCentX, circleCentX,
                0f, 0f,
                0f, 0f,
                circleCentX, circleCentX
            ),
            Path.Direction.CCW
        )
        canvas?.drawPath(circleRectPath, mPaint)
    }

    /**
     * 绘制 两个圆角的矩形-右边
     */
    private fun drawRightCircle(canvas: Canvas?, rect: RectF) {
        circleRectPath.reset()
        //圆中心X坐标
        val circleCentX = viewHeight / 2F

        //float[] radii中有8个值，依次为左上角，右上角，右下角，左下角的rx,ry
        circleRectPath.addRoundRect(
            rect,
            floatArrayOf(
                0f, 0f,
                circleCentX, circleCentX,
                circleCentX, circleCentX,
                0f, 0f
            ),
            Path.Direction.CCW
        )
        canvas?.drawPath(circleRectPath, mPaint)
    }

    /**
     * @param centerX 文字中心x坐标
     */
    private fun drawCenterTxt(txt: String, canvas: Canvas?, centerX: Float) {
        canvas?.drawText(txt, centerX, baseLineY, mTxtPaint)
    }

    fun refreshPercentAndTxt(pList: List<Float>, tList: List<String>) {
        if (percentList.isNotEmpty()) {
            percentList.clear()
        }
        percentList.addAll(pList)

        if (txtList.isNotEmpty()) {
            txtList.clear()
        }
        txtList.addAll(tList)
        invalidate()
    }
}
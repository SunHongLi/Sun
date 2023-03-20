package com.hl.sun.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.hl.sun.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * 参考：
 * https://github.com/lixiaote/UnderLineTextView
 * https://zhuanlan.zhihu.com/p/564963398
 * 次参考：
 * https://github.com/zamberform/AnnotationTextAndroid
 * https://juejin.cn/post/6844903766018244622
 * <p>
 * Date:2023/3/2
 * Author: sunHL
 */

public class UnderLineNoteTextView extends AppCompatTextView {
    //下划线起始点和注脚信息
    private ArrayList<TextLineInfo> mInfos = new ArrayList<>();
    private Rect mRect;
    private Paint mLinePaint;
    private TextPaint mTxtPaint;
    private int mUnderLineColor;
    private int mNoteTxtColor;
    private float mUnderLineWidth;
    private float mNoteTopMargin = 0;
    private float mUnderLineTopMargin = 0;
    private float mNoteTextSize = 20;
    private float maxSpace;

    public UnderLineNoteTextView(Context context) {
        this(context, null, 0);
    }

    public UnderLineNoteTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnderLineNoteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        //获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.UnderlinedTextView, defStyleAttr, 0);

        mUnderLineColor = array.getColor(R.styleable.UnderlinedTextView_underlineColor, 0xFFFF0000);
        mNoteTxtColor = array.getColor(R.styleable.UnderlinedTextView_noteTextColor, 0xFFFF0000);

        mUnderLineWidth = array.getDimension(R.styleable.UnderlinedTextView_underlineWidth, 20);
        mUnderLineTopMargin = array.getDimension(R.styleable.UnderlinedTextView_underLineTopMargin, 0);

        mNoteTextSize = array.getDimension(R.styleable.UnderlinedTextView_noteTextSize, 20);
        mNoteTopMargin = array.getDimension(R.styleable.UnderlinedTextView_noteTopMargin, 0);

        //设置行间距：（注脚文字大小+注脚顶部margin）与（下划线宽度+下划线顶部margin）相比，取最大值
        maxSpace = Math.max(mNoteTextSize + mNoteTopMargin, mUnderLineWidth + mUnderLineTopMargin);
        setLineSpacing(maxSpace, 1F);
        //setPadding已被重写
        setPadding(getLeft(), getTop(), getRight(), getBottom());

        array.recycle();

        ///下划线画笔
        mRect = new Rect();
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置下划线带圆角
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(mUnderLineColor);
        mLinePaint.setStrokeWidth(mUnderLineWidth);

        ///注脚画笔
        mTxtPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTxtPaint.setColor(mNoteTxtColor);
        mTxtPaint.setTextSize(mNoteTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //得到TextView显示有多少行
        int count = getLineCount();
        //得到TextView的布局
        final Layout layout = getLayout();
        final float getRight = getRight() - getLeft();

        float x_start, x_stop;
        int firstCharInLine, lastCharInLine;

        //遍历每一行
        for (int i = 0; i < count; i++) {
            //getLineBounds得到这一行的外包矩形,这个字符的顶部Y坐标就是rect的top 底部Y坐标就是rect的bottom
            int baseline = getLineBounds(i, mRect);
            //返回指定行开头的文本偏移量(就是每行首字在全部文本中排第几个字)（0&hellip；getLineCount()）。如果指定的行数等于行数，则返回文本的长度。
            firstCharInLine = layout.getLineStart(i);
            //返回指定行上最后一个字符后的文本偏移量。
            lastCharInLine = layout.getLineEnd(i);

            Iterator<TextLineInfo> iterator = mInfos.iterator();
            while (iterator.hasNext()) {
                x_start = 0;
                x_stop = 0;
                TextLineInfo info = iterator.next();
                //下划线从第几个字开始的
                int lineStart = info.getLineStart();
                //下划线结束于第几个字
                int lineEnd = info.getLineEnd();

                //判断该条下划线是否展示于当前行之前
                if (lineEnd <= firstCharInLine) {
                    //删掉上边行已经展示完的数据，可少遍历
                    //但是Activity执行onResume时会再次走到该onDraw方法，导致缺少数据
                    //iterator.remove();
                } else {
                    //判断当前行有没有下划线要显示
                    if (lineStart < lastCharInLine && lineEnd >= firstCharInLine) {
                        //如果下划线从上一行开始，则firstCharInLine<lineStart
                        //getPrimaryHorizontal:获取字符左边的X坐标，即下划线的起始点X坐标
                        x_start = layout.getPrimaryHorizontal(Math.max(lineStart, firstCharInLine));
                        //防止下划线圆角部分超出文字宽度
                        x_start += mUnderLineWidth / 2;
                        //如果下划线在下一行结束，则lineEnd<lastCharInLine
                        int endNum = Math.min(lineEnd, lastCharInLine);
                        //如果下划线划到行尾，则x_stop取值为行尾的X坐标getLineRight
                        x_stop = (endNum == lastCharInLine) ? layout.getLineRight(i) : layout.getPrimaryHorizontal(endNum);
                        //防止下划线圆角部分超出文字宽度
                        x_stop -= mUnderLineWidth / 2;
                    }

                    //通改x_stop判断当前行有没有下划线
                    if (x_stop > 0) {
                        //下划线垂直方向中点Y坐标 = 下划线高度一半 + 下划线顶部margin + 留余值
                        float bottomY = baseline + mUnderLineWidth / 2 + mUnderLineTopMargin + 2;
                        canvas.drawLine(x_start, bottomY, x_stop, bottomY, mLinePaint);
                        //判断下划线是否在当前行换行：下划线换行的，当前行不画注脚，画在最后一行
                        if (lineEnd <= lastCharInLine) {
                            ///绘制注脚
                            String txt = info.getNote();
                            //计算当前行能不能放得下
                            float txtWidth = mTxtPaint.measureText(txt);
                            //行终点-下划线终点的差值不够放下文字，则文字终点要左移
                            float txtEndX = ((getRight - x_stop) < txtWidth) ? getRight : (x_stop + txtWidth);
                            //文字起点X坐标 = 文字终点X坐标 - 文字宽度
                            float txtStartX = txtEndX - txtWidth;
                            canvas.drawText(txt, txtStartX, baseline + mNoteTextSize + mNoteTopMargin, mTxtPaint);
                        }
                    }
                }
            }
        }
        super.onDraw(canvas);
    }

    public int getUnderLineColor() {
        return mUnderLineColor;
    }

    public void setUnderLineColor(int mColor) {
        this.mUnderLineColor = mColor;
        invalidate();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        //底部要留足下划线、注脚、margin能显示完全的距离
        super.setPadding(left, top, right, Math.round(bottom + maxSpace));
    }

    public float getUnderlineWidth() {
        return mUnderLineWidth;
    }

    public void setUnderlineWidth(float mStrokeWidth) {
        this.mUnderLineWidth = mStrokeWidth;
        invalidate();
    }

    public void setUnderInfo(Collection<TextLineInfo> infos) {
        if (infos != null) {
            mInfos.clear();
            mInfos.addAll(infos);
            invalidate();
        }
    }
}

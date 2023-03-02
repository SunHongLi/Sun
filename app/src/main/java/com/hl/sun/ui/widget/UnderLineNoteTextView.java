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
 * 参考：https://github.com/lixiaote/UnderLineTextView
 * 次参考：https://github.com/zamberform/AnnotationTextAndroid
 * Date:2023/3/2
 * Author: sunHL
 */

public class UnderLineNoteTextView extends AppCompatTextView {
    private ArrayList<TextLineInfo> mInfos = new ArrayList<>();
    private Rect mRect;
    private Paint mPaint;
    private TextPaint mTxtPaint;
    private int mUnderLineColor;
    private int mNoteTxtColor;
    private float mUnderLineWidth;
    private float mNoteTopMargin = 0;
    private float mNoteTextSize = 20;

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
        mNoteTopMargin = array.getDimension(R.styleable.UnderlinedTextView_noteTopMargin, 20);
        mNoteTextSize = array.getDimension(R.styleable.UnderlinedTextView_noteTextSize, 20);

        //行间距
        setLineSpacing(mNoteTextSize + mNoteTopMargin, 1F);
        setPadding(getLeft(), getTop(), getRight(), getBottom());

        array.recycle();

        mRect = new Rect();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mUnderLineColor);
        mPaint.setStrokeWidth(mUnderLineWidth);

        //注脚画笔
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
        final float getRight = getRight();

        float x_start = 0, x_stop = 0, x_diff;
        int firstCharInLine, lastCharInLine;

        //遍历每一行
        for (int i = 0; i < count; i++) {
            //getLineBounds得到这一行的外包矩形,这个字符的顶部Y坐标就是rect的top 底部Y坐标就是rect的bottom
            int baseline = getLineBounds(i, mRect);
            // mRect.bottom+=mLineTopMargin;
            //返回指定行开头的文本偏移量(就是每行首字在全部文本中排第几个字)（0&hellip；getLineCount()）。如果指定的行数等于行数，则返回文本的长度。
            firstCharInLine = layout.getLineStart(i);
            //返回指定  行上最后一个字符后的文本偏移量。
            lastCharInLine = layout.getLineEnd(i);

            Iterator<TextLineInfo> iterator = mInfos.iterator();
            while (iterator.hasNext()) {
                x_start = 0;
                x_stop = 0;
                TextLineInfo info = iterator.next();
                int lineStart = info.getLineStart();
                int lineEnd = info.getLineEnd();
                if (lineEnd <= firstCharInLine) {
                    //删掉上边行已经展示完的数据，可少遍历
                    //iterator.remove();
                } else {
                    if (lineStart < lastCharInLine && lineEnd >= firstCharInLine) {
                        x_start = layout.getPrimaryHorizontal(Math.max(lineStart, firstCharInLine));
                        int endNum = Math.min(lineEnd, lastCharInLine);
                        x_stop = (endNum == lastCharInLine) ? layout.getLineRight(i) : layout.getPrimaryHorizontal(endNum);
                    }


                    if (x_stop > 0) {
                        float bottomY = baseline + mUnderLineWidth / 2 + 2;
                        canvas.drawLine(x_start, bottomY, x_stop, bottomY, mPaint);
                        if (lineEnd <= lastCharInLine) {//断行的不画注脚
                            ///绘制注脚
                            String txt = info.getNote();
                            //计算当前行能不能放得下
                            float txtWidth = mTxtPaint.measureText(txt);
                            float txtEndX = ((getRight - txtWidth) < x_stop) ? getRight : (x_stop + txtWidth);
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
        super.setPadding(left, top, right, bottom + (int) mNoteTopMargin + (int) mUnderLineWidth);
    }

    public float getUnderlineWidth() {
        return mUnderLineWidth;
    }

    public void setUnderlineWidth(float mStrokeWidth) {
        this.mUnderLineWidth = mStrokeWidth;
        invalidate();
    }

    public void setUnderInfo(Collection<TextLineInfo> infos) {
        mInfos.clear();
        mInfos.addAll(infos);
        invalidate();
    }
}

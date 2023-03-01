package com.hl.sun.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatTextView;

import com.hl.sun.R;

/**
 * 参考：https://juejin.cn/post/6844903766018244622
 */
public class DashTextView extends AppCompatTextView {

    private static final String TAG = "DashTextView";
    private Paint mPaint;
    private Path mPath;
    private String tips;
    private String title;
    private int color;
    private boolean dash = true;//默认有虚线

    public DashTextView(Context context) {
        super(context);
    }

    public DashTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public DashTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        color = R.color.gray;
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DashTextView);
            //点击弹窗标题
            if (typedArray.hasValue(R.styleable.DashTextView_title)) {
                title = typedArray.getString(R.styleable.DashTextView_title);
            }
            //点击弹窗内容
            if (typedArray.hasValue(R.styleable.DashTextView_tips)) {
                tips = typedArray.getString(R.styleable.DashTextView_tips);
            }
            //虚线颜色
            if (typedArray.hasValue(R.styleable.DashTextView_color)) {
                color = typedArray.getResourceId(R.styleable.DashTextView_color, R.color.gray);
            }
            //是否显示虚线
            if (typedArray.hasValue(R.styleable.DashTextView_dash)) {
                dash = typedArray.getBoolean(R.styleable.DashTextView_dash, true);
            }
        }
        initPaint();
        setOnClickListener(v -> Log.i(TAG, TextUtils.isEmpty(title) ? getText().toString() : title + "   " + tips));
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //默认使用textview当前颜色
        mPaint.setColor(getResources().getColor(color));
        //画笔粗细
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
        mPath = new Path();
        //设置虚线距离
        setPadding(0, 0, 0, 3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        int centerY = getHeight();
        mPath.reset();
        mPath.moveTo(0, centerY);
        mPath.lineTo(getTextWidth(), centerY);
        canvas.drawPath(mPath, mPaint);
    }

    //获取每行长度，选取最大长度。因为部分手机换行导致虚线过长
    private float getTextWidth() {
        float textWidth = 0;
        //循环遍历打印每一行
        for (int i = 0; i < getLineCount(); i++) {
            if (textWidth < getLayout().getLineWidth(i)) {
                textWidth = getLayout().getLineWidth(i);
            }
        }
        return textWidth == 0 ? getWidth() : textWidth;
    }

}
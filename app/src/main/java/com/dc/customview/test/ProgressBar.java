package com.dc.customview.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.dc.customview.R;

/**
 * Description:圆形进度条
 */
public class ProgressBar extends View {

    private int mInnerBackground = Color.RED;
    private int mOuterBackground = Color.RED;
    private int mRoundWidth = 10;
    private float mProgressTextSize;
    private int mProgressTextColor = Color.RED;

    private Paint mInnerPaint, mOuterPaint, mTextPaint;

    private int mMax = 100;
    private int mProgress = 0;

    public ProgressBar(Context context) {
        this(context, null);
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ProgressBar);
        mInnerBackground = array.getColor(R.styleable.ProgressBar_innerBackground, mInnerBackground);
        mOuterBackground = array.getColor(R.styleable.ProgressBar_outerBackground, mOuterBackground);
        mRoundWidth = (int) array.getDimension(R.styleable.ProgressBar_roundWidth, dip2px(mRoundWidth));
        mProgressTextSize = array.getDimensionPixelSize(R.styleable.ProgressBar_progressTextSize, sp2px(mProgressTextSize));
        mProgressTextColor = array.getColor(R.styleable.ProgressBar_progressTextColor, mProgressTextColor);
        array.recycle();

        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setColor(mInnerBackground);
        mInnerPaint.setStrokeWidth(mRoundWidth);
        mInnerPaint.setStyle(Paint.Style.STROKE);

        mOuterPaint = new Paint();
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setColor(mOuterBackground);
        mOuterPaint.setStrokeWidth(mRoundWidth);
        mOuterPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mProgressTextColor);
        mTextPaint.setTextSize(mProgressTextSize);
    }

    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    private float dip2px(int dip) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(width, height), Math.min(width, height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画内圆
        int center = getWidth() / 2;
        int radius = getWidth() / 2 - mRoundWidth / 2;
        canvas.drawCircle(center, center, radius, mInnerPaint);

        //画外圆弧
        RectF rectF = new RectF(mRoundWidth / 2, mRoundWidth / 2,
                getWidth() - mRoundWidth / 2, getHeight() - mRoundWidth / 2);
        if (mProgress == 0) {
            return;
        }
        float percent = (float) mProgress / mMax;
        canvas.drawArc(rectF, 0, percent * 360, false, mOuterPaint);

        String text =((int) (percent * 100)) + "%";
        Rect textBounds = new Rect();
        mTextPaint.getTextBounds(text,0,text.length(),textBounds);
        int x = getWidth() / 2 - textBounds.width() / 2;
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top)/2-fontMetricsInt.bottom;
        int baseLine = getHeight() / 2 + dy;
        canvas.drawText(text,x,baseLine,mTextPaint);
    }

    public synchronized void setMax(int max){
        if (max < 0) {

        }
        this.mMax = max;
    }

    public synchronized  void setProgress(int progress){
        if (progress < 0){

        }
        this.mProgress = progress;
        // 刷新 invalidate
        invalidate();
    }
}

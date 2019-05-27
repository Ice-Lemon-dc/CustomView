package com.dc.customview.qqstep;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.dc.customview.R;

public class QQStepView extends View {

    private int mOuterColor = Color.RED;
    private int mInnerColor = Color.BLUE;
    /**
     * 20px
     */
    private int mBorderWidth = 20;
    private int mStepTextSize;
    private int mStepTextColor;

    private Paint mOutPaint, mInnerPaint, mTextPaint;

    private int mStepMax ;
    private int mCurrentStep;

    public QQStepView(Context context) {
        this(context, null);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.QQStepView);
        mOuterColor = array.getColor(R.styleable.QQStepView_outerColor, mOuterColor);
        mInnerColor = array.getColor(R.styleable.QQStepView_innerColor, mInnerColor);
        mBorderWidth = (int) array.getDimension(R.styleable.QQStepView_borderWidth, mBorderWidth);
        mStepTextSize = array.getDimensionPixelSize(R.styleable.QQStepView_stepTextSize, mStepTextSize);
        mStepTextColor = array.getColor(R.styleable.QQStepView_stepTextColor, mStepTextColor);
        array.recycle();

        mOutPaint = new Paint();
        mOutPaint.setAntiAlias(true);
        mOutPaint.setStrokeWidth(mBorderWidth);
        mOutPaint.setColor(mOuterColor);
        mOutPaint.setStyle(Paint.Style.STROKE);
        mOutPaint.setStrokeCap(Paint.Cap.ROUND);

        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setStrokeWidth(mBorderWidth);
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setStyle(Paint.Style.STROKE);
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mStepTextColor);
        mTextPaint.setTextSize(mStepTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width > height ? height : width, width > height ? height : width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int center = getWidth() / 2;
        int radius = getWidth() / 2 - mBorderWidth / 2;
        RectF rectF = new RectF(center - radius, center - radius, center + radius, center + radius);
        canvas.drawArc(rectF, 135, 270, false, mOutPaint);

        if (mStepMax == 0) {
            return;
        }

        float sweepAngle = (float)mCurrentStep / mStepMax;
        canvas.drawArc(rectF, 135, sweepAngle * 270, false, mInnerPaint);

        String stepText = mCurrentStep + "";
        Rect textBounds = new Rect();
        mTextPaint.getTextBounds(stepText, 0, stepText.length(), textBounds);
        int dx = getWidth() / 2 - textBounds.width() / 2;
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        int dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        int baseLine = getHeight() / 2 + dy;
        canvas.drawText(stepText, dx, baseLine, mTextPaint);
    }

    public synchronized void setStepMax(int stepMax) {
        this.mStepMax = stepMax;
    }

    public synchronized void setCurrentStep(int currentStep) {
        this.mCurrentStep = currentStep;
        invalidate();
    }
}

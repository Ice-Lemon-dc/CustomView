package com.dc.customview.colortracktextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.dc.customview.R;

public class ColorTrackTextView extends AppCompatTextView {

    /**
     * 绘制不变声字体的画笔
     */
    private Paint mOriginPaint;

    /**
     * 绘制变色字体的画笔
     */
    private Paint mChangePaint;

    private float mCurrentProgress = 0.0f;

    private Direction mDirection = Direction.LEFT_TO_RIGHT;

    public enum Direction {
        /**
         * 左到右，右到左
         */
        LEFT_TO_RIGHT, RIGHT_TO_LEFT
    }

    public ColorTrackTextView(Context context) {
        this(context, null);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context, attrs);
    }

    private void initPaint(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView);
        int originColor = array.getColor(R.styleable.ColorTrackTextView_originColor, getTextColors().getDefaultColor());
        int changeColor = array.getColor(R.styleable.ColorTrackTextView_changeColor, getTextColors().getDefaultColor());

        mOriginPaint = getPaintByColor(originColor);
        mChangePaint = getPaintByColor(changeColor);

        array.recycle();
    }

    private Paint getPaintByColor(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        //防抖动
        paint.setDither(true);
        paint.setTextSize(getTextSize());
        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);


        int middle = (int) (mCurrentProgress * getWidth());
        if (mDirection == Direction.LEFT_TO_RIGHT) {
            //左到右 左边是红色右边是黑色
            drawText(canvas, mChangePaint, 0, middle);
            //绘制变色
            drawText(canvas, mOriginPaint, middle, getWidth());
        } else {
            //右到左 右边是红色左边是黑色
            drawText(canvas, mChangePaint, getWidth() - middle, getWidth());
            //绘制变色
            drawText(canvas, mOriginPaint, 0, getWidth() - middle);
        }
    }

    private void drawText(Canvas canvas, Paint paint, int start, int end) {
        canvas.save();
        Rect rect = new Rect(start, 0, end, getHeight());
        canvas.clipRect(rect);
        String text = getText().toString();
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int x = getWidth() / 2 - bounds.width() / 2;
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        int baseLine = getHeight() / 2 + dy;
        canvas.drawText(text, x, baseLine, paint);
        canvas.restore();
    }

    public void setDirection(Direction direction) {
        this.mDirection = direction;
    }

    public void setCurrentProgress(float currentProgress) {
        this.mCurrentProgress = currentProgress;
        invalidate();
    }

    public void setChangeColor(int changeColor) {
        this.mChangePaint.setColor(changeColor);
    }

    public void setOriginColor(int originColor) {
        this.mOriginPaint.setColor(originColor);
    }
}

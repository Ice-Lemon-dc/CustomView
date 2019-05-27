package com.dc.customview.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ShapeView extends View {

    private Shape mCurrentShape = Shape.Circle;
    private Paint mPaint;
    private Path mPath;

    public ShapeView(Context context) {
        this(context, null);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
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
        switch (mCurrentShape) {
            case Circle:
                int center = getWidth() / 2;
                mPaint.setColor(Color.YELLOW);
                canvas.drawCircle(center, center, center, mPaint);
                break;
            case Square:
                mPaint.setColor(Color.BLUE);
                canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
                break;
            case Triangle:
                mPaint.setColor(Color.RED);
                if (mPath == null) {
                    mPath = new Path();
                    mPath.moveTo(getWidth() / 2, 0);
                    mPath.lineTo(0, (float) (getWidth() / 2 * Math.sqrt(3)));
                    mPath.lineTo(getWidth(),(float) (getWidth() / 2 * Math.sqrt(3)));
                    mPath.close();
                }
                canvas.drawPath(mPath,mPaint);
                break;
            default:
                break;
        }
    }

    public void exchange() {
        switch (mCurrentShape) {
            case Circle:
                mCurrentShape = Shape.Square;
                break;
            case Square:
                mCurrentShape = Shape.Triangle;
                break;
            case Triangle:
                mCurrentShape = Shape.Circle;
                break;
            default:
                break;
        }
        invalidate();
    }

    public enum Shape {
        /**
         * 圆形，正方形，三角
         */
        Circle, Square, Triangle
    }
}

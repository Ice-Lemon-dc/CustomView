package com.dc.customview.ratingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.dc.customview.R;

public class RatingBar extends View {

    private Bitmap mStarNormalBitmap, mStarFocusBitmap;
    private int mGradeNumber = 5;
    private int mCurrentGrade = 0;

    public RatingBar(Context context) {
        this(context, null);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        int starNormalId = array.getResourceId(R.styleable.RatingBar_starNormal, R.drawable.star_normal);
        int starFocusId = array.getResourceId(R.styleable.RatingBar_starFocus, R.drawable.star_selected);
        mStarNormalBitmap = BitmapFactory.decodeResource(getResources(), starNormalId);
        mStarFocusBitmap = BitmapFactory.decodeResource(getResources(), starFocusId);

        mGradeNumber = array.getInt(R.styleable.RatingBar_gradeNumber, mGradeNumber);

        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //需要加上间隔 padding
        int width = mStarNormalBitmap.getWidth() * mGradeNumber;
        int height = mStarNormalBitmap.getHeight();
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mGradeNumber; i++) {
            int x = mStarNormalBitmap.getWidth() * i;
            if (mCurrentGrade > i) {
                canvas.drawBitmap(mStarFocusBitmap, x, 0, null);
            } else {
                canvas.drawBitmap(mStarNormalBitmap, x, 0, null);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //移动 按下 抬起 处理逻辑都是一样
        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN: //尽量减少onDraw的调用
            case MotionEvent.ACTION_MOVE:
//            case MotionEvent.ACTION_UP:
                float moveX = event.getX();
                int currentGrade = (int) (moveX / mStarNormalBitmap.getWidth() + 1);
                if (currentGrade < 0) {
                    currentGrade = 0;
                }
                if (currentGrade > mGradeNumber) {
                    currentGrade = mCurrentGrade;
                }
                if (currentGrade == mCurrentGrade) {
                    return true;
                }
                mCurrentGrade = currentGrade;
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }
}

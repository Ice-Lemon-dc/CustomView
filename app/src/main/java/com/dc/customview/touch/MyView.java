package com.dc.customview.touch;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View {

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("TAG","======MyView dispatchTouchEvent ACTION_DOWN========");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("TAG","++++++++MyView dispatchTouchEvent ACTION_MOVE++++++++");
                break;
            case MotionEvent.ACTION_UP:
                Log.e("TAG","--------MyView dispatchTouchEvent ACTION_UP--------");
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("TAG","======MyView onTouchEvent ACTION_DOWN========");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("TAG","++++++++MyView onTouchEvent ACTION_MOVE++++++++");
                break;
            case MotionEvent.ACTION_UP:
                Log.e("TAG","--------MyView onTouchEvent ACTION_UP--------");
                break;
            default:
                break;
        }
        return true;
    }
}

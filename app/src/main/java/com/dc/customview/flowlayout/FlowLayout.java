package com.dc.customview.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {

    private List<List<View>> mChildViews = new ArrayList<>();
    private FlowAdapter mAdapter;

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //清空集合
        mChildViews.clear();

        int childCount = getChildCount();

        //获取宽度
        int width = MeasureSpec.getSize(widthMeasureSpec);

        //高度需要计算
        int height = getPaddingTop() + getPaddingBottom();

        //一行的宽度
        int lineWidth = getPaddingLeft() + getPaddingRight();

        ArrayList<View> childViews = new ArrayList<>();
        mChildViews.add(childViews);

        // 子View高度不一致的情况下
        int maxHeight = 0;

        //循环测量子View
        for (int i = 0; i < childCount; i++) {
            //for循环测量子View
            View childView = getChildAt(i);

            if (childView.getVisibility() == GONE) {
                return;
            }

            //这段话执行后就可以获取子View的宽高，因为会调用子View的onMeasure方法
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);

            // LinearLayout有自己的 LayoutParams  会复写一个非常重要的方法generateLayoutParams()
            MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();

            // 一行不够的情况下需要换行 考虑 margin
            if (lineWidth + childView.getMeasuredWidth() + params.leftMargin + params.rightMargin > width) {
                //换行，累加高度
                height += maxHeight;
                lineWidth = childView.getMeasuredWidth() + params.rightMargin + params.leftMargin;
                childViews = new ArrayList<>();
                mChildViews.add(childViews);
            } else {
                lineWidth += childView.getMeasuredWidth() + params.rightMargin + params.leftMargin;
                maxHeight = Math.max(childView.getMeasuredHeight() + params.bottomMargin + params.topMargin, maxHeight);
            }
            childViews.add(childView);
        }
        height += maxHeight;

        //根据子View计算设置自己的宽高
        setMeasuredDimension(width, height);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left, top = getPaddingTop(), right, bottom;
        for (List<View> childViews : mChildViews) {
            left = getPaddingLeft();
            int maxHeight = 0;
            for (View childView : childViews) {
                if (childView.getVisibility() == GONE) {
                    continue;
                }
                MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
                left += params.leftMargin;
                int childTop = top + params.topMargin;
                right = left + childView.getMeasuredWidth();
                bottom = childTop + childView.getMeasuredHeight();
                Log.e("TAG", childView.toString());

                Log.e("TAG", "left -> " + left + " top-> " + childTop + " right -> " + right + " bottom-> " + bottom);

                // 摆放
                childView.layout(left, childTop, right, bottom);
                // left 叠加
                left += childView.getMeasuredWidth() + params.rightMargin;

                // 不断的叠加top值
                int childHeight = childView.getMeasuredHeight() + params.topMargin + params.bottomMargin;
                maxHeight = Math.max(maxHeight, childHeight);
            }
            top += maxHeight;
        }
    }

    public void setAdapter(FlowAdapter adapter) {
        if (adapter == null) {
            // 抛空指针异常
        }
        // 清空所有子View
        removeAllViews();

        mAdapter = adapter;

        // 获取数量
        int childCount = mAdapter.getCount();
        for (int i = 0; i < childCount; i++) {
            // 通过位置获取View
            View childView = mAdapter.getView(i, this);
            addView(childView);
        }
    }
}

package com.dc.customview.verticaldragview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;

public class VerticalDragView extends FrameLayout {

    private ViewDragHelper mDragHelper;

    private View mDragView;

    /**
     * 后面菜单的高度
     */
    private int mMenuHeight;
    /**
     * 菜单是否打开
     */
    private boolean mMenuIsOpen = false;

    private float mDownY;


    public VerticalDragView(@NonNull Context context) {
        this(context, null);
    }

    public VerticalDragView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalDragView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragHelper = ViewDragHelper.create(this, mDragHelperCallback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount != 2) {
            throw new RuntimeException("VerticalDragListView 只能包含两个子布局");
        }
        mDragView = getChildAt(1);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 如果布局内容改变 setVisibility();
        if (changed) {
            View menuView = getChildAt(0);
            mMenuHeight = menuView.getMeasuredHeight();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 菜单打开要拦截
        if (mMenuIsOpen) {
            return true;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                // 让 DragHelper 拿一个完整的事件
                mDragHelper.processTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = ev.getY();
                if ((moveY - mDownY > 0) && !canChildScrollUp()) {
                    // 向下滑动 && 滚动到了顶部，拦截不让ListView做处理
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    /**
     * 判断View是否滚动到了最顶部 (SwipeRefreshLayout中的源码)
     *
     * @return 还能不能向上滚
     */
    public boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mDragView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mDragView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return mDragView.canScrollVertically(-1) || mDragView.getScrollY() > 0;
            }
        } else {
            return mDragView.canScrollVertically(-1);
        }
    }

    private ViewDragHelper.Callback mDragHelperCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            // 指定该子View是否可以拖动，就是 child
            // 只能是列表可以拖动
            return child == mDragView;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            // 垂直拖动移动的位置
            // 垂直拖动的范围只能是后面菜单 View 的高度
            if (top <= 0) {
                top = 0;
            }
            if (top >= mMenuHeight) {
                top = mMenuHeight;
            }
            return top;
        }

        // 手指松开的时候两者选其一，要么打开要么关闭
        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            if (releasedChild == mDragView) {
                if (mDragView.getTop() > mMenuHeight / 2) {
                    // 滚动到菜单的高度（打开）
                    mDragHelper.settleCapturedViewAt(0, mMenuHeight);
                    mMenuIsOpen = true;
                } else {
                    // 滚动到0的位置（关闭）
                    mDragHelper.settleCapturedViewAt(0, 0);
                    mMenuIsOpen = false;
                }
                invalidate();
            }
        }
    };

    /**
     * 响应滚动
     */
    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }
}

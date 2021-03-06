package com.dc.customview.slidingmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.dc.ScreenUtils;
import com.dc.customview.R;

public class SlidingMenu extends HorizontalScrollView {

    /**
     * 菜单的宽度
     */
    private int mMenuWidth;

    private View mContentView, mMenuView;

    /**
     * 手指快速滑动 - 手势处理类
     */
    private GestureDetector mGestureDetector;

    /**
     * 手指快速滑动 - 菜单是否打开
     */
    private boolean mMenuIsOpen = false;

    /**
     *
     */
    private boolean mIsIntercept = false;

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 初始化自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SlidingMenu);
        float rightMargin = array.getDimension(
                R.styleable.SlidingMenu_menuRightMargin, ScreenUtils.dip2px(context, 50));
        // 菜单页的宽度是 = 屏幕的宽度 - 右边的一小部分距离（自定义属性）
        mMenuWidth = (int) (ScreenUtils.getScreenWidth(context) - rightMargin);
        array.recycle();
        mGestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    protected void onFinishInflate() {
        // 这个方法是布局解析完毕也就是 XML 布局文件解析完毕
        super.onFinishInflate();
        // 获取LinearLayout
        ViewGroup container = (ViewGroup) getChildAt(0);

        int childCount = container.getChildCount();
        if (childCount != 2) {
            throw new RuntimeException("只能放置两个子View!");
        }

        mMenuView = container.getChildAt(0);

        ViewGroup.LayoutParams menuParams = mMenuView.getLayoutParams();
        menuParams.width = mMenuWidth;
        // 7.0 以下的手机必须采用下面的方式
        mMenuView.setLayoutParams(menuParams);

        // 2.菜单页的宽度是 屏幕的宽度 - 右边的一小部分距离（自定义属性）
        mContentView = container.getChildAt(1);
        ViewGroup.LayoutParams contentParams = mContentView.getLayoutParams();
        contentParams.width = ScreenUtils.getScreenWidth(getContext());
        mContentView.setLayoutParams(contentParams);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        scrollTo(mMenuWidth, 0);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        // 算一个梯度值
        float scale = 1f * l / mMenuWidth;
        // 右边的缩放: 最小是 0.7f, 最大是 1f
        float rightScale = 0.7f + 0.3f * scale;
        // 设置右边的缩放,默认是以中心点缩放
        // 设置缩放的中心点位置
        mContentView.setPivotX(0);
        mContentView.setPivotY(mContentView.getMeasuredHeight() / 2);
        mContentView.setScaleX(rightScale);
        mContentView.setScaleY(rightScale);

        // 菜单的缩放和透明度
        // 透明度是 半透明到完全透明  0.5f - 1.0f
        float leftAlpha = 0.5f + (1 - scale) * 0.5f;
        mMenuView.setAlpha(leftAlpha);
        float leftScale = 0.7f + (1 - scale) * 0.3f;
        mMenuView.setScaleX(leftScale);
        mMenuView.setScaleY(leftScale);

        mMenuView.setTranslationX(0.25f * l);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // 如果有拦截不需要执行自己的onTouchEvent
        if (mIsIntercept) {
            return true;
        }
        if (mGestureDetector.onTouchEvent(ev)) {
            //处理手指快速滑动  手势处理类使用  拦截
            return true;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                //只需要管手指抬起 ，根据我们当前滚动的距离来判断
                int currentScrollx = getScrollX();
                if (currentScrollx < mMenuWidth / 2) {
                    //关闭
                    openMenu();
                } else {
                    //打开
                    closeMenu();
                }
                // 确保 super.onTouchEvent() 不会执行
                return true;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mIsIntercept = false;
        // 当菜单打开的时候，手指触摸右边部分内容需要关闭菜单，还要拦截事件
        if (mMenuIsOpen) {
            float currentX = ev.getX();
            if (currentX > mMenuWidth) {
                closeMenu();
                // 子View不需要响应任何事件
                mIsIntercept = true;
                //返回true 会拦截子View的事件 但也会响应自己的onTouchEvent事件
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void openMenu() {
        smoothScrollTo(0, 0);
        mMenuIsOpen = true;
    }

    private void closeMenu() {
        smoothScrollTo(mMenuWidth, 0);
        mMenuIsOpen = false;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(velocityY) > Math.abs(velocityX)) {
                // 代表上下快速划  这个时候不做处理
                return super.onFling(e1, e2, velocityX, velocityY);
            }
            if (mMenuIsOpen) {
                // 逻辑  如果是菜单打开  向左边快速滑动的时候 应该切换菜单状态
                if (velocityX < 0) {
                    toggleMenu();
                    return true;
                }
            } else {
                // 如果是菜单关闭  向右边快速滑动的时候 应该切换菜单状态
                if (velocityX > 0) {
                    toggleMenu();
                    return true;
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    public void toggleMenu() {
        if (mMenuIsOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }
}

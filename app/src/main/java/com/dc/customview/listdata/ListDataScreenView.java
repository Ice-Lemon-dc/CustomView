package com.dc.customview.listdata;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * @author Lemon
 */
public class ListDataScreenView extends LinearLayout implements View.OnClickListener {

    private Context mContext;
    /**
     * 头部Tab
     */
    private LinearLayout mMenuTabView;

    /**
     * 阴影（View） + 菜单内容布局(FrameLayout)
     */
    private FrameLayout mMenuMiddleView;

    /**
     * 阴影
     */
    private View mShadowView;

    /**
     * 菜单内容
     */
    private FrameLayout mMenuContainerView;

    /**
     * 阴影的颜色
     */
    private int mShadowColor = 0x88888888;

    private BaseMenuAdapter mAdapter;
    private int mMenuContainerHeight;

    /**
     * 动画是否在执行
      */
    private boolean mAnimatorExecute;

    /**
     * 当前打开的位置
     */
    private int mCurrentPosition = -1;

    private static final long DURATION_TIME = 350;

    public ListDataScreenView(Context context) {
        this(context, null);
    }

    public ListDataScreenView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListDataScreenView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initLayout();
    }

    private void initLayout() {
        setOrientation(VERTICAL);
        // 1.1创建头部用来存放Tab
        mMenuTabView = new LinearLayout(mContext);
        mMenuTabView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mMenuTabView);

        // 用来存放阴影 + 菜单内容
        mMenuMiddleView = new FrameLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        params.weight = 1;
        mMenuMiddleView.setLayoutParams(params);
        addView(mMenuMiddleView);

        mShadowView = new View(mContext);
        mShadowView.setBackgroundColor(mShadowColor);
        mShadowView.setAlpha(0);
        mShadowView.setVisibility(GONE);
        mShadowView.setOnClickListener(this);
        mMenuMiddleView.addView(mShadowView);

        mMenuContainerView = new FrameLayout(mContext);
        mMenuContainerView.setBackgroundColor(Color.WHITE);
        mMenuMiddleView.addView(mMenuContainerView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 内容高度是整个View的75%
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (mMenuContainerHeight == 0 && height > 0) {
            mMenuContainerHeight = (int) (height * 75f / 100);
            ViewGroup.LayoutParams params = mMenuContainerView.getLayoutParams();
            params.height = mMenuContainerHeight;
            mMenuContainerView.setLayoutParams(params);
            // 进来的时候阴影不显示 ，内容也是不显示的（把它移上去）
            mMenuContainerView.setTranslationY(-mMenuContainerHeight);
        }
    }

    /**
     * 设置Adapter
     *
     * @param adapter BaseMenuAdapter
     */
    public void setAdapter(BaseMenuAdapter adapter) {
        this.mAdapter = adapter;

        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            // 获取菜单的Tab
            View tabView = mAdapter.getTabView(i, mMenuTabView);
            mMenuTabView.addView(tabView);
            LinearLayout.LayoutParams params = (LayoutParams) tabView.getLayoutParams();
            params.weight = 1;
            tabView.setLayoutParams(params);

            // 获取菜单内容
            View menuView = mAdapter.getMenuView(i, mMenuContainerView);
            menuView.setVisibility(GONE);
            mMenuContainerView.addView(menuView);

            // 设置Tab点击事件
            setTabClick(tabView, i);
        }
    }

    private void setTabClick(View tabView, int position) {
        tabView.setOnClickListener(v -> {
            if (mCurrentPosition == -1) {
                // 没打开
                openMenu(tabView,position);
            } else {
                if (mCurrentPosition == position) {
                    closeMenu();
                } else {
                    // 切换显示
                    View currentMenu = mMenuContainerView.getChildAt(mCurrentPosition);
                    currentMenu.setVisibility(GONE);
                    mAdapter.menuClose(mMenuTabView.getChildAt(mCurrentPosition));
                    mCurrentPosition = position;
                    currentMenu = mMenuContainerView.getChildAt(mCurrentPosition);
                    currentMenu.setVisibility(VISIBLE);
                    mAdapter.menuOpen(mMenuTabView.getChildAt(mCurrentPosition));
                }
            }
        });
    }

    private void closeMenu() {
        Log.e("---===", "" + mAnimatorExecute);
        if (mAnimatorExecute) {
            return;
        }
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mMenuContainerView, "translationY", 0, -mMenuContainerHeight);
        translationAnimator.setDuration(DURATION_TIME);
        translationAnimator.start();
        mShadowView.setVisibility(View.VISIBLE);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mShadowView, "alpha", 1f, 0f);
        alphaAnimator.setDuration(DURATION_TIME);
        // 要等关闭动画执行完才能去隐藏当前菜单
        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                View menuView = mMenuContainerView.getChildAt(mCurrentPosition);
                menuView.setVisibility(View.GONE);
                mCurrentPosition = -1;
                mShadowView.setVisibility(GONE);
                mAnimatorExecute = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                mAnimatorExecute = true;
                mAdapter.menuClose(mMenuTabView.getChildAt(mCurrentPosition));
            }
        });
        alphaAnimator.start();
    }

    private void openMenu(View tabView, int position) {
        if (mAnimatorExecute) {
            return;
        }

        mShadowView.setVisibility(VISIBLE);

        View menuView = mMenuContainerView.getChildAt(position);
        menuView.setVisibility(VISIBLE);

        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mMenuContainerView, "translationY", -mMenuContainerHeight, 0);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mShadowView, "alpha", 0f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationAnimator, alphaAnimator);
        animatorSet.setDuration(DURATION_TIME);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimatorExecute = true;
                // 把当前的 tab 传到外面
                mAdapter.menuOpen(tabView);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimatorExecute = false;
                mCurrentPosition = position;
            }
        });
        animatorSet.start();
    }

    @Override
    public void onClick(View v) {
        closeMenu();
    }
}

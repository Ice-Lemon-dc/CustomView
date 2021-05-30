package com.dc.customview.circleloading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

/**
 * @author Lemon
 */
public class LoadingView extends RelativeLayout {

    private CircleView mLeftView, mMiddleView, mRightView;
    private int mTranslationDistance = 30;
    private final long ANIMATION_TIME = 350;

    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTranslationDistance = dip2px(mTranslationDistance);
        setBackgroundColor(Color.WHITE);
        // 添加三个View 但是要圆形
        mLeftView = getCircleView(context);
        mLeftView.exchangeColor(Color.BLUE);
        mMiddleView = getCircleView(context);
        mMiddleView.exchangeColor(Color.RED);
        mRightView = getCircleView(context);
        mRightView.exchangeColor(Color.GREEN);

        addView(mLeftView);
        addView(mRightView);
        // 中间的点在最上面
        addView(mMiddleView);

        post(new Runnable() {
            @Override
            public void run() {
                expendAnimation();
            }
        });
    }

    private void expendAnimation() {
        ObjectAnimator leftTranslationAnimator = ObjectAnimator.ofFloat(mLeftView, "translationX",0, -mTranslationDistance);
        ObjectAnimator rightTranslationAnimator = ObjectAnimator.ofFloat(mRightView, "translationX", 0, mTranslationDistance);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIMATION_TIME);
        set.setInterpolator(new DecelerateInterpolator());
        set.playTogether(leftTranslationAnimator, rightTranslationAnimator);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 往里面跑
                innerAnimation();
            }
        });
        set.start();
    }

    private void innerAnimation() {
        // 左边跑
        ObjectAnimator leftTranslationAnimator = ObjectAnimator.ofFloat(mLeftView,"translationX",-mTranslationDistance,0);
        // 右边跑
        ObjectAnimator rightTranslationAnimator = ObjectAnimator.ofFloat(mRightView,"translationX",mTranslationDistance,0);
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new AccelerateInterpolator());
        set.setDuration(ANIMATION_TIME);
        set.playTogether(leftTranslationAnimator,rightTranslationAnimator);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                int leftColor = mLeftView.getColor();
                int rightColor = mRightView.getColor();
                int middleColor = mMiddleView.getColor();
                mMiddleView.exchangeColor(leftColor);
                mRightView.exchangeColor(middleColor);
                mLeftView.exchangeColor(rightColor);
                expendAnimation();
            }
        });
        set.start();
    }

    private CircleView getCircleView(Context context) {
        CircleView circleView = new CircleView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dip2px(10),dip2px(10));
        params.addRule(CENTER_IN_PARENT);
        circleView.setLayoutParams(params);
        return circleView;
    }

    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dip, getResources().getDisplayMetrics());
    }
}

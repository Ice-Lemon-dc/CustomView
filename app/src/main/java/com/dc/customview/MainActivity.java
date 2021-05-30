package com.dc.customview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.appcompat.app.AppCompatActivity;

import com.dc.customview.colortracktextview.ColorTrackTextView;
import com.dc.customview.qqstep.QQStepView;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *  自定义View:
 *      1.自定义属性,获取自定义属性(达到配置效果)
 *      2.onMeasure()方法用于测量计算自己的宽高(如果是继承系统已有的TextView、Button则不需要)
 *      3.onDraw()用于绘制自己的显示
 *      4.onTouch()用于与用户交互
 *
 *   自定义ViewGroup:
 *      1.自定义属性,获取自定义属性(达到配置效果) 很少有
 *      2.onMeasure()方法,for循环测量子View,根据子View的宽高来计算自己的宽高
 *      3.onDraw()一般不需要,默认情况是不会调用,如果需要绘制需要实现dispatchDraw()方法
 *      4.onLayout()用来摆放子View,前提是不是GONE的情况
 *      5.onTouch()用于与用户交互
 */
public class MainActivity extends AppCompatActivity {

    private ColorTrackTextView mColorTrackTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final QQStepView qqStepView = findViewById(R.id.step_view);
        qqStepView.setStepMax(4000);
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, 3000);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentStep = (float) animation.getAnimatedValue();
                qqStepView.setCurrentStep((int) currentStep);
            }
        });
        valueAnimator.start();

        mColorTrackTextView = findViewById(R.id.color_track_tv);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public void leftToRight(View view) {
        mColorTrackTextView.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT);
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentProgress = (float) animation.getAnimatedValue();
                mColorTrackTextView.setCurrentProgress(currentProgress);
            }
        });
        valueAnimator.start();

    }

    public void rightToLeft(View view) {
        mColorTrackTextView.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentProgress = (float) animation.getAnimatedValue();
                mColorTrackTextView.setCurrentProgress(currentProgress);
            }
        });
        valueAnimator.start();
    }
}

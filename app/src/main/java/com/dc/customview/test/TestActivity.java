package com.dc.customview.test;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.dc.customview.R;

public class TestActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private ShapeView mShapeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setMax(5000);

        ValueAnimator animator = ObjectAnimator.ofFloat(0,5000);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                mProgressBar.setProgress((int) progress);
            }
        });
        animator.start();

        mShapeView = findViewById(R.id.shape_view);
    }

    public void exchange(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mShapeView.exchange();
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}

package com.dc.customview.viewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dc.customview.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerCardActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<Integer> mUlr;
    private Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_card);

        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(3);


        mViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {

           float max_scale = 1f;
           float min_scale = 0.8f;
            @Override
            public void transformPage(View page, float position) {
                if (position < -1) {
                    position = -1;
                } else if (position > 1) {
                    position = 1;
                }

                float temp = 1 - Math.abs(position);
                float scale = min_scale + (max_scale - min_scale) * temp;
                page.setScaleX(scale);
                page.setScaleY(scale);
            }
        });

        mUlr = new ArrayList<>();
        mUlr.add(R.mipmap.img);
        mUlr.add(R.mipmap.img);
        mUlr.add(R.mipmap.img);
        mUlr.add(R.mipmap.img);
        mUlr.add(R.mipmap.img);

        mAdapter = new Adapter(getSupportFragmentManager(),mUlr);
        mViewPager.setAdapter(mAdapter);
    }

    public static class Adapter extends FragmentPagerAdapter {

        private List<Integer> datas;

        public Adapter(FragmentManager fm,List<Integer> datas) {
            super(fm);
            this.datas = datas;
        }

        @Override
        public Fragment getItem(int position) {
            return ViewPagerFragment.getInstance(datas.get(position));
        }

        @Override
        public int getCount() {
            return datas.size();
        }
    }
}

package com.dc.customview.parallaxviewpager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dc.customview.R;

/**
 * 视差效果ViewPager
 *
 * @author Lemon
 */
public class ParallaxViewPagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parallax_view_pager);

        ParallaxViewPager parallaxViewPager = findViewById(R.id.parallax_vp);
        parallaxViewPager.setLayout(getSupportFragmentManager(), new int[]{R.layout.fragment_page_first,R.layout.fragment_page_second,
                R.layout.fragment_page_third,R.layout.fragment_page_first});
    }
}
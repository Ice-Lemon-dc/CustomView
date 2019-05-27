package com.dc.customview.viewpager;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dc.customview.R;

public class ViewPagerFragment extends Fragment{

    private ImageView mImageView;

    public static ViewPagerFragment getInstance(int url){
        ViewPagerFragment fragment = new ViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("url",url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_item,null);
        mImageView = view.findViewById(R.id.iv);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null){
            int url = bundle.getInt("url");
            mImageView.setImageResource(url);
        }
    }
}

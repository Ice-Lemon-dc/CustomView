package com.dc.customview.listdata;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dc.customview.R;

/**
 * @author Lemon
 */
public class ListScreenMenuAdapter extends BaseMenuAdapter {

    private Context mContext;
    private String[] mItems = {"类型", "品牌", "价格", "更多"};

    public ListScreenMenuAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public View getTabView(int position, ViewGroup parent) {
        TextView tabView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.ui_list_data_screen_tab, parent, false);
        tabView.setText(mItems[position]);
        return tabView;
    }

    @Override
    public View getMenuView(int position, ViewGroup parent) {
        TextView menuView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.ui_list_data_screen_menu, parent, false);
        menuView.setText(mItems[position]);
        return menuView;
    }

    @Override
    public void menuClose(View tabView) {
        TextView tabTv = (TextView) tabView;
        tabTv.setTextColor(Color.BLACK);
    }

    @Override
    public void menuOpen(View tabView) {
        TextView tabTv = (TextView) tabView;
        tabTv.setTextColor(Color.RED);
    }
}

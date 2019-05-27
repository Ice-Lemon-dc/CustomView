package com.dc.customview.verticaldragview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dc.customview.R;

import java.util.ArrayList;
import java.util.List;

public class VerticalDragViewActivity extends AppCompatActivity {

    private ListView mListView;
    private List<String> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_drag_view);

        mListView = findViewById(R.id.list_view);

        mItems = new ArrayList<String>();

        for (int i = 0; i < 50; i++) {
            mItems.add("i -> " + i);
        }

        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mItems.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView item = (TextView) LayoutInflater.from(VerticalDragViewActivity.this)
                        .inflate(R.layout.item_vertical_drag, parent, false);
                item.setText(mItems.get(position));
                return item;
            }
        });
    }
}

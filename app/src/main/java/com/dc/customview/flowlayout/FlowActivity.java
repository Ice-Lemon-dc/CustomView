package com.dc.customview.flowlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dc.customview.R;

import java.util.ArrayList;
import java.util.List;

public class FlowActivity extends AppCompatActivity {

    private FlowLayout mFlowLayout;
    private List<String> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);

        mFlowLayout = findViewById(R.id.flow_layout);

        mItems = new ArrayList<>();
        mItems.add("1111111");
        mItems.add("11");
        mItems.add("1111");
        mItems.add("1111");
        mItems.add("11");
        mItems.add("1111");
        mItems.add("1111111");
        mItems.add("1111111");
        mItems.add("11");
        mItems.add("1111");
        mItems.add("1111");
        mItems.add("11");
        mItems.add("1111");
        mItems.add("1111111");

        mFlowLayout.setAdapter(new FlowAdapter() {
            @Override
            public int getCount() {
                return mItems.size();
            }

            @Override
            public View getView(int position, ViewGroup parent) {
                TextView tagTv = (TextView) LayoutInflater.from(FlowActivity.this).inflate(R.layout.item_tag, parent, false);
                tagTv.setText(mItems.get(position));
                return tagTv;
            }
        });
    }
}

package com.dc.customview.listdata;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dc.customview.R;

/**
 * @author Lemon
 */
public class ListDataScreenActivity extends AppCompatActivity {

    private ListDataScreenView mListDataScreenView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data_screen);

        mListDataScreenView = findViewById(R.id.list_data_screen_view);
        mListDataScreenView.setAdapter(new ListScreenMenuAdapter(this));
    }
}
package com.dc.customview.messagebubble;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import com.dc.customview.R;

/**
 * @author Lemon
 */
public class MessageBubbleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_bubble);

        MessageBubbleView.attach(findViewById(R.id.text_view), new BubbleMessageTouchListener.BubbleDisappearListener() {
            @Override
            public void dismiss(View view) {

            }
        });
    }
}
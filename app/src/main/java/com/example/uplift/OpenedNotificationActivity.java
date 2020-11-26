package com.example.uplift;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.time.Instant;
import java.util.Objects;

public class OpenedNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opened_notification);

        Intent receivedInfo = getIntent();
        int imgId = receivedInfo.getIntExtra("img", -1);

        LinearLayout mainLayout = findViewById(R.id.opened_notification_main);
        TextView tvContent = findViewById(R.id.opened_notification_content);
        ImageView ivContent = findViewById(R.id.opened_notification_img);

        if (imgId == -1) {
            Log.e("ERROR", "Text content");

            String textContent = receivedInfo.getStringExtra("text");
            tvContent.setText(textContent);
            mainLayout.setBackgroundColor(Color.WHITE);
            tvContent.setVisibility(View.VISIBLE);
        } else {
            Log.e("ERROR", String.valueOf(imgId));
            mainLayout.setBackgroundColor(Color.BLACK);
            ivContent.setImageResource(imgId);
            ivContent.setVisibility(View.VISIBLE);

        }


    }
}
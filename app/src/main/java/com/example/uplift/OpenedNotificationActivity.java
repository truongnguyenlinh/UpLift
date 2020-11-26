package com.example.uplift;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.time.Instant;
import java.util.Objects;

public class OpenedNotificationActivity extends AppCompatActivity {

    private Instant Glide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opened_notification);

        Intent receivedInfo = getIntent();
//        String categoryName = receivedInfo.getStringExtra("category");
        int imgId = receivedInfo.getIntExtra("img", -1);

        LinearLayout mainLayout = findViewById(R.id.opened_notification_main);
//        TextView tvTitle = findViewById(R.id.opened_notification_title);
        TextView tvContent = findViewById(R.id.opened_notification_content);
        ImageView ivContent = findViewById(R.id.opened_notification_img);

//        tvTitle.setText(categoryName);

        if (imgId == -1) {
            Log.e("ERROR", "Text content");

            String textContent = receivedInfo.getStringExtra("text");
            tvContent.setText(textContent);
            mainLayout.setBackgroundColor(Color.WHITE);
        } else {
            Log.e("ERROR", String.valueOf(imgId));
            mainLayout.setBackgroundColor(Color.BLACK);
            ivContent.setImageResource(imgId);
        }


    }
}
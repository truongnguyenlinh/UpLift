package com.example.uplift;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
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
        String categoryName = receivedInfo.getStringExtra("category");
        int imgId = receivedInfo.getIntExtra("img", -1);

        TextView tvTitle = findViewById(R.id.opened_notification_title);
        TextView tvContent = findViewById(R.id.opened_notification_content);
        ImageView ivContent = findViewById(R.id.opened_notification_img);

        tvTitle.setText(categoryName);

        if (imgId == -1) {
            Log.e("ERROR", "Text content");

            String textContent = receivedInfo.getStringExtra("text");
            tvContent.setText(textContent);
        } else {
            Log.e("ERROR", String.valueOf(imgId));
            ivContent.setImageResource(imgId);
        }


    }
}
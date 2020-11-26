package com.example.uplift;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class OpenedNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opened_notification);

        TextView tvTitle = findViewById(R.id.opened_notification_title);
        TextView tvContent = findViewById(R.id.opened_notification_content);
        ImageView ivContent = findViewById(R.id.opened_notification_img);

    }
}
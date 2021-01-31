package com.example.uplift;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.time.Instant;
import java.util.Objects;

public class OpenedNotificationActivity extends AppCompatActivity {

    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opened_notification);

        Intent receivedInfo = getIntent();
        int imgId = receivedInfo.getIntExtra("img", -1);

        ConstraintLayout mainLayout = findViewById(R.id.opened_notification_main);
        TextView tvContent = findViewById(R.id.opened_notification_content);
        ImageView ivContent = findViewById(R.id.opened_notification_img);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpenedNotificationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        if (imgId == -1) {
            String textContent = receivedInfo.getStringExtra("text");
            tvContent.setText(textContent);
            mainLayout.setBackgroundColor(Color.WHITE);
            tvContent.setVisibility(View.VISIBLE);
        } else {
            mainLayout.setBackgroundColor(Color.BLACK);
            ivContent.setImageResource(imgId);
            ivContent.setVisibility(View.VISIBLE);

        }


    }
}
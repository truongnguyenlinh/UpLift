package com.example.uplift;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ContentSelectionActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    RecyclerView categoryRecycler;
    Category[] categories;
    List<String> selectedCategories;
    Button btnFinish;
    String name;
    String frequencyString;
    int frequency;

    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_content_selection);

        categoryRecycler = findViewById(R.id.category_recycler);
        btnFinish = findViewById(R.id.btnFinish);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        frequency = intent.getIntExtra("frequency", 60);
        frequencyString = intent.getStringExtra("frequencyString");

        selectedCategories = new ArrayList<>();
        categories = Category.getAllCategories();
        String[] categoryNames = new String[categories.length];
        int[] categoryImages = new int[categories.length];
        for (int i = 0; i < categories.length; i++) {
            categoryNames[i] = categories[i].getName();
            categoryImages[i] = categories[i].getImageResourceId();
        }

        CategoriesAdapter adapter = new CategoriesAdapter(categoryNames, categoryImages);
        categoryRecycler.setAdapter(adapter);

        StaggeredGridLayoutManager lm = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        categoryRecycler.setLayoutManager(lm);

        adapter.setListener(new CategoriesAdapter.Listener() {
            @Override
            public void onClick(View categoryView, Boolean selected, String categoryName) {
                ImageView ivCategory = categoryView.findViewById(R.id.category_image);
                TextView tvCategory = categoryView.findViewById(R.id.category_name);
                ImageView ivCheckMark = categoryView.findViewById(R.id.check_mark);

                if (selected) {
                    // Main category
                    ivCategory.setColorFilter(Color.argb(150, 0, 0, 0));
                    tvCategory.setTextColor(Color.argb(150, 255, 255, 255));
                    ivCheckMark.setVisibility(View.VISIBLE);
                    selectedCategories.add(categoryName);

                } else {
                    ivCategory.setColorFilter(Color.argb(0, 0, 0, 0));
                    tvCategory.setTextColor(Color.argb(255, 255, 255, 255));
                    ivCheckMark.setVisibility(View.INVISIBLE);
                    selectedCategories.remove(categoryName);
                }
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPreferences();
                startAlert();
                Intent intent = new Intent(ContentSelectionActivity.this, MainActivity.class);
                intent.putExtra("frequencyString", frequencyString);
                startActivity(intent);
            }
        });
    }

    private void setPreferences() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        UserPreference userPreference = new UserPreference(name, frequency, selectedCategories);
        databaseReference.child(user.getUid()).setValue(userPreference);
    }

    public void startAlert() {

        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), frequency
                , pendingIntent);

        Toast.makeText(this, "You Will Be UpLifted " + frequencyString + "!",
                Toast.LENGTH_LONG).show();
    }
}

package com.example.uplift;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GetFrequencyActivity extends AppCompatActivity {

    private Button btnNext;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private Spinner spinnerFrequency;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_frequency);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(),SignInActivity.class));
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
        spinnerFrequency = findViewById(R.id.spinnerFrequency);
        btnNext = findViewById(R.id.btnNext);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.frequency, R.layout.spinner_item);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerFrequency.setAdapter(adapter);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String frequencyString = spinnerFrequency.getSelectedItem().toString();
                int frequency = frequencyToInt(frequencyString);

//                setPreferences();
                Intent intent = new Intent(GetFrequencyActivity.this, ContentSelectionActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("frequency", frequency);
                intent.putExtra("frequencyString", frequencyString);

                startActivity(intent);
            }
        });
    }

    private int frequencyToInt(String frequency) {
        int frequencyMilliseconds = 0;
        switch (frequency) {
            case "Every Half Hour":
                frequencyMilliseconds = 1800000;
                break;
            case "Every Hour":
                frequencyMilliseconds = 3600000;
                break;
            case "Every 2 Hours":
                frequencyMilliseconds = 7200000;
                break;
            case "Every 4 Hours":
                frequencyMilliseconds = 14400000;
                break;
            case "Every 8 Hours":
                frequencyMilliseconds = 28800000;
                break;
            case "Once a Day":
                frequencyMilliseconds = 86400000;
                break;
        }
        return frequencyMilliseconds;
    }
}
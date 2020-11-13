package com.example.uplift;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class GetNameActivity extends AppCompatActivity {

    private Button btnDone;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private TextView tvNamePrompt;
    private EditText editTextName;

    public GetNameActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_name);
        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(),SignInActivity.class));
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
//        tvNamePrompt = findViewById(R.id.tvNamePrompt);
        editTextName = findViewById(R.id.EditTextName);
        btnDone = findViewById(R.id.btnNext);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserName();
                startActivity(new Intent(GetNameActivity.this, GetFrequencyActivity.class));
            }
        });

    }
    private void setUserName(){
        String name = editTextName.getText().toString().trim();

        UserPreference userPreference = new UserPreference(name);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child(user.getUid()).setValue(userPreference);
    }
}
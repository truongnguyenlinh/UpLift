package com.example.uplift;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SetPreferencesActivity extends AppCompatActivity {

    Button btnDone;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private EditText editTextName;
    private Spinner spinnerFrequency;

    public SetPreferencesActivity() {
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_preferences);
        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(),SignInActivity.class));
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
        editTextName = findViewById(R.id.EditTextName);
        spinnerFrequency = findViewById(R.id.spinnerFrequency);
        btnDone = findViewById(R.id.btnDone);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPreference();
                startActivity(new Intent(SetPreferencesActivity.this, MainActivity.class));
            }
        });

    }
    private void userPreference(){
        String name = editTextName.getText().toString().trim();

        UserPreference userPreference = new UserPreference(name);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child(user.getUid()).setValue(userPreference);
        Toast.makeText(getApplicationContext(),"User information updated",Toast.LENGTH_LONG).show();
    }
}
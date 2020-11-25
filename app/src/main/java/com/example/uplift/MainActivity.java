package com.example.uplift;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    Button btnLogOut;
    Button btnSettings;
    TextView welcome;
    String name;
    int frequency;
    String frequencyString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        welcome = findViewById(R.id.welcome);
        btnLogOut = findViewById(R.id.btnLogOut);
        btnSettings = findViewById(R.id.btnSettings);

        Intent intent = getIntent();
        frequencyString = intent.getStringExtra("frequencyString");

        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(),SignInActivity.class));
        }
        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.child("name").getValue(String.class);
                frequency = dataSnapshot.child("frequency").getValue(int.class);
                welcome.setText("Welcome " + name + "!");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ERROR", "The read failed: " + databaseError.getCode());
            }
        });
        welcome.setText("Welcome.. ");

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                stopNotifications();
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateDialog();
            }
        });
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }

    public void showUpdateDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.settings_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextUserName = dialogView.findViewById(R.id.editTextUserName);
        editTextUserName.setText(name);

        final Spinner spinnerSettingsFrequency = dialogView.findViewById(R.id.spinnerSettingsFrequency);
        spinnerSettingsFrequency.setSelection(getIndex(spinnerSettingsFrequency, frequencyToString(frequency)));

        final Button btnUpdate = dialogView.findViewById(R.id.btnUpdate);
        dialogBuilder.setTitle("Update Settings");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextUserName.getText().toString().trim();
                String frequency = spinnerSettingsFrequency.getSelectedItem().toString().trim();

                databaseReference.child("name").setValue(name);
                databaseReference.child("frequency").setValue(frequencyToInt(frequency));
                alertDialog.dismiss();
            }
        });
    }

    private String frequencyToString(int frequency) {
        String frequencyString = "";
        switch (frequency) {
            case 1800000:
                frequencyString = "Every Half Hour";
                break;
            case 3600000:
                frequencyString = "Every Hour";
                break;
            case 7200000:
                frequencyString = "Every 2 Hours";
                break;
            case 14400000:
                frequencyString = "Every 4 Hours";
                break;
            case 28800000:
                frequencyString = "Every 8 Hours";
                break;
            case 86400000:
                frequencyString = "Once a Day";
                break;
        }
        return frequencyString;
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

    public void stopNotifications() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }
}
package com.example.uplift;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    Button btnLogOut;
    Button btnSettings;
    TextView welcome;
    String name;
    int frequency;
    String position;
    List<String> selectedCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        welcome = findViewById(R.id.welcome);
        btnLogOut = findViewById(R.id.btnLogOut);
        btnSettings = findViewById(R.id.btnSettings);

        Intent intent = getIntent();
        position = intent.getStringExtra("frequencyString");
        selectedCategories = new ArrayList<>();

        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(),SignInActivity.class));
        }
        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = dataSnapshot.child("name").getValue(String.class);
                frequency = dataSnapshot.child("frequency").getValue(int.class);
                if (name != null) {
                    welcome.setText("Welcome " + name + ",");
                } else {
                    welcome.setText(R.string.welcome_back);
                }

                try {
                    for (DataSnapshot postSnapShot: dataSnapshot.child("categories").getChildren()) {
                        String category = postSnapShot.getValue(String.class);
                        selectedCategories.add(category);
                        Log.e("ERROR", category);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ERROR", "The read failed: " + databaseError.getCode());
            }
        });

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
        Log.e("ERROR", "showUpdateDialog: " + selectedCategories.get(0) );

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.settings_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextUserName = dialogView.findViewById(R.id.editTextUserName);
        editTextUserName.setText(name);

        final Spinner spinnerSettingsFrequency = dialogView.findViewById(R.id.spinnerSettingsFrequency);
        spinnerSettingsFrequency.setSelection(getIndex(spinnerSettingsFrequency, frequencyToString(frequency)));

        final ListView listView = dialogView.findViewById(R.id.settingsContent);

        final List<String> categories = new ArrayList<>();
        Category[] allCategories = Category.getAllCategories();
        for (Category category: allCategories) {
            categories.add(category.getName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, categories);

        listView.setAdapter(dataAdapter);

        for (String category: selectedCategories) {
            int position = categoryToPosition(category);
            listView.setItemChecked(position, true);
//            listView.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.blueTheme));
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SparseBooleanArray clickedItemPositions = listView.getCheckedItemPositions();
                for(int index = 0; index < clickedItemPositions.size();index++){
                    boolean checked = clickedItemPositions.valueAt(index);
                    int key = clickedItemPositions.keyAt(index);
                    String item = (String) listView.getItemAtPosition(key);

                    if (checked) {
                        listView.getChildAt(key).setBackgroundColor(getResources().getColor(R.color.blueTheme));
                        if (!selectedCategories.contains(item)){
                            selectedCategories.add(item);
                        }
                    } else {
                        listView.getChildAt(key).setBackgroundColor(Color.TRANSPARENT);
                        selectedCategories.remove(item);
                    }
                }
            }
        });

        final Button btnUpdate = dialogView.findViewById(R.id.btnUpdate);
        dialogBuilder.setTitle("Update Settings");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextUserName.getText().toString().trim();
                String frequency = spinnerSettingsFrequency.getSelectedItem().toString().trim();

                UserPreference userPreference = new UserPreference(name, frequencyToInt(frequency), selectedCategories);
                databaseReference.setValue(userPreference);
                alertDialog.dismiss();
                // uncomment these once push new categories to database works
//                stopNotifications();
//                restartNotifications();
            }
        });
    }

    private int categoryToPosition(String category) {
        Log.e("ERROR", "categoryToPosition: " + category );
        int position = 0;
        switch (category) {
            case "Animals":
                position = 0;
                break;
            case "Nature":
                position = 1;
                break;
            case "Wellness":
                position = 2;
                break;
            case "Travel":
                position = 3;
                break;
            case "Good News":
                position = 4;
                break;
            case "Exercise":
                position = 5;
                break;
        }
        return position;
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

    public void startAlert() {
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), frequency
                , pendingIntent);
    }

    private void restartNotifications() {
        DatabaseReference databaseRefFreq = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("frequency/");
        databaseRefFreq.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                frequency = dataSnapshot.getValue(int.class);
                startAlert();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ERROR", "The read failed: " + databaseError.getCode());
            }
        });
    }
}
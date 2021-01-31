package com.example.uplift;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    ImageButton btnSettings;
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
                try {
                    frequency = dataSnapshot.child("frequency").getValue(int.class);
                } catch (NullPointerException e) {
                    Log.d("ERROR", "The user is missing user preferences.");
//                    Toast.makeText(MainActivity.this, "Oops, it looks like your account" +
//                            " is missing some information!", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(MainActivity.this, GetNameActivity.class));
                }
                if (name != null) {
                    welcome.setText("Welcome " + name + ",");
                } else {
                    welcome.setText(R.string.welcome_back);
                }

                try {
                    for (DataSnapshot postSnapShot: dataSnapshot.child("categories").getChildren()) {
                        String category = postSnapShot.getValue(String.class);
                        if (!selectedCategories.contains(category)) {
                            selectedCategories.add(category);
                        }
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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, categories) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                String item = categories.get(position);
                if (selectedCategories.contains(item)) {
                    view.setBackgroundColor(getResources().getColor(R.color.semiBlue));
                } else {
                    view.setBackgroundColor(Color.TRANSPARENT);
                }
                return view;
            }
        };

        listView.setAdapter(dataAdapter);

        for (String category: selectedCategories) {
            int position = categoryToPosition(category);
            listView.setItemChecked(position, true);
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
                        listView.getChildAt(key).setBackgroundColor(getResources().getColor(R.color.semiBlue));
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

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rounded);
        alertDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextUserName.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(MainActivity.this, "You must enter a name.", Toast.LENGTH_LONG).show();
                    return;
                }
                String frequency = spinnerSettingsFrequency.getSelectedItem().toString().trim();

                if (selectedCategories.isEmpty()) {
                    Toast.makeText(MainActivity.this, "You must select at least one category.", Toast.LENGTH_LONG).show();
                    return;
                }

                UserPreference userPreference = new UserPreference(name, frequencyToInt(frequency), selectedCategories);
                databaseReference.setValue(userPreference);
                alertDialog.dismiss();
                // reset notifications with new settings
                stopNotifications();
                restartNotifications();
            }
        });
    }

    private int categoryToPosition(String category) {
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
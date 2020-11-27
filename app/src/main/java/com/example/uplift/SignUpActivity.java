package com.example.uplift;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    EditText SignUpMail,SignUpPass;
    Button SignUpButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        SignUpMail = findViewById(R.id.SignUpMail);
        SignUpPass = findViewById(R.id.SignUpPass);
        auth = FirebaseAuth.getInstance();
        SignUpButton = findViewById(R.id.SignUpButton);

        SignUpPass.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        userSignUp();
                        return true;
                    }
                }
                return false;
            }
        });

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSignUp();
            }
        });
    }


    public void userSignUp() {
        String email = SignUpMail.getText().toString();
        String pass = SignUpPass.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(),"Please enter your E-mail address",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(getApplicationContext(),"Please enter your Password",Toast.LENGTH_LONG).show();
        }
        if (pass.length() < 8 ){
            Toast.makeText(getApplicationContext(),"Password must be at least 8 characters",Toast.LENGTH_LONG).show();
        }
        else{
            auth.createUserWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Unable to sign up.",Toast.LENGTH_LONG).show();
                            }
                            else {
                                startActivity(new Intent(SignUpActivity.this, GetNameActivity.class));
                                finish();
                            }
                        }
                    });}
    }

    public void signInScreen(View v){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}
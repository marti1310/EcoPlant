package com.example.ecoplant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecoplant.R;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    private Button btnSignup;
    private Button btnLogin;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            FirebaseApp.initializeApp(this);
            setContentView(R.layout.activity_main);

            Button btnSignup = findViewById(R.id.btnSignup);
            Button btnLogin = findViewById(R.id.btnLogin);

            btnSignup.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            });

            btnLogin.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            });
        }
    }




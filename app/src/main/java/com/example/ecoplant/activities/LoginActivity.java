package com.example.ecoplant.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecoplant.R;
import com.example.ecoplant.repository.UtilisateurRepository;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    private UtilisateurRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        repository = new UtilisateurRepository(this);

        EditText usernameEdit = findViewById(R.id.usernameEdit); // utilisé pour email
        EditText passwordEdit = findViewById(R.id.passwordEdit);
        MaterialButton signinButton = findViewById(R.id.signinButton);
        ImageButton backButton = findViewById(R.id.backButton);
        TextView forgotPasswordText = findViewById(R.id.forgotPasswordText);

        signinButton.setOnClickListener(v -> {
            String email = usernameEdit.getText().toString().trim();
            String password = passwordEdit.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isInternetAvailable()) {
                // Auth Firebase (asynchrone)
                repository.connecterUtilisateurFirebase(email, password, success -> {
                    if (success) {
                        runOnUiThread(this::goToStartActivity);
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, "Identifiants invalides (Firebase)", Toast.LENGTH_SHORT).show());
                    }
                });
            } else {
                // Auth locale (Room) en thread séparé
                new Thread(() -> {
                    boolean success = repository.connecterUtilisateurLocal(email, password);
                    runOnUiThread(() -> {
                        if (success) {
                            goToStartActivity();
                        } else {
                            Toast.makeText(this, "Identifiants invalides (local)", Toast.LENGTH_SHORT).show();
                        }
                    });
                }).start();
            }
        });

        backButton.setOnClickListener(v -> finish());

        forgotPasswordText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ResetpasswordActivity.class);
            startActivity(intent);
        });

    }





    private boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private void goToStartActivity() {
        Intent intent = new Intent(LoginActivity.this, StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}

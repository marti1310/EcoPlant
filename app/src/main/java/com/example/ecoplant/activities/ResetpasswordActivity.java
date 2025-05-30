package com.example.ecoplant.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecoplant.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class ResetpasswordActivity extends AppCompatActivity {

    private EditText emailEditText;
    private MaterialButton resetButton;
    private ImageButton backButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);

        emailEditText = findViewById(R.id.usernameEdit);
        resetButton = findViewById(R.id.resetButton);
        backButton = findViewById(R.id.backButton);
        auth = FirebaseAuth.getInstance();

        resetButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Veuillez saisir votre e-mail", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "E-mail de réinitialisation envoyé", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Échec de l'envoi de l'e-mail", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        backButton.setOnClickListener(v -> finish());
    }
}

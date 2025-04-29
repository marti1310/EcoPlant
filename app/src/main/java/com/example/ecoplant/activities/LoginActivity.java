package com.example.ecoplant.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
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
        signinButton.setOnClickListener(v -> {
            String email = usernameEdit.getText().toString().trim();
            String password = passwordEdit.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            repository.connecterUtilisateur(email, password);
        });

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }
}

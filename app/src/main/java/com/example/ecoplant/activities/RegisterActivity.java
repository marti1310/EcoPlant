package com.example.ecoplant.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecoplant.R;
import com.example.ecoplant.models.Utilisateur;
import com.example.ecoplant.repository.UtilisateurRepository;
import com.google.android.material.button.MaterialButton;

public class RegisterActivity extends AppCompatActivity {

    private UtilisateurRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        repository = new UtilisateurRepository(this);

        EditText usernameEdit = findViewById(R.id.usernameEdit);
        EditText emailEdit = findViewById(R.id.emailEdit);
        EditText passwordEdit = findViewById(R.id.passwordEdit);

        MaterialButton signupButton = findViewById(R.id.signupButton);
        signupButton.setOnClickListener(v -> {
            String username = usernameEdit.getText().toString().trim();
            String email = emailEdit.getText().toString().trim();
            String password = passwordEdit.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            Utilisateur user = new Utilisateur();
            user.setUsername(username);
            user.setEmail(email);
            user.setPasswordHash(password);

            repository.inscrireUtilisateur(user);
        });

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }
}

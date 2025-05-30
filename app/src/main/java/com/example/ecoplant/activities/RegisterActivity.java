package com.example.ecoplant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecoplant.R;
import com.example.ecoplant.models.Utilisateur;
import com.example.ecoplant.repository.UtilisateurRepository;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private UtilisateurRepository repository;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        repository = new UtilisateurRepository(this);
        auth = FirebaseAuth.getInstance();

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

            // Créer l'utilisateur sur Firebase
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null) {
                                // Créer l'objet Utilisateur local avec UID Firebase
                                Utilisateur user = new Utilisateur();
                                user.setUid(firebaseUser.getUid());
                                user.setUsername(username);
                                user.setEmail(email);
                                user.setPasswordHash(password);

                                // Insérer en base locale (Room)
                                repository.inscrireUtilisateur(user);

                                Toast.makeText(RegisterActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();

                                // Redirection vers MainActivity
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(RegisterActivity.this, "Erreur interne, utilisateur non trouvé", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Erreur : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }
}

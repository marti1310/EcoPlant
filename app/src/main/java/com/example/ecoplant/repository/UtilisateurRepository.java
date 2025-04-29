package com.example.ecoplant.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ecoplant.database.AppDatabase;
import com.example.ecoplant.database.UtilisateurDao;
import com.example.ecoplant.models.Utilisateur;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.room.Room;

import java.util.concurrent.Executors;

public class UtilisateurRepository {

    private final UtilisateurDao utilisateurDao;
    private final FirebaseAuth auth;
    private final Context context;

    public UtilisateurRepository(Context context) {
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "ecoplant-db").build();
        this.utilisateurDao = db.utilisateurDao();
        this.auth = FirebaseAuth.getInstance();
        this.context = context;
    }

    public void inscrireUtilisateur(Utilisateur user) {
        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPasswordHash())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        Toast.makeText(context, "Inscription réussie", Toast.LENGTH_SHORT).show();

                        // Insertion Room locale
                        Executors.newSingleThreadExecutor().execute(() -> utilisateurDao.insert(user));

                    } else {
                        Toast.makeText(context, "Erreur : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Inscription", task.getException().toString());
                    }
                });
    }

    public void connecterUtilisateur(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Connexion réussie", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Erreur : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}

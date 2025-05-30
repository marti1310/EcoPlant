package com.example.ecoplant.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
                        if (firebaseUser != null) {
                            String uid = firebaseUser.getUid();
                            user.setUid(uid);  // Met à jour l'UID dans l'objet User

                            // Insertion Room locale avec l'UID correct
                            Executors.newSingleThreadExecutor().execute(() -> {
                                utilisateurDao.insert(user);
                                Log.d("Inscription", "Utilisateur inséré dans Room avec UID : " + uid);
                            });

                            Toast.makeText(context, "Inscription réussie", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("Inscription", "FirebaseUser null après création");
                        }
                    } else {
                        Toast.makeText(context, "Erreur : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Inscription", task.getException().toString());
                    }
                });
    }


    // Connexion en ligne (Firebase)
    public void connecterUtilisateurFirebase(String email, String password, UtilisateurLoginCallback callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    callback.onResult(task.isSuccessful());
                });
    }

    // Connexion locale (Room)
    public boolean connecterUtilisateurLocal(String email, String password) {
        Utilisateur user = utilisateurDao.getUserByEmailAndPassword(email, password);
        return user != null;
    }

    // Callback interface pour retour asynchrone Firebase
    public interface UtilisateurLoginCallback {
        void onResult(boolean success);
    }
}

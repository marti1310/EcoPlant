package com.example.ecoplant.repository;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.example.ecoplant.database.AppDatabase;
import com.example.ecoplant.database.ParcelleDao;
import com.example.ecoplant.models.Parcelle;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class ParcelleRepository {

    private final ParcelleDao parcelleDao;
    private final FirebaseFirestore firestore;
    private final List<Parcelle> cache;

    public ParcelleRepository(Context context) {
        Room Room = null;
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "ecoplant-db").build();
        this.parcelleDao = db.parcelleDao();
        this.firestore = FirebaseFirestore.getInstance();
        this.cache = new ArrayList<>();
    }

    public void insert(Parcelle parcelle) {
        // Cache mémoire
        cache.add(parcelle);

        // Insertion locale
        Executors.newSingleThreadExecutor().execute(() -> parcelleDao.insert(parcelle));

        // Synchro Firebase
        Map<String, Object> data = new HashMap<>();
        data.put("userId", parcelle.getUserId());
        data.put("name", parcelle.getName());
        data.put("location", parcelle.getLocation());
        data.put("creationDate", parcelle.getCreationDate());

        firestore.collection("parcelles")
                .add(data)
                .addOnSuccessListener(doc -> Log.d("Firebase", "Parcelle ajoutée : " + doc.getId()))
                .addOnFailureListener(e -> Log.e("Firebase", "Erreur ajout parcelle", e));
    }

    public List<Parcelle> getCachedParcelles() {
        return cache;
    }
}

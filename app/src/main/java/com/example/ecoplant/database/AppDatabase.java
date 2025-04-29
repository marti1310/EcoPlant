package com.example.ecoplant.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.ecoplant.models.Utilisateur;
import com.example.ecoplant.models.Plante;
import com.example.ecoplant.models.Parcelle;

@Database(
        entities = {Utilisateur.class, Plante.class, Parcelle.class},
        version = 1
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UtilisateurDao utilisateurDao();
    public abstract PlanteDao planteDao();
    public abstract ParcelleDao parcelleDao();
}

package com.example.ecoplant.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.example.ecoplant.converter.ImageListConverter;
import com.example.ecoplant.models.Utilisateur;
import com.example.ecoplant.models.Plante;
import com.example.ecoplant.models.Parcelle;

@Database(
        entities = {Utilisateur.class, Plante.class, Parcelle.class},
        version = 8 // <-- toujours +1 si tu changes le schéma !
)
@TypeConverters({ImageListConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UtilisateurDao utilisateurDao();
    public abstract PlanteDao planteDao();
    public abstract ParcelleDao parcelleDao();
    private static volatile AppDatabase INSTANCE;


    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "ecoplant-db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}

package com.example.ecoplant.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.ecoplant.models.Plante;

import java.util.List;

@Dao
public interface PlanteDao {

    @Insert
    void insert(Plante plante);

    @Update
    void update(Plante plante);

    @Delete
    void delete(Plante plante);

    @Query("SELECT * FROM plantes WHERE id = :id")
    Plante getPlanteById(int id);

    @Query("SELECT * FROM plantes")
    List<Plante> getAllPlantes();

    @Query("SELECT * FROM plantes WHERE userId = :userId")
    List<Plante> getPlantesByUser(String userId);

    // Si tu ajoutes une relation Parcelle <-> Plante
    // @Query("SELECT * FROM plantes WHERE parcelleId = :parcelleId")
    // List<Plante> getPlantesByParcelle(int parcelleId);
}

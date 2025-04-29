package com.example.ecoplant.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.ecoplant.models.Plante;

import java.util.List;

@Dao
public interface PlanteDao {

    @Insert
    void insert(Plante plante);

    @Query("SELECT * FROM plantes WHERE id = :id")
    Plante getPlanteById(int id);

    @Query("SELECT * FROM plantes")
    List<Plante> getAllPlantes();
}

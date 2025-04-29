package com.example.ecoplant.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.ecoplant.models.Parcelle;

import java.util.List;

@Dao
public interface ParcelleDao {

    @Insert
    void insert(Parcelle parcelle);

    @Update
    void update(Parcelle parcelle);

    @Delete
    void delete(Parcelle parcelle);

    @Query("SELECT * FROM parcelles WHERE user_id = :userId")
    List<Parcelle> getParcellesByUser(int userId);

    @Query("SELECT * FROM parcelles")
    List<Parcelle> getAllParcelles();
}

package com.example.ecoplant.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.ecoplant.models.Utilisateur;

import java.util.List;

@Dao
public interface UtilisateurDao {

    @Insert
    void insert(Utilisateur utilisateur);

    @Update
    void update(Utilisateur utilisateur);

    @Delete
    void delete(Utilisateur utilisateur);

    @Query("SELECT * FROM Utilisateurs WHERE email = :email LIMIT 1")
    Utilisateur findByEmail(String email);

    @Query("SELECT * FROM Utilisateurs")
    List<Utilisateur> getAllUtilisateurs();
}

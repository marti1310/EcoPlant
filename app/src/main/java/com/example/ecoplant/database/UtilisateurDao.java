package com.example.ecoplant.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.ecoplant.models.Utilisateur;

import java.util.List;

@Dao
public interface UtilisateurDao {

    @Insert
    void insert(Utilisateur utilisateur);

    @Query("SELECT * FROM utilisateurs WHERE email = :email LIMIT 1")
    Utilisateur findByEmail(String email);

    @Query("SELECT * FROM utilisateurs")
    List<Utilisateur> getAllUtilisateurs();


    @Query("SELECT * FROM utilisateurs WHERE email = :email AND passwordHash = :password LIMIT 3")
    Utilisateur getUserByEmailAndPassword(String email, String password);

    @Query("SELECT * FROM utilisateurs WHERE uid = :uid LIMIT 1")
    Utilisateur findByUid(String uid);
}

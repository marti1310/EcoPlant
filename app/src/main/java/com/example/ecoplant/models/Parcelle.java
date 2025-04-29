package com.example.ecoplant.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "parcelles")
public class Parcelle {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private String name;
    private String location;
    private String creationDate; // format ISO 8601 recommandé (ex : "2025-04-30T14:00")

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}

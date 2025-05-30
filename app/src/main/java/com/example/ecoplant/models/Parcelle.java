package com.example.ecoplant.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.ecoplant.converter.ImageListConverter;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = "parcelles")
@TypeConverters({ImageListConverter.class})
public class Parcelle implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String userId;

    private String name;
    private String location;
    private String creationDate; // ex: "2025-05-29T16:30"
    private String description;
    private ArrayList<String> imagePaths;
    // --- AJOUTE CES CHAMPS POUR LES SCORES ---
    private double scoreSoilStructure;
    private double scoreWaterRetention;
    private double scoreNitrogenFixation;

    // --- Getters et setters de base (déjà présents ?) ---
    // ...

    public double getScoreSoilStructure() {
        return scoreSoilStructure;
    }
    public void setScoreSoilStructure(double scoreSoilStructure) {
        this.scoreSoilStructure = scoreSoilStructure;
    }

    public double getScoreWaterRetention() {
        return scoreWaterRetention;
    }
    public void setScoreWaterRetention(double scoreWaterRetention) {
        this.scoreWaterRetention = scoreWaterRetention;
    }

    public double getScoreNitrogenFixation() {
        return scoreNitrogenFixation;
    }
    public void setScoreNitrogenFixation(double scoreNitrogenFixation) {
        this.scoreNitrogenFixation = scoreNitrogenFixation;
    }
    // --- Constructeurs ---
    public Parcelle() {}

    public Parcelle(String userId, String name, String location, String creationDate, ArrayList<String> imagePaths, String description) {
        this.userId = userId;
        this.name = name;
        this.location = location;
        this.creationDate = creationDate;
        this.imagePaths = imagePaths;
        this.description = description;
    }

    // --- Getters et setters ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCreationDate() { return creationDate; }
    public void setCreationDate(String creationDate) { this.creationDate = creationDate; }

    public ArrayList<String> getImagePaths() { return imagePaths; }
    public void setImagePaths(ArrayList<String> imagePaths) { this.imagePaths = imagePaths; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

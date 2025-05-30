package com.example.ecoplant.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "plantes")
public class Plante {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String userId;
    private String speciesName;
    private String photoPath;
    private String creationDate; // Ajouté : date d’ajout/prise de photo

    private double scoreNitrogenFixation;
    private double scoreSoilStructure;
    private double scoreWaterRetention;

    // ----- Getters & Setters -----
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSpeciesName() {
        return speciesName;
    }
    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public String getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public double getScoreNitrogenFixation() {
        return scoreNitrogenFixation;
    }
    public void setScoreNitrogenFixation(double scoreNitrogenFixation) {
        this.scoreNitrogenFixation = scoreNitrogenFixation;
    }

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
    public String getPhotoPath() {
        return photoPath;
    }
    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}

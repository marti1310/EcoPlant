package com.example.ecoplant.models;

import androidx.room.Entity;

@Entity(primaryKeys = {"parcelleId", "planteId"})
public class ParcellePlanteCrossRef {
    public int parcelleId;
    public int planteId;
}

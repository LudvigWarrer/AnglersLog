package com.example.benjamin.crudtest;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Benjamin on 22-10-2017.
 */

@IgnoreExtraProperties
class Fish {
    private String fishId;
    private String fishName;
    private String weight;
    private double latitude;
    private double longitude;
    private String fileName;

    public Fish(){
        // This constructor is required!
    }

    Fish(String fishId, String fishName, String weight, double latitude, double longitude, String fileName) {
        this.fishId = fishId;
        this.fishName = fishName;
        this.weight = weight;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fileName = fileName;
    }

    String getFishId() {
        return fishId;
    }

    String getFishName() {
        return fishName;
    }

    String getWeight() {
        return weight;
    }

    double getLatitude() {
        return latitude;
    }

    double getLongitude(){
        return longitude;
    }

    String getFileName(){
        return fileName;
    }
}



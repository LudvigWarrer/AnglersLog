package com.example.benjamin.crudtest;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Benjamin on 22-10-2017.
 */

@IgnoreExtraProperties
public class Fish {
    private String fishId;
    private String fishName;
    private String weight;
    private double latitude;
    private double longitude;
    private String fileName;

    public Fish(){
        // This constructor is required!
    }

    public Fish(String fishId , String fishName, String weight, double latitude, double longitude, String fileName) {
        this.fishId = fishId;
        this.fishName = fishName;
        this.weight = weight;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fileName = fileName;
    }

    public String getFishId() {
        return fishId;
    }

    public String getFishName() {
        return fishName;
    }


    public String getWeight() {
        return weight;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public String getFileName(){
        return fileName;
    }
}



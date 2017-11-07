package com.example.benjamin.crudtest;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Benjamin on 22-10-2017.
 */

@IgnoreExtraProperties
public class Fish {
    private String fishId;
    private String fishName;
    private String weight;

    public Fish(){
        // This constructor is required!
    }

    public Fish(String fishId , String fishName, String weight) {
        this.fishId = fishId;
        this.fishName = fishName;
        this.weight = weight;
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
}



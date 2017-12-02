package com.berstek.veripay.models;


import com.google.firebase.database.Exclude;

public class Contact {

    @Exclude
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

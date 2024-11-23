package com.example.localcrimeeye;

public class CrimeLocation {
    private String name;
    private String description;

    public CrimeLocation() {
        // Default constructor required for calls to DataSnapshot.getValue(CrimeLocation.class)
    }

    public CrimeLocation(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
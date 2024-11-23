package com.example.localcrimeeye;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class User {
    private String name;
    private String phone;
    private double latitude;
    private double longitude;
    private String timestamp; // Store timestamp as a string
    private boolean isNewLocation;
    private Date timeStamp; // Store date of the report

    // Default constructor required for Firebase
    public User() {
    }

    // Constructor with isNewLocation default to false for new users
    public User(String name, String phone, double latitude, double longitude) {
        this(name, phone, latitude, longitude, false); // Use constructor chaining
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()); // Set current timestamp
    }

    // Constructor allowing manual setting of isNewLocation
    public User(String name, String phone, double latitude, double longitude, boolean isNewLocation) {
        this.name = name;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isNewLocation = isNewLocation;  // Use isNewLocation for clarity
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()); // Set current timestamp
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Date getTimeStamp() {
        return timeStamp; // Return report date
    }

    // Method to return latitude and longitude as a single formatted string
    public String getLocation() {
        return String.format("%.6f, %.6f", latitude, longitude);  // Latitude and longitude formatted to 6 decimal places
    }

    public boolean isNewLocation() {
        return isNewLocation;  // Renamed for clarity
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setNewLocation(boolean isNewLocation) {
        this.isNewLocation = isNewLocation;  // Renamed setter for clarity
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp; // Set report date
    }
}

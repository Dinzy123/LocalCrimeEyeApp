package com.example.localcrimeeye;

public class HelperClass {
    private String name;
    private String email;
    private String username;
    private String password;
    private String phone;
    private Double latitude;
    private Double longitude;

    // Constructor
    public HelperClass(String name, String email, String username, String password, String phone, Double latitude, Double longitude) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}

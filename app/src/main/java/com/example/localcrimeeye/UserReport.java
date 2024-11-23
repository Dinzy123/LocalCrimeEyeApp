package com.example.localcrimeeye;

public class UserReport {
    private String username;
    private String reportDetails;
    private double latitude;
    private double longitude;

    private String phone;

    public UserReport() {
        // Default constructor required for calls to DataSnapshot.getValue(UserReport.class)
    }

    public UserReport(String username, String reportDetails, double latitude, double longitude, String phone) {
        this.username = username;
        this.reportDetails = reportDetails;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public String getReportDetails() {
        return reportDetails;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getPhone() {
        return phone;
    }
}


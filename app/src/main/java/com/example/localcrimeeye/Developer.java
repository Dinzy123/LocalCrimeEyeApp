package com.example.localcrimeeye;

public class Developer {
    private String name;
    private String email;
    private int imageResId; // Resource ID for the drawable image
    private String portfolioUrl;

    public Developer(String name, String email, int imageResId, String portfolioUrl) {
        this.name = name;
        this.email = email;
        this.imageResId = imageResId;
        this.portfolioUrl = portfolioUrl;
    }

    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public int getImageResId() { return imageResId; }
    public String getPortfolioUrl() { return portfolioUrl; }
}

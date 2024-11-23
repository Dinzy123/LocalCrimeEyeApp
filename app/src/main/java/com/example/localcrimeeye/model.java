package com.example.localcrimeeye;

public class model {
    String name, course, email, location; // Change 'purl' to 'location'

    model() {
    }

    public model(String name, String course, String email, String location) { // Update constructor
        this.name = name;
        this.course = course;
        this.email = email;
        this.location = location; // Set location
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location; // Getter for location
    }

    public void setLocation(String location) {
        this.location = location; // Setter for location
    }
}

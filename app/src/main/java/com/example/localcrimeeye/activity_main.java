package com.example.localcrimeeye;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class activity_main extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String currentTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load the saved theme from SharedPreferences
        sharedPreferences = getSharedPreferences("AppSettingsPrefs", MODE_PRIVATE);
        currentTheme = sharedPreferences.getString("theme", "light");

        // Apply the current theme before calling setContentView
        applyTheme(currentTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView localNewsTextView = findViewById(R.id.Local_News);
        TextView reportedCrime = findViewById(R.id.reported_crime);
        TextView userProfile = findViewById(R.id.profile);
        TextView Meeting = findViewById(R.id.Meetings);
        TextView popularCases = findViewById(R.id.textView8);
        ImageView comments = findViewById(R.id.imageView6);
        ImageView members = findViewById(R.id.imageView7);
        ImageView settings = findViewById(R.id.imageView9);

        //the 4 images

        ImageView reports_img = findViewById(R.id.imageViewButton1);
        ImageView news_img = findViewById(R.id.imageViewButton2);
        ImageView meetings_img = findViewById(R.id.imageViewButton3);
        ImageView developers_img = findViewById(R.id.imageViewButton4);

        //images holder buttons
        reports_img.setOnClickListener(v -> {
            Intent intent = new Intent(activity_main.this, CardActivity.class);
            startActivity(intent);
        });

        news_img.setOnClickListener(v -> {
            Intent intent = new Intent(activity_main.this, NewsActivity.class);
            startActivity(intent);
        });

        meetings_img.setOnClickListener(v -> {
            Intent intent = new Intent(activity_main.this, MeetingActivity.class);
            startActivity(intent);
        });

        developers_img.setOnClickListener(v -> {
            Intent intent = new Intent(activity_main.this, DevelopersActivity.class);
            startActivity(intent);
        });


        // Test holder buttons
        localNewsTextView.setOnClickListener(v -> {
            Intent intent = new Intent(activity_main.this, NewsActivity.class);
            startActivity(intent);
        });

        reportedCrime.setOnClickListener(v -> {
            Intent intent = new Intent(activity_main.this, CardActivity.class);
            startActivity(intent);
        });

        userProfile.setOnClickListener(v -> {
            Intent intent = new Intent(activity_main.this, DevelopersActivity.class);
            startActivity(intent);
        });

        Meeting.setOnClickListener(v -> {
            Intent intent = new Intent(activity_main.this, MeetingActivity.class);
            startActivity(intent);
        });

        popularCases.setOnClickListener(v -> {
            Intent intent = new Intent(activity_main.this, NewsActivity.class);
            startActivity(intent);
        });

        comments.setOnClickListener(v -> openWebsite("https://forms.visme.co/formsPlayer/g7qn11p0-feedback-form"));

        members.setOnClickListener(v -> {
            Intent intent = new Intent(activity_main.this, ProfileActivity.class);
            startActivity(intent);
        });

        settings.setOnClickListener(v -> {
            Intent intent = new Intent(activity_main.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    // Method to apply the selected theme
    private void applyTheme(String selectedTheme) {
        if ("dark".equals(selectedTheme)) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if the theme was changed in the SettingsActivity
        String newTheme = sharedPreferences.getString("theme", "light");

        // Only recreate the activity if the theme has actually changed
        if (!newTheme.equals(currentTheme)) {
            currentTheme = newTheme;
            applyTheme(newTheme);
            recreate();
        }
    }

    // Method to open a website in the browser
    private void openWebsite(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}

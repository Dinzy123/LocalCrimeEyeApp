package com.example.localcrimeeye;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

public class AddMemberActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
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
        setContentView(R.layout.activity_add_member);

        TextView contactDeveloperText = findViewById(R.id.loginRedirectText);
        TextView backlogin = findViewById(R.id.logindirect);

        backlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMemberActivity.this, activity_main.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for the TextView to open the website
        contactDeveloperText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebsite("https://dinzy123.github.io/CodeWaveKings-Portfolio/");
            }
        });
    }

    // Method to open the website
    private void openWebsite(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

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
            recreate();  // This will recreate the activity with the new theme
        }
    }
}

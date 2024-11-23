package com.example.localcrimeeye;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

public class SettingsActivity extends AppCompatActivity {

    private Switch notificationSwitch;
    private RadioGroup themeGroup;
    private RadioButton lightTheme;
    private RadioButton darkTheme;
    private Button signOutButton;
    private Button aboutButton;
    private Button feedbackButton;
    private Button resetSettingsButton;
    private ImageView logoImage;

    // SharedPreferences for storing app settings
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply the theme before setting the content view
        sharedPreferences = getSharedPreferences("AppSettingsPrefs", MODE_PRIVATE);
        String theme = sharedPreferences.getString("theme", "light");
        if (theme != null && theme.equals("dark")) {
            setTheme(R.style.DarkTheme);  // Apply dark theme if chosen
        } else {
            setTheme(R.style.LightTheme); // Default to light theme
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize the UI elements
        notificationSwitch = findViewById(R.id.notifications_switch);
        themeGroup = findViewById(R.id.radio_theme_group);
        lightTheme = findViewById(R.id.radio_light);
        darkTheme = findViewById(R.id.radio_dark);
        signOutButton = findViewById(R.id.sign_out_button);
        aboutButton = findViewById(R.id.about_button);
        feedbackButton = findViewById(R.id.feedback_button);
        resetSettingsButton = findViewById(R.id.reset_settings_button);
        logoImage = findViewById(R.id.logo_image); // Initialize the logo ImageView

        // Initialize SharedPreferences
        editor = sharedPreferences.edit();

        // Load saved settings
        loadSettings();

        // Notification Switch Listener
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("notifications_enabled", isChecked);
            editor.apply();
            Toast.makeText(SettingsActivity.this, "Notifications " + (isChecked ? "Enabled" : "Disabled"), Toast.LENGTH_SHORT).show();
        });

        // Theme Selection Listener
        themeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_light) {
                editor.putString("theme", "light");
                applyTheme("light");
            } else if (checkedId == R.id.radio_dark) {
                editor.putString("theme", "dark");
                applyTheme("dark");
            }
            editor.apply();
        });

        // Sign Out Button Listener
        signOutButton.setOnClickListener(v -> signOutUser());

        // About Button Listener
        aboutButton.setOnClickListener(v -> showAboutDialog());

        // Feedback Button Listener
        feedbackButton.setOnClickListener(v -> sendFeedback());


    }

    // Method to load saved settings
    private void loadSettings() {
        // Load notification setting
        boolean notificationsEnabled = sharedPreferences.getBoolean("notifications_enabled", true);
        notificationSwitch.setChecked(notificationsEnabled);

        // Load theme setting
        String theme = sharedPreferences.getString("theme", "light");
        if (theme != null) {
            if (theme.equals("light")) {
                lightTheme.setChecked(true);
            } else {
                darkTheme.setChecked(true);
            }
            applyTheme(theme);  // Apply the current theme
        }
    }

    // Method to apply theme
    private void applyTheme(String selectedTheme) {
        String currentTheme = sharedPreferences.getString("theme", "light");
        if (!selectedTheme.equals(currentTheme)) {
            editor.putString("theme", selectedTheme);
            editor.apply();
            recreate();  // Only recreate the activity if the theme is different
        }

        // Change button text color based on the theme
        if (selectedTheme.equals("dark")) {
            signOutButton.setTextColor(getResources().getColor(android.R.color.white));
            aboutButton.setTextColor(getResources().getColor(android.R.color.white));
            feedbackButton.setTextColor(getResources().getColor(android.R.color.white));
            resetSettingsButton.setTextColor(getResources().getColor(android.R.color.white));
        } else {
            // Set to default color (black) for light theme
            signOutButton.setTextColor(getResources().getColor(android.R.color.black));
            aboutButton.setTextColor(getResources().getColor(android.R.color.black));
            feedbackButton.setTextColor(getResources().getColor(android.R.color.black));
            resetSettingsButton.setTextColor(getResources().getColor(android.R.color.black));
        }
    }

    // Override onBackPressed to ensure the selected theme is applied
    @Override
    public void onBackPressed() {
        // Check the currently selected theme
        String selectedTheme = sharedPreferences.getString("theme", "light");

        // Apply the theme when going back
        if (selectedTheme.equals("dark")) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }

        super.onBackPressed();  // Call the default back action
    }

    // Method to sign out the user
    private void signOutUser() {
        // Logic to sign out the user (Firebase Auth or others)
        // FirebaseAuth.getInstance().signOut(); // Example Firebase sign-out code

        // Return to login activity or welcome screen
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();  // Close current activity
    }

    // Method to show the about dialog
    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About App")
                .setMessage("Version 1.0\nDeveloped by Sanele Mkhabela\nPrivacy Policy: [Link]")
                .setPositiveButton("OK", null)
                .create()
                .show();
    }

    // Method to send feedback via email
    private void sendFeedback() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:")); // Only email apps should handle this
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"saneledinzy123@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for Forum Crime Eye");
        startActivity(Intent.createChooser(emailIntent, "Send Feedback"));
    }

}

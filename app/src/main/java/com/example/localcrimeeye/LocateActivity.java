package com.example.localcrimeeye;

import static androidx.core.graphics.drawable.DrawableCompat.applyTheme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;



import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LocateActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private double latitude;
    private double longitude;
    private String userName;
    private String phoneNumber;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private String currentTheme;
    private ImageView imageView5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply theme before calling setContentView
        sharedPreferences = getSharedPreferences("AppSettingsPrefs", MODE_PRIVATE);
        currentTheme = sharedPreferences.getString("theme", "light");

        applyTheme(currentTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.locate);

        mapView = findViewById(R.id.mapView);
        TextView locationLabel = findViewById(R.id.location_label);
        Button callButton = findViewById(R.id.call_button);
        Button sendMessageButton = findViewById(R.id.send_message_button);
        ImageView home = findViewById(R.id.imageView5);
        ImageView comments = findViewById(R.id.imageView6);
        ImageView members = findViewById(R.id.imageView7);
        ImageView settings = findViewById(R.id.imageView9);

        home.setOnClickListener(v -> {
            Intent intent = new Intent(LocateActivity.this, activity_main.class);
            startActivity(intent);
        });

        comments.setOnClickListener(v -> openWebsite("https://forms.visme.co/formsPlayer/g7qn11p0-feedback-form"));

        members.setOnClickListener(v -> {
            Intent intent = new Intent(LocateActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        settings.setOnClickListener(v -> {
            Intent intent = new Intent(LocateActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude", 0.0);
        longitude = intent.getDoubleExtra("longitude", 0.0);
        userName = intent.getStringExtra("name");

        String locationText = "Location: " + latitude + ", " + longitude;
        locationLabel.setText(locationText);

        fetchPhoneNumber(userName);

        callButton.setOnClickListener(v -> {
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);
            } else {
                Toast.makeText(LocateActivity.this, "Ooops!, user is missing phone number.", Toast.LENGTH_SHORT).show();
            }
        });

        sendMessageButton.setOnClickListener(v -> {
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
                smsIntent.putExtra("sms_body", "Hello! We have your location and we are coming to help you now, so stay calm! \n\n From the Forum");
                startActivity(smsIntent);
            } else {
                Toast.makeText(LocateActivity.this, "Ooops!, user is missing phone number.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap gMap) {
        LatLng userLocation = new LatLng(latitude, longitude);
        gMap.addMarker(new MarkerOptions().position(userLocation).title(userName));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f));
    }

    private void fetchPhoneNumber(String userName) {
        // Query to find the user by their name
        databaseReference.orderByChild("name").equalTo(userName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Assuming "phone" is directly under the user node
                                phoneNumber = snapshot.child("phone").getValue(String.class);
                                if (phoneNumber != null) {
                                    Toast.makeText(LocateActivity.this, "Phone: " + phoneNumber, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LocateActivity.this, "Ooops!, user is missing phone number.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(LocateActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(LocateActivity.this, "Error fetching phone number", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openWebsite(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
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

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}

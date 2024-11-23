package com.example.localcrimeeye;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap myMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView locationLabel;
    private Button emergencyButton;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationLabel = view.findViewById(R.id.location_label);
        emergencyButton = view.findViewById(R.id.emergency_button);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        // Retrieve the location data from the arguments
        Bundle args = getArguments();
        if (args != null) {
            String location = args.getString("location");
            if (location != null) {
                locationLabel.setText(location);
            } else {
                locationLabel.setText("Location not provided.");
            }
        } else {
            locationLabel.setText("No arguments provided.");
        }

        // Set up the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.locationTextView);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        myMap.setMyLocationEnabled(true);

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                updateUIWithLocation(location);
            } else {
                locationLabel.setText("Unable to get current location.");
                Log.e("HomeFragment", "Failed to retrieve location.");
            }
        }).addOnFailureListener(e -> Log.e("HomeFragment", "Location error: " + e.getMessage()));
    }

    private void updateUIWithLocation(Location location) {
        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
        myMap.addMarker(new MarkerOptions().position(currentLatLng).title("You are here"));

        // Update the location label
        String locationText = "Lat: " + location.getLatitude() + ", Lng: " + location.getLongitude();
        locationLabel.setText(locationText);
    }

    private void sendLocationToFirebase() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                // Generate a unique ID for the location
                String locationId = databaseReference.push().getKey();

                if (locationId != null) {
                    // Create an object to store the location
                    EmergencyLocation emergencyLocation = new EmergencyLocation(location.getLatitude(), location.getLongitude());

                    // Save to Firebase
                    databaseReference.child(locationId).setValue(emergencyLocation)
                            .addOnSuccessListener(aVoid -> {
                                // Successfully saved location
                                locationLabel.setText("Location sent.");
                            })
                            .addOnFailureListener(e -> {
                                // Failed to save location
                                locationLabel.setText("Failed to send location.");
                                Log.e("HomeFragment", "Firebase error: " + e.getMessage());
                            });
                }
            } else {
                locationLabel.setText("Unable to send location.");
                Log.e("HomeFragment", "Failed to retrieve location.");
            }
        }).addOnFailureListener(e -> Log.e("HomeFragment", "Location error: " + e.getMessage()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onMapReady(myMap);
        }
    }

    public static class EmergencyLocation {
        public double latitude;
        public double longitude;

        public EmergencyLocation() {
            // Default constructor required for calls to DataSnapshot.getValue(EmergencyLocation.class)
        }

        public EmergencyLocation(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}
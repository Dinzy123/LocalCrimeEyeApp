package com.example.localcrimeeye;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseBackgroundService extends Service {

    private DatabaseReference databaseReference;
    private static final String CHANNEL_ID = "Channel_id_notification";
    private SharedPreferences sharedPreferences;

    @Override
    public IBinder onBind(Intent intent) {
        return null; // We don't need to bind this service to an activity
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Initialize SharedPreferences to store and retrieve last known locations
        sharedPreferences = getSharedPreferences("location_prefs", MODE_PRIVATE);

        // Add Firebase listener for data changes
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        String userId = snapshot.getKey();
                        double latitude = user.getLatitude();
                        double longitude = user.getLongitude();

                        // Check if latitude and longitude are valid (non-zero values)
                        if (latitude != 0.0 && longitude != 0.0 && isNewLocation(userId, latitude, longitude)) {
                            // Save new location and send a notification
                            saveLocation(userId, latitude, longitude);
                            sendNotification(user.getName() + " added a new location.");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });

        return START_STICKY;
    }

    // Method to check if the location is new
    private boolean isNewLocation(String userId, double latitude, double longitude) {
        String lastLocation = sharedPreferences.getString(userId, null);
        String currentLocation = latitude + "," + longitude;

        // Compare with the last stored location
        return lastLocation == null || !lastLocation.equals(currentLocation);
    }

    // Method to save the new location
    private void saveLocation(String userId, double latitude, double longitude) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(userId, latitude + "," + longitude); // Save new location
        editor.apply();
    }

    // Method to send the notification
    private void sendNotification(String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    "Emergency Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_person_24)
                .setContentTitle("New Report! Hurry.")
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        notificationManager.notify(0, builder.build());
    }
}

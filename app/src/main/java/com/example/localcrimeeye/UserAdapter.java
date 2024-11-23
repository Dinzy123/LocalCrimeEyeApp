package com.example.localcrimeeye;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<User> userList;
    private final Context context;
    private static final String CHANNEL_ID = "LocationReceivedChannel";
    private static final int NOTIFICATION_ID = 123;

    public UserAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
        createNotificationChannel();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        // Bind user data to views
        holder.nameTextView.setText(user.getName());
        holder.phoneTextView.setText(user.getPhone());

        // Format and display latitude and longitude as location
        String location = "Loc: " + user.getLatitude() + ", " + user.getLongitude();
        holder.locationTextView.setText(location);

        // Highlight the card if the report is marked as new
        if (user.isNewLocation()) {
            holder.itemView.setBackgroundColor(Color.YELLOW);  // Highlight with yellow color
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);  // Default white background
        }

        // Set click listeners for buttons
        holder.acceptButton.setOnClickListener(v -> {
            // Navigate to LocateActivity with latitude, longitude, and user's name
            Intent intent = new Intent(context, LocateActivity.class);
            intent.putExtra("latitude", user.getLatitude());
            intent.putExtra("longitude", user.getLongitude());
            intent.putExtra("name", user.getName());
            intent.putExtra("phone", user.getPhone());
            context.startActivity(intent);

            // Show notification
            showLocationReceivedNotification(user.getName(), location, user.getLatitude(), user.getLongitude());

            // Mark the user card as no longer new
            user.setNewLocation(false);
            notifyItemChanged(position);
        });

        holder.declineButton.setOnClickListener(v -> {
            // Remove user from the list, prevent IndexOutOfBoundsException
            if (position >= 0 && position < userList.size()) {
                userList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, userList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, locationTextView, phoneTextView;
        Button acceptButton, declineButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nametext);
            phoneTextView = itemView.findViewById(R.id.phonetext);
            locationTextView = itemView.findViewById(R.id.locationtext);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            declineButton = itemView.findViewById(R.id.declineButton);
        }
    }

    private void showLocationReceivedNotification(String userName, String location, double latitude, double longitude) {
        // Create an intent for the notification click action (open LocateActivity)
        Intent intent = new Intent(context, LocateActivity.class);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("name", userName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // PendingIntent for notification click action
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.homelogo)
                .setContentTitle("Location Received")
                .setContentText("Location received for " + userName + ": " + location)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true) // Notification is dismissed when clicked
                .setContentIntent(pendingIntent); // Attach the pending intent

        // Show the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    private void createNotificationChannel() {
        // Create a notification channel for Android Oreo and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Location Received";
            String description = "Notification for location received";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}

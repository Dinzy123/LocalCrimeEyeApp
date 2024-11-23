package com.example.localcrimeeye;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private DatabaseReference databaseReference;
    private static final int ITEMS_PER_PAGE = 10; // Items per page
    private int currentPage = 0;
    private Button nextButton;
    private Button prevButton;
    private SharedPreferences sharedPreferences;
    private String currentTheme;
    private String lastKey = null; // Track last key for pagination

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Load the saved theme from SharedPreferences
        sharedPreferences = getSharedPreferences("AppSettingsPrefs", MODE_PRIVATE);
        currentTheme = sharedPreferences.getString("theme", "light");

        // Apply the current theme before calling setContentView
        applyTheme(currentTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_controller);

        recyclerView = findViewById(R.id.recview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, this);
        recyclerView.setAdapter(userAdapter);

        // Set up Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Set up pagination buttons
        nextButton = findViewById(R.id.nextButton);
        prevButton = findViewById(R.id.prevButton);

        nextButton.setOnClickListener(v -> {
            currentPage++;
            loadReports(currentPage);
        });

        prevButton.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--;
                loadReports(currentPage);
            }
        });

        // Load initial reports
        loadReports(currentPage);
    }

    private void loadReports(int page) {
        Query query;
        if (lastKey == null) {
            query = databaseReference.orderByKey().limitToFirst(ITEMS_PER_PAGE);
        } else {
            query = databaseReference.orderByKey().startAfter(lastKey).limitToFirst(ITEMS_PER_PAGE);
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear(); // Clear the list before adding new data
                int userCount = 0;
                String tempLastKey = null;

                // Get the current date for comparison
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String todayDate = sdf.format(new Date()); // Today's date

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    userCount++;
                    tempLastKey = snapshot.getKey(); // Track last key for pagination

                    User user = snapshot.getValue(User.class); // Deserialize into User object
                    if (user != null) {
                        String timestamp = snapshot.child("timestamp").getValue(String.class);
                        if (timestamp != null) {
                            // Extract only the date part from the timestamp (assuming it's in ISO format)
                            String reportDate = timestamp.split(" ")[0];

                            // Check if the report was made today
                            if (todayDate.equals(reportDate)) {
                                user.setNewLocation(true); // Mark as new report (made today)
                            } else {
                                user.setNewLocation(false); // Mark as old report
                            }

                            userList.add(user); // Add user to the list
                        }
                    }
                }

                lastKey = tempLastKey; // Update the last key for pagination

                userAdapter.notifyDataSetChanged(); // Notify the adapter to refresh the RecyclerView

                // Enable/disable pagination buttons based on data availability
                prevButton.setEnabled(currentPage > 0);
                nextButton.setEnabled(userCount == ITEMS_PER_PAGE); // Enable next if there are enough users
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("CardActivity", "Database error: " + databaseError.getMessage());
            }
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
}

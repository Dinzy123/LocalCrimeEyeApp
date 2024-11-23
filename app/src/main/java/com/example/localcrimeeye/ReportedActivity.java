package com.example.localcrimeeye;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ReportedActivity extends AppCompatActivity {

    private Button btnRequestAssistance;
    private Button btnScheduleMeeting;
    private TextView tvReportsCount;
    private TextView tvResolvedCount;
    private TextView tvReportsChange;
    private RecyclerView rvCrimeLocations;
    private RecyclerView rvUserReports;

    private CrimeLocationsAdapter crimeLocationsAdapter;
    private UserReportsAdapter userReportsAdapter;
    private List<CrimeLocation> crimeLocationList;
    private List<UserReport> userReportList;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reported);

        initViews();
        setupRecyclerViews();
        setupButtonListeners();
        loadCrimeStatistics();
        loadDataFromFirebase();
    }

    private void initViews() {
        rvCrimeLocations = findViewById(R.id.rvCrimeLocations);
        rvUserReports = findViewById(R.id.rvUserReports);
        btnRequestAssistance = findViewById(R.id.btnRequestAssistance);
        btnScheduleMeeting = findViewById(R.id.btnScheduleMeeting);
        tvReportsCount = findViewById(R.id.tvReportsCount);
        tvResolvedCount = findViewById(R.id.tvResolvedCount);
        tvReportsChange = findViewById(R.id.tvReportsChange);
    }

    private void setupRecyclerViews() {
        crimeLocationList = new ArrayList<>();
        userReportList = new ArrayList<>();

        crimeLocationsAdapter = new CrimeLocationsAdapter(crimeLocationList);
        userReportsAdapter = new UserReportsAdapter(userReportList);

        rvCrimeLocations.setLayoutManager(new LinearLayoutManager(this));
        rvCrimeLocations.setAdapter(crimeLocationsAdapter);

        rvUserReports.setLayoutManager(new LinearLayoutManager(this));
        rvUserReports.setAdapter(userReportsAdapter);
    }

    private void setupButtonListeners() {
        btnRequestAssistance.setOnClickListener(v -> requestAssistance());
        btnScheduleMeeting.setOnClickListener(v -> scheduleMeeting());
    }

    private void requestAssistance() {
        Toast.makeText(this, "Assistance requested", Toast.LENGTH_SHORT).show();
    }

    private void scheduleMeeting() {
        Toast.makeText(this, "Meeting scheduled", Toast.LENGTH_SHORT).show();
    }

    private void loadCrimeStatistics() {
        // Example data for statistics; replace with dynamic data if needed
        tvReportsCount.setText("56");
        tvResolvedCount.setText("34");
        tvReportsChange.setText("+12%");
    }

    private void loadDataFromFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Load crime locations
        databaseReference.child("crimeLocations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                crimeLocationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CrimeLocation crimeLocation = snapshot.getValue(CrimeLocation.class);
                    if (crimeLocation != null) {
                        crimeLocationList.add(crimeLocation);
                    }
                }
                crimeLocationsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReportedActivity.this, "Failed to load crime locations.", Toast.LENGTH_SHORT).show();
            }
        });

        // Load user reports
        databaseReference.child("userReports").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userReportList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserReport userReport = snapshot.getValue(UserReport.class);
                    if (userReport != null) {
                        userReportList.add(userReport);
                    }
                }
                userReportsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReportedActivity.this, "Failed to load user reports.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

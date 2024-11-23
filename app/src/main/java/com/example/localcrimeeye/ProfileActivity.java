package com.example.localcrimeeye;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView membersRecyclerView;
    private MembersAdapter membersAdapter;
    private List<Member> memberList;

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
        setContentView(R.layout.activity_profile);

        membersRecyclerView = findViewById(R.id.membersRecyclerView);
        Button addMemberButton = findViewById(R.id.addMemberButton);

        // Sample members
        memberList = new ArrayList<>();
        memberList.add(new Member("Thabani Ngcobo", "thabaningcobo22@gmail.com"));
        memberList.add(new Member("Jane Smith", "jane34@gmail.com"));
        memberList.add(new Member("Mlungisi Dinangwe", "dinangwe@gmail.com"));



        // Set up the RecyclerView
        membersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        membersAdapter = new MembersAdapter(memberList);
        membersRecyclerView.setAdapter(membersAdapter);

        // Handle add member button click
        addMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, AddMemberActivity.class);
                startActivity(intent);
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

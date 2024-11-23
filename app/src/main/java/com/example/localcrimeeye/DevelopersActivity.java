package com.example.localcrimeeye;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class DevelopersActivity extends AppCompatActivity {

    private RecyclerView developersRecyclerView;
    private DeveloperAdapter developerAdapter;
    private List<Developer> developerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers);

        developersRecyclerView = findViewById(R.id.developersRecyclerView);
        developersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the developer list with local drawable images
        developerList = new ArrayList<>();
        developerList.add(new Developer("Sanele Mkhabela", "saneledinzy123@gmail.com", R.drawable.my_pic, "https://dinzy123.github.io/CodeWaveKings-Portfolio/"));
        developerList.add(new Developer("Nonkululeko Guma (Co-Founder)", "nonkululekoguma97@gmail.com", R.drawable.nonku_pic, "https://www.telkomlearn.co.za/mycourses"));
        // Add more developers here

        // Set up the adapter
        developerAdapter = new DeveloperAdapter(this, developerList);
        developersRecyclerView.setAdapter(developerAdapter);
    }
}

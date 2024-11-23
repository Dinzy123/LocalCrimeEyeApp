package com.example.localcrimeeye;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        WebView newsWebView = findViewById(R.id.newsWebView);
        ImageButton backButton = findViewById(R.id.backButton);

        // Configure WebView
        WebSettings webSettings = newsWebView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JavaScript if needed
        webSettings.setDomStorageEnabled(true);  // Enable DOM storage if needed
        newsWebView.setWebViewClient(new WebViewClient()); // Handle navigation within the WebView

        // Load URL
        newsWebView.loadUrl("https://www.citizen.co.za/local-newspapers/#kwazulu-natal"); // Replace with your news URL

        // Back button functionality
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the current activity and return to the previous one
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Close the current activity and return to the previous one
        super.onBackPressed();
    }
}

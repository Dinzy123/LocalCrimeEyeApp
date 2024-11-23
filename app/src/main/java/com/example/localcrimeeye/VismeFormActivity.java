package com.example.localcrimeeye;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class VismeFormActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visme_form);

        // Initialize WebView
        webView = findViewById(R.id.visme_form_webview);

        // Enable JavaScript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Enable debugging for WebView (only for development)
        WebView.setWebContentsDebuggingEnabled(true);

        // Allow mixed content (optional, only if needed)
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        // Ensure WebView stays within the app and not open in a browser
        webView.setWebViewClient(new WebViewClient());

        // Load the Visme form URL
        webView.loadUrl("https://forms.visme.co/formsPlayer/g7qn11p0-feedback-form"); // Replace with your actual Visme form URL
    }
}

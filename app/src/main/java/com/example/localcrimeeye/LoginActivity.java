package com.example.localcrimeeye;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText loginUsername, loginPassword;
    Button loginButton;
    TextView signupRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components
        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        loginButton = findViewById(R.id.login_button);

        Intent serviceIntent = new Intent(this, FirebaseBackgroundService.class);
        startService(serviceIntent);


        // Handle login button click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()) {
                    checkUser();
                }
            }
        });

        // Redirect to SignUpActivity on clicking signup text
        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    // Validate user inputs
    private boolean validateInputs() {
        return validateUsername() && validatePassword();
    }

    // Validate username input
    public Boolean validateUsername() {
        String val = loginUsername.getText().toString();
        if (val.isEmpty()) {
            loginUsername.setError("Username cannot be empty");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    // Validate password input
    public Boolean validatePassword() {
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        if (userUsername.equals("test") && userPassword.equals("test123")) {
            // Save the logged-in user's username in SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", userUsername);
            editor.apply();

            Intent intent = new Intent(LoginActivity.this, activity_main.class);
            startActivity(intent);
            finish();
        } else {
            // If the credentials are wrong, show an error message
            if (!userUsername.equals("Forum")) {
                loginUsername.setError("Invalid Username");
                loginUsername.requestFocus();
            } else {
                loginPassword.setError("Invalid Password");
                loginPassword.requestFocus();
            }
        }
    }
}

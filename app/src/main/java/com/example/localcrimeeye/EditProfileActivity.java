package com.example.localcrimeeye;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {

    EditText editName, editEmail, editUsername, editPassword, editPhone, editLocation;
    Button saveButton;
    TextView backToProfile;
    String nameUser, emailUser, usernameUser, passwordUser, phoneUser, locationUser;
    DatabaseReference reference;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        reference = FirebaseDatabase.getInstance().getReference("users");

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        editPhone = findViewById(R.id.editPhone);
        editLocation = findViewById(R.id.editLocation);
        saveButton = findViewById(R.id.saveButton);
        backToProfile = findViewById(R.id.BackToProfile);

        showData();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()) {
                    boolean nameChanged = isNameChanged();
                    boolean emailChanged = isEmailChanged();
                    boolean passwordChanged = isPasswordChanged();
                    boolean phoneChanged = isPhoneChanged();

                    if (nameChanged || emailChanged || passwordChanged || phoneChanged) {
                        Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        returnToMainActivity(); // Navigate to MainActivity
                    } else {
                        Toast.makeText(EditProfileActivity.this, "No Changes Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        backToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Return to the previous page
            }
        });

        // Disable editing for location
        editLocation.setEnabled(false);
    }

    // Validate input fields
    private boolean validateInputs() {
        if (editName.getText().toString().trim().isEmpty()) {
            editName.setError("Name cannot be empty");
            return false;
        }
        if (editEmail.getText().toString().trim().isEmpty()) {
            editEmail.setError("Email cannot be empty");
            return false;
        }
        if (editPassword.getText().toString().trim().isEmpty()) {
            editPassword.setError("Password cannot be empty");
            return false;
        }
        if (editPhone.getText().toString().trim().isEmpty()) {
            editPhone.setError("Phone cannot be empty");
            return false;
        }
        return true;
    }

    public boolean isNameChanged() {
        if (!nameUser.equals(editName.getText().toString().trim())) {
            reference.child(usernameUser).child("name").setValue(editName.getText().toString().trim());
            nameUser = editName.getText().toString().trim();
            return true;
        }
        return false;
    }

    public boolean isEmailChanged() {
        if (!emailUser.equals(editEmail.getText().toString().trim())) {
            reference.child(usernameUser).child("email").setValue(editEmail.getText().toString().trim());
            emailUser = editEmail.getText().toString().trim();
            return true;
        }
        return false;
    }

    public boolean isPasswordChanged() {
        if (!passwordUser.equals(editPassword.getText().toString().trim())) {
            reference.child(usernameUser).child("password").setValue(editPassword.getText().toString().trim());
            passwordUser = editPassword.getText().toString().trim();
            return true;
        }
        return false;
    }

    public boolean isPhoneChanged() {
        if (!phoneUser.equals(editPhone.getText().toString().trim())) {
            reference.child(usernameUser).child("phone").setValue(editPhone.getText().toString().trim());
            phoneUser = editPhone.getText().toString().trim();
            return true;
        }
        return false;
    }

    public void showData() {
        Intent intent = getIntent();
        usernameUser = intent.getStringExtra("username"); // Ensure username is retrieved first

        // Retrieve data from Firebase Database
        reference.child(usernameUser).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    nameUser = dataSnapshot.child("name").getValue(String.class);
                    emailUser = dataSnapshot.child("email").getValue(String.class);
                    passwordUser = dataSnapshot.child("password").getValue(String.class);
                    phoneUser = dataSnapshot.child("phone").getValue(String.class);
                    locationUser = dataSnapshot.child("location").getValue(String.class);

                    // Set the EditText fields with the retrieved data
                    editName.setText(nameUser);
                    editEmail.setText(emailUser);
                    editUsername.setText(usernameUser);
                    editPassword.setText(passwordUser);
                    editPhone.setText(phoneUser);
                    editLocation.setText(locationUser); // Display last saved location

                    // Make the username field non-editable
                    editUsername.setEnabled(false);
                } else {
                    Toast.makeText(EditProfileActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(EditProfileActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void returnToMainActivity() {
        Intent intent = new Intent(EditProfileActivity.this, activity_main.class);
        startActivity(intent);
        finish(); // Close the EditProfileActivity
    }
}

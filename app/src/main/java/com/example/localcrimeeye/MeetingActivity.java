package com.example.localcrimeeye;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.List;

public class MeetingActivity extends AppCompatActivity {

    private EditText editTextMeetingTitle;
    private EditText editTextMeetingDate;
    private EditText editTextMeetingTime;
    private EditText editTextMembers;
    private EditText editTextMeetingLoc;
    private EditText editTextMettingDescription;
    private Button buttonSendInvite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting); // Ensure this matches your XML file name

        editTextMeetingTitle = findViewById(R.id.editTextMeetingTitle);
        editTextMeetingDate = findViewById(R.id.editTextMeetingDate);
        editTextMeetingTime = findViewById(R.id.editTextMeetingTime);
        editTextMembers = findViewById(R.id.editTextMembers);
        editTextMettingDescription = findViewById(R.id.editTextMettingDescription);
        buttonSendInvite = findViewById(R.id.buttonSendInvite);
        editTextMeetingLoc = findViewById(R.id.editTextMeetingLoc);

        // Set click listener to open DatePickerDialog
        editTextMeetingDate.setOnClickListener(v -> showDatePickerDialog());

        // Set click listener to open TimePickerDialog
        editTextMeetingTime.setOnClickListener(v -> showTimePickerDialog());

        buttonSendInvite.setOnClickListener(v -> sendMeetingInvites());
    }

    private void showDatePickerDialog() {
        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format the selected date and set it to the EditText
                    String date = selectedYear + "/" + ("0" + (selectedMonth + 1)) + "/" + selectedDay; // Month is 0-based
                    editTextMeetingDate.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        // Get the current time
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, selectedHour, selectedMinute) -> {
                    // Format the selected time and set it to the EditText
                    String time = String.format("%02d:%02d", selectedHour, selectedMinute); // Format to HH:mm
                    editTextMeetingTime.setText(time);
                }, hour, minute, true); // true for 24-hour format
        timePickerDialog.show();
    }

    private void sendMeetingInvites() {
        String meetingTitle = editTextMeetingTitle.getText().toString().trim();
        String meetingDate = editTextMeetingDate.getText().toString().trim();
        String meetingTime = editTextMeetingTime.getText().toString().trim();
        String meetingLoc = editTextMeetingLoc.getText().toString().trim();
        String members = editTextMembers.getText().toString().trim();
        String description = editTextMettingDescription.getText().toString().trim();

        if (meetingTitle.isEmpty()) {
            Toast.makeText(this, "Meeting title is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (meetingDate.isEmpty()) {
            Toast.makeText(this, "Meeting date is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (meetingTime.isEmpty()) {
            Toast.makeText(this, "Meeting time is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (meetingLoc.isEmpty()) {
            Toast.makeText(this,"Meeting Location is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (members.isEmpty()) {
            Toast.makeText(this, "Members' emails are required", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] emailAddresses = members.split(",");
        for (int i = 0; i < emailAddresses.length; i++) {
            emailAddresses[i] = emailAddresses[i].trim(); // Trim each email address
        }

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain"); // Use text/plain to allow any email client
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Meeting Invitation: " + meetingTitle);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "You are invited to a meeting.\n" + "\n" +
                "Title: " + "" + meetingTitle + "\n" +
                "Date: " + "" + meetingDate + "\n" +
                "Time: " + "" + meetingTime + "\n" +
                "Location: " + "" + meetingLoc + "\n" +
                "Description: " + "" + description + "\n" + "\n" +
                "Please join us in saving the community and fighting crime!.");

        if (emailAddresses.length > 0) {
            emailIntent.putExtra(Intent.EXTRA_EMAIL, emailAddresses);
        }

        // Check for available email clients
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);

            // Clear text fields after sending the email
            clearTextFields();
        } else {
            Toast.makeText(this, "No email client found", Toast.LENGTH_SHORT).show();
        }

        logAvailableEmailClients(emailIntent);
    }

    private void clearTextFields() {
        editTextMeetingTitle.setText("");
        editTextMeetingDate.setText("");
        editTextMeetingTime.setText("");
        editTextMeetingLoc.setText("");
        editTextMembers.setText("");
        editTextMettingDescription.setText("");
    }

    private void logAvailableEmailClients(Intent emailIntent) {
        List<ResolveInfo> activities = getPackageManager().queryIntentActivities(emailIntent, 0);
        for (ResolveInfo app : activities) {
            Log.d("EmailClient", "Found email client: " + app.activityInfo.packageName);
        }
    }
}

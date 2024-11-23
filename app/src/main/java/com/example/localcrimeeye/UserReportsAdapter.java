package com.example.localcrimeeye;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UserReportsAdapter extends RecyclerView.Adapter<UserReportsAdapter.UserReportViewHolder> {

    private final List<UserReport> userReportList;

    public UserReportsAdapter(List<UserReport> userReportList) {
        this.userReportList = userReportList;
    }

    @NonNull
    @Override
    public UserReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_report, parent, false);
        return new UserReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserReportViewHolder holder, int position) {
        UserReport userReport = userReportList.get(position);
        holder.tvUsername.setText(userReport.getUsername());
        holder.tvReportDetails.setText(userReport.getReportDetails());

        // Display the location using latitude and longitude
        String location = "Location: " + userReport.getLatitude() + ", " + userReport.getLongitude();
        holder.tvLocation.setText(location);
    }

    @Override
    public int getItemCount() {
        return userReportList.size();
    }

    static class UserReportViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;
        TextView tvReportDetails;
        TextView tvLocation; // Make sure to declare this TextView

        public UserReportViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvReportDetails = itemView.findViewById(R.id.tvReportDetails);
            tvLocation = itemView.findViewById(R.id.locationtext);  // Initialize location TextView
        }
    }
}

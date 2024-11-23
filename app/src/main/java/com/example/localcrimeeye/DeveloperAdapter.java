package com.example.localcrimeeye;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DeveloperAdapter extends RecyclerView.Adapter<DeveloperAdapter.DeveloperViewHolder> {

    private Context context;
    private List<Developer> developerList;

    public DeveloperAdapter(Context context, List<Developer> developerList) {
        this.context = context;
        this.developerList = developerList;
    }

    @NonNull
    @Override
    public DeveloperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activit_developer_card, parent, false);
        return new DeveloperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeveloperViewHolder holder, int position) {
        Developer developer = developerList.get(position);

        holder.developerName.setText(developer.getName());
        holder.developerEmail.setText(developer.getEmail());

        // Load the image from the drawable resource
        holder.developerImage.setImageResource(developer.getImageResId());

        // Open the portfolio URL in a browser when the text is clicked
        holder.developerPortfolio.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(developer.getPortfolioUrl()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return developerList.size();
    }

    public static class DeveloperViewHolder extends RecyclerView.ViewHolder {

        ImageView developerImage;
        TextView developerName, developerEmail, developerPortfolio;

        public DeveloperViewHolder(@NonNull View itemView) {
            super(itemView);

            developerImage = itemView.findViewById(R.id.developer_image);
            developerName = itemView.findViewById(R.id.developer_name);
            developerEmail = itemView.findViewById(R.id.developer_email);
            developerPortfolio = itemView.findViewById(R.id.developer_portfolio);
        }
    }
}

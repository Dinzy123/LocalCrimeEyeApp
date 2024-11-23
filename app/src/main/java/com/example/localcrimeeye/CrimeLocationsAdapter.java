package com.example.localcrimeeye;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CrimeLocationsAdapter extends RecyclerView.Adapter<CrimeLocationsAdapter.CrimeLocationViewHolder> {

    private final List<CrimeLocation> crimeLocationList;

    public CrimeLocationsAdapter(List<CrimeLocation> crimeLocationList) {
        this.crimeLocationList = crimeLocationList;
    }

    @NonNull
    @Override
    public CrimeLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_crime_location, parent, false);
        return new CrimeLocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CrimeLocationViewHolder holder, int position) {
        CrimeLocation crimeLocation = crimeLocationList.get(position);
        holder.tvLocationName.setText(crimeLocation.getName());
        holder.tvLocationDescription.setText(crimeLocation.getDescription());
    }

    @Override
    public int getItemCount() {
        return crimeLocationList.size();
    }

    static class CrimeLocationViewHolder extends RecyclerView.ViewHolder {
        TextView tvLocationName;
        TextView tvLocationDescription;

        public CrimeLocationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLocationName = itemView.findViewById(R.id.tvLocationName);
            tvLocationDescription = itemView.findViewById(R.id.tvLocationDescription);
        }
    }
}

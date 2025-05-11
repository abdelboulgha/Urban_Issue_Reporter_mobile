package com.example.urban_issue_reporter_mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.urban_issue_reporter_mobile.R;

import com.example.urban_issue_reporter_mobile.model.Reclamation;

import java.util.List;

public class ReclamationAdapter extends RecyclerView.Adapter<ReclamationAdapter.ReclamationViewHolder> {

    private List<Reclamation> reclamationList;

    public ReclamationAdapter(List<Reclamation> reclamationList) {
        this.reclamationList = reclamationList;
    }

    public void setReclamations(List<Reclamation> newList) {
        this.reclamationList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReclamationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reclamation, parent, false);
        return new ReclamationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReclamationViewHolder holder, int position) {
        Reclamation r = reclamationList.get(position);
        holder.titre.setText(r.getTitre());
        holder.description.setText(r.getDescription());
        holder.date.setText(r.getDateDeCreation());
        holder.statut.setText(r.getStatut());
        holder.localisation.setText(r.getLocalisation());

        // Set category and region names if available
        if (r.getCategorie() != null) {
            holder.categorie.setText(r.getCategorie().getLibelle());
        } else {
            holder.categorie.setText("Catégorie non disponible");
        }

        if (r.getRegion() != null) {
            holder.region.setText(r.getRegion().getNom());
        } else {
            holder.region.setText("Région non disponible");
        }
    }


    @Override
    public int getItemCount() {
        return reclamationList != null ? reclamationList.size() : 0;
    }

    static class ReclamationViewHolder extends RecyclerView.ViewHolder {
        TextView titre, description, date, statut, localisation, categorie, region;

        public ReclamationViewHolder(@NonNull View itemView) {
            super(itemView);
            titre = itemView.findViewById(R.id.tvTitre);
            description = itemView.findViewById(R.id.tvDescription);
            date = itemView.findViewById(R.id.tvDate);
            statut = itemView.findViewById(R.id.tvStatut);
            localisation = itemView.findViewById(R.id.tvLocalisation);
            categorie = itemView.findViewById(R.id.tvCategorie);  // Add category TextView
            region = itemView.findViewById(R.id.tvRegion);  // Add region TextView
        }
    }

}


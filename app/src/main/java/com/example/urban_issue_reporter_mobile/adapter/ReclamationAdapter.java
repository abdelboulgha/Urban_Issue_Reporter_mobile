package com.example.urban_issue_reporter_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.urban_issue_reporter_mobile.R;
import com.example.urban_issue_reporter_mobile.model.Reclamation;
import com.example.urban_issue_reporter_mobile.model.VoteManager;

import java.util.List;

public class ReclamationAdapter extends RecyclerView.Adapter<ReclamationAdapter.ReclamationViewHolder> {

    // Interface pour gérer les clics sur le bouton de vote
    public interface VoteClickListener {
        void onVoteClick(int position, Reclamation reclamation, boolean isVoting);
    }

    private Context context;
    private List<Reclamation> reclamationList;
    private VoteClickListener voteClickListener;

    public ReclamationAdapter(Context context, List<Reclamation> reclamationList, VoteClickListener listener) {
        this.context = context;
        this.reclamationList = reclamationList;
        this.voteClickListener = listener;
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
        holder.votes.setText(r.getNombreDeVotes() + " votes");

        // Définir l'apparence et le texte du bouton en fonction de l'état du vote
        boolean hasVoted = VoteManager.hasVoted(context, r.getId());
        if (hasVoted) {
            holder.btnVote.setText("Annuler vote");
            holder.btnVote.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorAccentSecondary));
        } else {
            holder.btnVote.setText("Voter +1");
            holder.btnVote.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorAccent));
        }

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

        // Configurer le clic sur le bouton de vote
        holder.btnVote.setOnClickListener(v -> {
            if (voteClickListener != null) {
                boolean isCurrentlyVoted = VoteManager.hasVoted(context, r.getId());
                voteClickListener.onVoteClick(holder.getAdapterPosition(), r, !isCurrentlyVoted);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reclamationList != null ? reclamationList.size() : 0;
    }

    static class ReclamationViewHolder extends RecyclerView.ViewHolder {
        TextView titre, description, date, statut, localisation, categorie, region, votes;
        Button btnVote;

        public ReclamationViewHolder(@NonNull View itemView) {
            super(itemView);
            titre = itemView.findViewById(R.id.tvTitre);
            description = itemView.findViewById(R.id.tvDescription);
            date = itemView.findViewById(R.id.tvDate);
            statut = itemView.findViewById(R.id.tvStatut);
            localisation = itemView.findViewById(R.id.tvLocalisation);
            categorie = itemView.findViewById(R.id.tvCategorie);
            region = itemView.findViewById(R.id.tvRegion);
            votes = itemView.findViewById(R.id.tvVotes);
            btnVote = itemView.findViewById(R.id.btnVote);
        }
    }
}
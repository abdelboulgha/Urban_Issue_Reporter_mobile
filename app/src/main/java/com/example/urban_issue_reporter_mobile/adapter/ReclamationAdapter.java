package com.example.urban_issue_reporter_mobile.adapter;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.urban_issue_reporter_mobile.R;
import com.example.urban_issue_reporter_mobile.model.Photo;
import com.example.urban_issue_reporter_mobile.model.Reclamation;
import com.example.urban_issue_reporter_mobile.ui.reclamation.ReclamationViewModel;
import com.example.urban_issue_reporter_mobile.model.VoteManager;

import java.util.ArrayList;
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
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_reclamation, parent, false);
        return new ReclamationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReclamationViewHolder holder, int position) {
        Reclamation r = reclamationList.get(position);

        // Afficher les informations de base
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

        // Charger les photos
        holder.loadPhotos(r.getId());
    }

    @Override
    public int getItemCount() {
        return reclamationList != null ? reclamationList.size() : 0;
    }

    static class ReclamationViewHolder extends RecyclerView.ViewHolder {
        TextView titre, description, date, statut, localisation, categorie, region, votes;
        Button btnVote;
        ViewPager2 photosViewPager;
        LinearLayout photosIndicator;
        PhotosAdapter photosAdapter;
        ReclamationViewModel viewModel;
        Context context;

        public ReclamationViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialiser les vues de base
            titre = itemView.findViewById(R.id.tvTitre);
            description = itemView.findViewById(R.id.tvDescription);
            date = itemView.findViewById(R.id.tvDate);
            statut = itemView.findViewById(R.id.tvStatut);
            localisation = itemView.findViewById(R.id.tvLocalisation);
            categorie = itemView.findViewById(R.id.tvCategorie);
            region = itemView.findViewById(R.id.tvRegion);
            votes = itemView.findViewById(R.id.tvVotes);
            btnVote = itemView.findViewById(R.id.btnVote);

            // Initialiser les vues pour les photos
            context = itemView.getContext();
            photosViewPager = itemView.findViewById(R.id.photosViewPager);
            photosIndicator = itemView.findViewById(R.id.photosIndicator);

            try {
                // Initialiser le ViewModel
                viewModel = new ViewModelProvider((FragmentActivity) context).get(ReclamationViewModel.class);

                // Initialiser l'adaptateur de photos
                photosAdapter = new PhotosAdapter(context, new ArrayList<>());
                photosViewPager.setAdapter(photosAdapter);
            } catch (Exception e) {
                Log.e("ReclamationAdapter", "Erreur lors de l'initialisation du viewModel: " + e.getMessage());
            }
        }

        void loadPhotos(int reclamationId) {
            try {
                viewModel.getPhotosForReclamation(reclamationId).observe((LifecycleOwner) context, photos -> {
                    if (photos != null && !photos.isEmpty()) {
                        Log.d("Photos", "Récupération de " + photos.size() + " photos pour la réclamation " + reclamationId);
                        photosAdapter.setPhotos(photos);
                        photosViewPager.setVisibility(View.VISIBLE);

                        // Mettre à jour les indicateurs de photos
                        updatePhotoIndicators(photos.size());
                    } else {
                        Log.d("Photos", "Aucune photo pour la réclamation " + reclamationId);
                        photosViewPager.setVisibility(View.GONE);
                        photosIndicator.setVisibility(View.GONE);
                    }
                });
            } catch (Exception e) {
                Log.e("ReclamationAdapter", "Erreur lors du chargement des photos: " + e.getMessage());
                photosViewPager.setVisibility(View.GONE);
                photosIndicator.setVisibility(View.GONE);
            }
        }

        private void updatePhotoIndicators(int size) {
            photosIndicator.removeAllViews();

            if (size > 1) {
                // Créer les indicateurs de pagination
                for (int i = 0; i < size; i++) {
                    View indicator = new View(context);
                    int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, context.getResources().getDisplayMetrics());
                    int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, context.getResources().getDisplayMetrics());
                    int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources().getDisplayMetrics());

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                    params.setMargins(margin, 0, margin, 0);
                    indicator.setLayoutParams(params);
                    indicator.setBackgroundResource(i == 0 ? R.drawable.indicator_active : R.drawable.indicator_inactive);

                    photosIndicator.addView(indicator);
                }

                // Configurer le changement d'indicateur lors du défilement
                photosViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        for (int i = 0; i < photosIndicator.getChildCount(); i++) {
                            photosIndicator.getChildAt(i).setBackgroundResource(
                                    i == position ? R.drawable.indicator_active : R.drawable.indicator_inactive);
                        }
                    }
                });

                photosIndicator.setVisibility(View.VISIBLE);
            } else {
                photosIndicator.setVisibility(View.GONE);
            }
        }
    }
}
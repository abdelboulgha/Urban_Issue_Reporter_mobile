package com.example.urban_issue_reporter_mobile.ui.reclamation;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.urban_issue_reporter_mobile.R;
import com.example.urban_issue_reporter_mobile.adapter.ReclamationAdapter;
import com.example.urban_issue_reporter_mobile.model.Reclamation;
import com.example.urban_issue_reporter_mobile.model.VoteManager;

import java.util.ArrayList;

public class ReclamationFragment extends Fragment implements ReclamationAdapter.VoteClickListener {

    private RecyclerView recyclerView;
    private ReclamationAdapter adapter;
    private ReclamationViewModel viewModel;
    private ProgressBar progressBar;

    public ReclamationFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reclamation, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialiser l'adaptateur avec le listener pour les votes
        adapter = new ReclamationAdapter(getContext(), new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ReclamationViewModel.class);

        // Afficher le chargement
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        viewModel.getReclamations().observe(getViewLifecycleOwner(), reclamations -> {
            if (reclamations != null) {
                for (Reclamation reclamation : reclamations) {
                    Integer regionId = reclamation.getRegionId();
                    Integer categorieId = reclamation.getCategorieId();

                    if (regionId != null) {
                        viewModel.getRegionById(regionId).observe(getViewLifecycleOwner(), region -> {
                            if (region != null) {
                                reclamation.setRegion(region);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }

                    if (categorieId != null) {
                        viewModel.getCategorieById(categorieId).observe(getViewLifecycleOwner(), categorie -> {
                            if (categorie != null) {
                                reclamation.setCategorie(categorie);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }

                adapter.setReclamations(reclamations);
            }

            // Masquer le chargement
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        });

        return view;
    }

    // Implémentation de l'interface VoteClickListener
    @Override
    public void onVoteClick(int position, Reclamation reclamation, boolean isVoting) {
        // Calculer le nouveau nombre de votes
        int currentVotes = reclamation.getNombreDeVotes();
        int newVoteCount;

        if (isVoting) {
            // Voter
            newVoteCount = currentVotes + 1;
            Toast.makeText(getContext(), "Vote en cours...", Toast.LENGTH_SHORT).show();
        } else {
            // Annuler le vote
            newVoteCount = currentVotes - 1;
            if (newVoteCount < 0) newVoteCount = 0; // Éviter les valeurs négatives
            Toast.makeText(getContext(), "Annulation du vote en cours...", Toast.LENGTH_SHORT).show();
        }

        // Appeler le ViewModel pour mettre à jour le vote
        int finalNewVoteCount = newVoteCount;
        viewModel.voteReclamation(reclamation.getId(), newVoteCount).observe(getViewLifecycleOwner(), success -> {
            if (success) {
                // Mise à jour locale
                reclamation.setNombreDeVotes(finalNewVoteCount);

                // Mettre à jour le statut du vote dans SharedPreferences
                if (isVoting) {
                    VoteManager.addVote(getContext(), reclamation.getId());
                    Toast.makeText(getContext(), "Vote enregistré!", Toast.LENGTH_SHORT).show();
                } else {
                    VoteManager.removeVote(getContext(), reclamation.getId());
                    Toast.makeText(getContext(), "Vote annulé!", Toast.LENGTH_SHORT).show();
                }

                // Mettre à jour l'interface
                adapter.notifyItemChanged(position);
            } else {
                // Erreur
                String message = isVoting ? "Erreur lors du vote" : "Erreur lors de l'annulation du vote";
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
package com.example.urban_issue_reporter_mobile.ui.mes_reclamations;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urban_issue_reporter_mobile.R;
import com.example.urban_issue_reporter_mobile.adapter.ReclamationAdapter;
import com.example.urban_issue_reporter_mobile.model.Reclamation;
import com.example.urban_issue_reporter_mobile.model.VoteManager;

import java.util.ArrayList;
import java.util.List;

public class MesReclamationsFragment extends Fragment implements ReclamationAdapter.VoteClickListener {

    private RecyclerView recyclerView;
    private ReclamationAdapter adapter;
    private MesReclamationsViewModel viewModel;
    private ProgressBar progressBar;
    private TextView tvEmptyMessage;
    private int userId;

    public MesReclamationsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mes_reclamations, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        tvEmptyMessage = view.findViewById(R.id.tvEmptyMessage);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Désactiver le recyclage des viewholders pour maintenir l'état des photos
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);

        // Initialiser l'adaptateur avec le listener pour les votes
        adapter = new ReclamationAdapter(getContext(), new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        // Récupérer l'ID de l'utilisateur connecté depuis les SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        if (userId == -1) {
            // Utilisateur non connecté, afficher un message
            Toast.makeText(getContext(), "Veuillez vous connecter pour voir vos réclamations", Toast.LENGTH_LONG).show();
            tvEmptyMessage.setText("Veuillez vous connecter pour voir vos réclamations");
            tvEmptyMessage.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return view;
        }

        viewModel = new ViewModelProvider(this).get(MesReclamationsViewModel.class);

        // Afficher le chargement
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tvEmptyMessage.setVisibility(View.GONE);

        viewModel.getMesReclamations(userId).observe(getViewLifecycleOwner(), reclamations -> {
            if (reclamations != null) {
                Log.d("MesReclamationsFragment", "Récupération de " + reclamations.size() + " réclamations");

                if (reclamations.isEmpty()) {
                    tvEmptyMessage.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    tvEmptyMessage.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    // Précharger toutes les photos
                    for (Reclamation reclamation : reclamations) {
                        int reclamationId = reclamation.getId();
                        Log.d("MesReclamationsFragment", "Préchargement des photos pour la réclamation " + reclamationId);
                        viewModel.getPhotosForReclamation(reclamationId);
                    }

                    // Charger les informations de région et de catégorie
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
            } else {
                Log.e("MesReclamationsFragment", "Liste de réclamations NULL");
                tvEmptyMessage.setText("Erreur lors de la récupération des réclamations");
                tvEmptyMessage.setVisibility(View.VISIBLE);
            }

            // Masquer le chargement
            progressBar.setVisibility(View.GONE);
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
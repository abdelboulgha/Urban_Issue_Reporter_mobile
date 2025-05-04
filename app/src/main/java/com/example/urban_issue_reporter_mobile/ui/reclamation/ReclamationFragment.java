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

import com.example.urban_issue_reporter_mobile.R;
import com.example.urban_issue_reporter_mobile.adapter.ReclamationAdapter;
import com.example.urban_issue_reporter_mobile.model.Reclamation;

import java.util.ArrayList;

public class ReclamationFragment extends Fragment {

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
        adapter = new ReclamationAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ReclamationViewModel.class);

        // Show loading
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
                                Log.d("ReclamatgetRegion", reclamation.getRegion().toString());
                            }
                        });
                    }

                    if (categorieId != null) {
                        viewModel.getCategorieById(categorieId).observe(getViewLifecycleOwner(), categorie -> {
                            if (categorie != null) {
                                reclamation.setCategorie(categorie);  // Set Categorie to Reclamation
                                Log.d("ReclamationCategorie", reclamation.getCategorie().toString());
                            }
                        });
                    }
                }

                adapter.setReclamations(reclamations);
            }

            // Hide loading
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        });



        return view;
    }
}

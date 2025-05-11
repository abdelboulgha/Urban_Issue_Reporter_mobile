package com.example.urban_issue_reporter_mobile.ui.reclamation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.urban_issue_reporter_mobile.model.Categorie;
import com.example.urban_issue_reporter_mobile.model.Photo;
import com.example.urban_issue_reporter_mobile.model.Reclamation;
import com.example.urban_issue_reporter_mobile.model.Region;
import com.example.urban_issue_reporter_mobile.repository.ReclamationRepository;

import java.util.List;

public class ReclamationViewModel extends ViewModel {

    private ReclamationRepository repository;

    public ReclamationViewModel() {
        repository = new ReclamationRepository();
    }

    // Méthodes existantes
    public LiveData<List<Reclamation>> getReclamations() {
        return repository.getReclamations();
    }

    public LiveData<Region> getRegionById(int id) {
        return repository.getRegionById(id);
    }

    public LiveData<Categorie> getCategorieById(int id) {
        return repository.getCategorieById(id);
    }

    // Nouvelle méthode pour voter
    public LiveData<Boolean> voteReclamation(int reclamationId, int newVoteCount) {
        return repository.voteReclamation(reclamationId, newVoteCount);
    }

    public LiveData<List<Photo>> getPhotosForReclamation(int reclamationId) {
        return repository.getPhotosForReclamation(reclamationId);
    }
}
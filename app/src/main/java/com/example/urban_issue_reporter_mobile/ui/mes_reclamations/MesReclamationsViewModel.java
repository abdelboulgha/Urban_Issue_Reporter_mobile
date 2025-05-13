package com.example.urban_issue_reporter_mobile.ui.mes_reclamations;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.urban_issue_reporter_mobile.model.Categorie;
import com.example.urban_issue_reporter_mobile.model.Photo;
import com.example.urban_issue_reporter_mobile.model.Reclamation;
import com.example.urban_issue_reporter_mobile.model.Region;
import com.example.urban_issue_reporter_mobile.repository.ReclamationRepository;

import java.util.List;

public class MesReclamationsViewModel extends ViewModel {

    private ReclamationRepository repository;

    public MesReclamationsViewModel() {
        repository = new ReclamationRepository();
    }

    // Méthode pour récupérer mes réclamations
    public LiveData<List<Reclamation>> getMesReclamations(int citoyenId) {
        return repository.getMesReclamations(citoyenId);
    }

    public LiveData<Region> getRegionById(int id) {
        return repository.getRegionById(id);
    }

    public LiveData<Categorie> getCategorieById(int id) {
        return repository.getCategorieById(id);
    }

    public LiveData<Boolean> voteReclamation(int reclamationId, int newVoteCount) {
        return repository.voteReclamation(reclamationId, newVoteCount);
    }

    public LiveData<List<Photo>> getPhotosForReclamation(int reclamationId) {
        return repository.getPhotosForReclamation(reclamationId);
    }
}
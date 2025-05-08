package com.example.urban_issue_reporter_mobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.urban_issue_reporter_mobile.model.Reclamation;
import com.example.urban_issue_reporter_mobile.repository.ReclamationRepository;

import java.util.List;

public class ReclamationViewModel extends ViewModel {
    private MutableLiveData<List<Reclamation>> reclamations;
    private MutableLiveData<String> error;
    private ReclamationRepository repository;

    public ReclamationViewModel() {
        reclamations = new MutableLiveData<>();
        error = new MutableLiveData<>();
        repository = new ReclamationRepository();
        fetchReclamations();
    }

    public LiveData<List<Reclamation>> getReclamations() {
        return reclamations;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void fetchReclamations() {
        repository.getReclamations(reclamations, error);
    }
}

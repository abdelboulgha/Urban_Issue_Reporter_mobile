package com.example.urban_issue_reporter_mobile.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.urban_issue_reporter_mobile.model.Reclamation;
import com.example.urban_issue_reporter_mobile.network.ApiService;
import com.example.urban_issue_reporter_mobile.network.RetrofitClient;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReclamationRepository {
    private ApiService apiService;

    public ReclamationRepository() {
        apiService = RetrofitClient.getInstance();
    }

    public void getReclamations(MutableLiveData<List<Reclamation>> data, MutableLiveData<String> error) {
        apiService.getReclamations().enqueue(new Callback<List<Reclamation>>() {
            @Override
            public void onResponse(Call<List<Reclamation>> call, Response<List<Reclamation>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    error.setValue("Erreur " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Reclamation>> call, Throwable t) {
                error.setValue(t.getMessage());
            }
        });
    }
}

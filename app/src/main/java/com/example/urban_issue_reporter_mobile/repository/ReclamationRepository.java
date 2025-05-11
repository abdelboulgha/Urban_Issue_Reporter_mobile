package com.example.urban_issue_reporter_mobile.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.urban_issue_reporter_mobile.api.ApiService;
import com.example.urban_issue_reporter_mobile.api.RetrofitClient;
import com.example.urban_issue_reporter_mobile.model.Categorie;
import com.example.urban_issue_reporter_mobile.model.CategorieResponse;
import com.example.urban_issue_reporter_mobile.model.Photo;
import com.example.urban_issue_reporter_mobile.model.Reclamation;
import com.example.urban_issue_reporter_mobile.model.ReclamationResponse;
import com.example.urban_issue_reporter_mobile.model.Region;
import com.example.urban_issue_reporter_mobile.model.RegionResponse;
import com.example.urban_issue_reporter_mobile.model.VoteRequest;
import com.example.urban_issue_reporter_mobile.model.VoteResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReclamationRepository {
    private ApiService apiService;

    public ReclamationRepository() {
        apiService = RetrofitClient.getInstance();
    }

    public LiveData<List<Reclamation>> getReclamations() {
        MutableLiveData<List<Reclamation>> data = new MutableLiveData<>();

        apiService.getReclamations().enqueue(new Callback<ReclamationResponse>() {
            @Override
            public void onResponse(Call<ReclamationResponse> call, Response<ReclamationResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body().getReclamations());
                }
            }

            @Override
            public void onFailure(Call<ReclamationResponse> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<Region> getRegionById(int id) {
        MutableLiveData<Region> data = new MutableLiveData<>();

        apiService.getRegionById(id).enqueue(new Callback<RegionResponse>() {
            @Override
            public void onResponse(Call<RegionResponse> call, Response<RegionResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("API_RESPONSE", "Region: " + response.body().getRegion());
                    data.setValue(response.body().getRegion());
                }
            }

            @Override
            public void onFailure(Call<RegionResponse> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }


    public LiveData<Categorie> getCategorieById(int id) {
        MutableLiveData<Categorie> data = new MutableLiveData<>();

        apiService.getCategorieById(id).enqueue(new Callback<CategorieResponse>() {
            @Override
            public void onResponse(Call<CategorieResponse> call, Response<CategorieResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body().getCategorie());
                }
            }

            @Override
            public void onFailure(Call<CategorieResponse> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<Boolean> voteReclamation(int reclamationId, int nouveauNombreVotes) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        VoteRequest request = new VoteRequest(nouveauNombreVotes);
        apiService.voteReclamation(reclamationId, request).enqueue(new Callback<VoteResponse>() {
            @Override
            public void onResponse(Call<VoteResponse> call, Response<VoteResponse> response) {
                if (response.isSuccessful()) {
                    result.setValue(true);
                } else {
                    result.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<VoteResponse> call, Throwable t) {
                result.setValue(false);
            }
        });

        return result;
    }

    public LiveData<List<Photo>> getPhotosForReclamation(int reclamationId) {
        MutableLiveData<List<Photo>> data = new MutableLiveData<>();

        apiService.getPhotosForReclamation(reclamationId).enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                    Log.d("Photos", "Récupération des photos réussie pour la réclamation " + reclamationId);
                } else {
                    data.setValue(null);
                    Log.e("Photos", "Échec de la récupération des photos: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                data.setValue(null);
                Log.e("Photos", "Erreur lors de la récupération des photos: " + t.getMessage());
            }
        });

        return data;
    }

}

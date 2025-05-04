package com.example.urban_issue_reporter_mobile.network;

import com.example.urban_issue_reporter_mobile.model.Reclamation;

import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;

public interface ApiService {
    @GET("reclamations")
    Call<List<Reclamation>> getReclamations();
}

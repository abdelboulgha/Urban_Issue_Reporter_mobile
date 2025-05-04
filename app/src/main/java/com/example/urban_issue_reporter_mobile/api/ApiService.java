package com.example.urban_issue_reporter_mobile.api;

import com.example.urban_issue_reporter_mobile.model.Categorie;
import com.example.urban_issue_reporter_mobile.model.CategorieResponse;
import com.example.urban_issue_reporter_mobile.model.ReclamationResponse;
import com.example.urban_issue_reporter_mobile.model.Region;
import com.example.urban_issue_reporter_mobile.model.RegionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("reclamations")
    Call<ReclamationResponse> getReclamations();

    @GET("region/{id}")
    Call<RegionResponse> getRegionById(@Path("id") int regionId);

    @GET("categorie/{id}")
    Call<CategorieResponse> getCategorieById(@Path("id") int categorieId);

}

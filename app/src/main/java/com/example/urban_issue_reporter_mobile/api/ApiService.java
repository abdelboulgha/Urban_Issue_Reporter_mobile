package com.example.urban_issue_reporter_mobile.api;

import com.example.urban_issue_reporter_mobile.model.Categorie;
import com.example.urban_issue_reporter_mobile.model.Region;
import com.example.urban_issue_reporter_mobile.ui.beans.Reclamation;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    // Méthode pour soumettre une réclamation
    @POST("reclamation")
    Call<ReclamationResponse> createReclamation(@Body ReclamationRequest request);

    // Méthode pour soumettre une réclamation avec une image
    @Multipart
    @POST("reclamation/avec-image")
    Call<ReclamationResponse> createReclamationWithImage(
            @Part("reclamation") RequestBody reclamationData,
            @Part MultipartBody.Part photo);

    // Méthode pour récupérer toutes les catégories
    @GET("categories")
    Call<List<Categorie>> getAllCategories();

    // Méthode pour récupérer toutes les régions
    @GET("regions")
    Call<List<Region>> getAllRegions();
}
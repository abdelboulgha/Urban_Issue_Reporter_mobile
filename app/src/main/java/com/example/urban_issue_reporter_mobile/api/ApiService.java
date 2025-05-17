package com.example.urban_issue_reporter_mobile.api;

import com.example.urban_issue_reporter_mobile.model.Categorie;
import com.example.urban_issue_reporter_mobile.model.CategoriesListResponse;
import com.example.urban_issue_reporter_mobile.model.CategorieResponse;
import com.example.urban_issue_reporter_mobile.model.Photo;
import com.example.urban_issue_reporter_mobile.model.ReclamationResponse;
import com.example.urban_issue_reporter_mobile.model.Region;
import com.example.urban_issue_reporter_mobile.model.RegionsListResponse;
import com.example.urban_issue_reporter_mobile.model.RegionResponse;
import com.example.urban_issue_reporter_mobile.model.VoteRequest;
import com.example.urban_issue_reporter_mobile.model.VoteResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface ApiService {
    @GET("reclamations")
    Call<ReclamationResponse> getReclamations();

    @GET("region/{id}")
    Call<RegionResponse> getRegionById(@Path("id") int regionId);

    @GET("categorie/{id}")
    Call<CategorieResponse> getCategorieById(@Path("id") int categorieId);

    // Méthode pour voter
    @PUT("reclamation/{id}/votes")
    Call<VoteResponse> voteReclamation(@Path("id") int reclamationId, @Body VoteRequest voteRequest);

    // Méthode pour récupérer les photos d'une réclamation
    @GET("photos/reclamation/{id}")
    Call<List<Photo>> getPhotosForReclamation(@Path("id") int reclamationId);

    // Méthode sans photo
    @POST("reclamations")
    Call<ReclamationResponse> createReclamation(@Body RequestBody reclamationData);

    // Méthode avec photo - MODIFIÉE pour utiliser PartMap au lieu de Part pour les données
    @Multipart
    @POST("reclamation/avec-image")
    Call<ReclamationResponse> createReclamationWithImage(
            @PartMap Map<String, RequestBody> reclamationData,
            @Part MultipartBody.Part photo);

    // Méthodes pour les catégories et régions
    @GET("categories")
    Call<CategoriesListResponse> getAllCategories();

    @GET("regions")
    Call<RegionsListResponse> getAllRegions();
}
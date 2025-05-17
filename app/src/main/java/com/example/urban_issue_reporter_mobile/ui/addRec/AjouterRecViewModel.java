package com.example.urban_issue_reporter_mobile.ui.addRec;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.urban_issue_reporter_mobile.api.ApiService;
import com.example.urban_issue_reporter_mobile.api.RetrofitClient;
import com.example.urban_issue_reporter_mobile.model.Categorie;
import com.example.urban_issue_reporter_mobile.model.CategoriesListResponse;
import com.example.urban_issue_reporter_mobile.model.ReclamationResponse;
import com.example.urban_issue_reporter_mobile.model.Region;
import com.example.urban_issue_reporter_mobile.model.RegionsListResponse;
import com.example.urban_issue_reporter_mobile.ui.beans.Reclamation;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AjouterRecViewModel extends ViewModel {

    private ApiService apiService;
    private MutableLiveData<List<Categorie>> categories = new MutableLiveData<>();
    private MutableLiveData<List<Region>> regions = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public AjouterRecViewModel() {
        apiService = RetrofitClient.getInstance();
        loadCategories();
        loadRegions();
    }

    public interface ReclamationCallback {
        void onSuccess();
        void onError(String message);
    }

    public LiveData<List<Categorie>> getCategories() {
        return categories;
    }

    public LiveData<List<Region>> getRegions() {
        return regions;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    private void loadCategories() {
        isLoading.setValue(true);
        Log.d("AjouterRecViewModel", "Début du chargement des catégories");
        apiService.getAllCategories().enqueue(new Callback<CategoriesListResponse>() {
            @Override
            public void onResponse(Call<CategoriesListResponse> call, Response<CategoriesListResponse> response) {
                isLoading.setValue(false);
                Log.d("AjouterRecViewModel", "Réponse reçue pour les catégories: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    List<Categorie> categoriesList = response.body().getCategories();
                    if (categoriesList != null) {
                        categories.setValue(categoriesList);
                        Log.d("AjouterRecViewModel", "Catégories chargées: " + categoriesList.size());
                    } else {
                        Log.e("AjouterRecViewModel", "Liste de catégories null");
                        categories.setValue(new ArrayList<>());
                    }
                } else {
                    try {
                        Log.e("AjouterRecViewModel", "Erreur: " + (response.errorBody() != null ? response.errorBody().string() : "Inconnue"));
                    } catch (Exception e) {
                        Log.e("AjouterRecViewModel", "Erreur lors de la lecture de l'erreur: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoriesListResponse> call, Throwable t) {
                isLoading.setValue(false);
                Log.e("AjouterRecViewModel", "Échec de l'appel API pour les catégories: " + t.getMessage(), t);
            }
        });
    }

    private void loadRegions() {
        isLoading.setValue(true);
        Log.d("AjouterRecViewModel", "Début du chargement des régions");
        apiService.getAllRegions().enqueue(new Callback<RegionsListResponse>() {
            @Override
            public void onResponse(Call<RegionsListResponse> call, Response<RegionsListResponse> response) {
                isLoading.setValue(false);
                Log.d("AjouterRecViewModel", "Réponse reçue pour les régions: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    List<Region> regionsList = response.body().getRegions();
                    if (regionsList != null) {
                        regions.setValue(regionsList);
                        Log.d("AjouterRecViewModel", "Régions chargées: " + regionsList.size());
                    } else {
                        Log.e("AjouterRecViewModel", "Liste de régions null");
                        regions.setValue(new ArrayList<>());
                    }
                } else {
                    try {
                        Log.e("AjouterRecViewModel", "Erreur: " + (response.errorBody() != null ? response.errorBody().string() : "Inconnue"));
                    } catch (Exception e) {
                        Log.e("AjouterRecViewModel", "Erreur lors de la lecture de l'erreur: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<RegionsListResponse> call, Throwable t) {
                isLoading.setValue(false);
                Log.e("AjouterRecViewModel", "Échec de l'appel API pour les régions: " + t.getMessage(), t);
            }
        });
    }

    // Vérifier la connectivité réseau
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        Log.d("AjouterRecViewModel", "État de la connexion réseau: " + (isConnected ? "Connecté" : "Déconnecté"));
        return isConnected;
    }

    // Méthode modifiée pour ajouter une réclamation
    public void addReclamation(Reclamation reclamation, Uri photoUri, Context context, ReclamationCallback callback) {
        try {
            // Vérifier la connexion réseau d'abord
            if (!isNetworkAvailable(context)) {
                callback.onError("Pas de connexion Internet. Veuillez vérifier votre connexion et réessayer.");
                return;
            }

            isLoading.setValue(true);
            Log.d("AjouterRecViewModel", "Début de l'ajout de réclamation");

            if (photoUri == null) {
                // Option 1: Sans photo (JSON)
                sendJsonReclamation(reclamation, callback);
            } else {
                // Option 2: Avec photo (Multipart)
                sendMultipartReclamation(reclamation, photoUri, context, callback);
            }
        } catch (Exception e) {
            isLoading.setValue(false);
            Log.e("AjouterRecViewModel", "Exception générale: " + e.getMessage(), e);
            callback.onError("Erreur inattendue: " + e.getMessage());
        }
    }

    // Envoyer réclamation sans photo (JSON)
    private void sendJsonReclamation(Reclamation reclamation, ReclamationCallback callback) {
        try {
            JSONObject jsonReclamation = new JSONObject();
            jsonReclamation.put("titre", reclamation.getTitre());
            jsonReclamation.put("description", reclamation.getDescription());
            jsonReclamation.put("localisation", reclamation.getLocalisation());
            jsonReclamation.put("categorieId", reclamation.getCategorieId());
            jsonReclamation.put("regionId", reclamation.getRegionId());
            jsonReclamation.put("citoyenId", reclamation.getCitoyenId());

            String jsonStr = jsonReclamation.toString();
            Log.d("AjouterRecViewModel", "JSON réclamation: " + jsonStr);

            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"), jsonStr);

            apiService.createReclamation(requestBody).enqueue(new Callback<ReclamationResponse>() {
                @Override
                public void onResponse(Call<ReclamationResponse> call, Response<ReclamationResponse> response) {
                    handleResponseWithLog(response, callback);
                }

                @Override
                public void onFailure(Call<ReclamationResponse> call, Throwable t) {
                    handleFailure(t, callback);
                }
            });
        } catch (Exception e) {
            isLoading.setValue(false);
            Log.e("AjouterRecViewModel", "Erreur lors de la création du JSON: " + e.getMessage(), e);
            callback.onError("Erreur lors de la préparation des données: " + e.getMessage());
        }
    }

    // Envoyer réclamation avec photo (Multipart)
    private void sendMultipartReclamation(Reclamation reclamation, Uri photoUri, Context context, ReclamationCallback callback) {
        try {
            // Créer un Map pour les données de réclamation
            Map<String, RequestBody> reclamationMap = new HashMap<>();
            reclamationMap.put("titre", createPartFromString(reclamation.getTitre()));
            reclamationMap.put("description", createPartFromString(reclamation.getDescription()));
            reclamationMap.put("localisation", createPartFromString(reclamation.getLocalisation()));
            reclamationMap.put("categorieId", createPartFromString(String.valueOf(reclamation.getCategorieId())));
            reclamationMap.put("regionId", createPartFromString(String.valueOf(reclamation.getRegionId())));
            reclamationMap.put("citoyenId", createPartFromString(String.valueOf(reclamation.getCitoyenId())));

            // Convertir l'Uri en fichier
            File photoFile = createFileFromUri(photoUri, context);

            // Créer la partie photo
            RequestBody photoBody = RequestBody.create(
                    MediaType.parse("image/jpeg"), photoFile);
            MultipartBody.Part photoPart = MultipartBody.Part.createFormData("photo", photoFile.getName(), photoBody);

            Log.d("AjouterRecViewModel", "Photo préparée: " + photoFile.getName() + " (" + photoFile.length() + " bytes)");
            Log.d("AjouterRecViewModel", "Données préparées: " + reclamationMap.size() + " champs");

            // Appel de l'API avec Map de RequestBody et MultipartBody.Part
            apiService.createReclamationWithImage(reclamationMap, photoPart)
                    .enqueue(new Callback<ReclamationResponse>() {
                        @Override
                        public void onResponse(Call<ReclamationResponse> call, Response<ReclamationResponse> response) {
                            handleResponseWithLog(response, callback);
                        }

                        @Override
                        public void onFailure(Call<ReclamationResponse> call, Throwable t) {
                            handleFailure(t, callback);
                        }
                    });
        } catch (IOException e) {
            isLoading.setValue(false);
            Log.e("AjouterRecViewModel", "Erreur lors de la préparation de la photo: " + e.getMessage(), e);
            callback.onError("Erreur lors de la préparation de la photo: " + e.getMessage());
        }
    }

    // Méthode utilitaire pour créer un RequestBody à partir d'une chaîne
    private RequestBody createPartFromString(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value != null ? value : "");
    }

    // Méthode utilitaire pour créer un fichier à partir d'un Uri
    private File createFileFromUri(Uri uri, Context context) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        File photoFile = new File(context.getCacheDir(), "photo_" + System.currentTimeMillis() + ".jpg");

        FileOutputStream outputStream = new FileOutputStream(photoFile);
        byte[] buffer = new byte[4 * 1024]; // 4k buffer
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
        outputStream.flush();
        try {
            if (inputStream != null) inputStream.close();
        } catch (IOException e) {
            Log.e("AjouterRecViewModel", "Erreur lors de la fermeture du flux d'entrée: " + e.getMessage());
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            Log.e("AjouterRecViewModel", "Erreur lors de la fermeture du flux de sortie: " + e.getMessage());
        }

        return photoFile;
    }

    // Méthode pour gérer la réponse avec journalisation améliorée
    private void handleResponseWithLog(Response<ReclamationResponse> response, ReclamationCallback callback) {
        isLoading.setValue(false);
        if (response.isSuccessful()) {
            Log.d("AjouterRecViewModel", "Réclamation soumise avec succès! Code: " + response.code());
            if (response.body() != null) {
                Log.d("AjouterRecViewModel", "Message du serveur: " + response.body().getMessage());
            }
            callback.onSuccess();
        } else {
            String errorMsg = "Erreur serveur: " + response.code();
            try {
                if (response.errorBody() != null) {
                    String errorBody = response.errorBody().string();
                    Log.e("AjouterRecViewModel", "Réponse d'erreur: " + errorBody);
                    errorMsg = response.code() + " - " + errorBody;
                }
            } catch (Exception e) {
                Log.e("AjouterRecViewModel", "Erreur lors de la lecture de l'erreur: " + e.getMessage());
            }
            Log.e("AjouterRecViewModel", "Erreur lors de la soumission: " + errorMsg);

            // Message d'erreur plus convivial en fonction du code d'erreur
            if (response.code() == 404) {
                errorMsg = "Le serveur n'a pas pu traiter cette requête (404)";
            } else if (response.code() >= 500) {
                errorMsg = "Erreur serveur. Veuillez réessayer plus tard (500)";
            }

            callback.onError(errorMsg);
        }
    }

    // Méthode pour gérer les échecs de connexion
    private void handleFailure(Throwable t, ReclamationCallback callback) {
        isLoading.setValue(false);
        Log.e("AjouterRecViewModel", "Échec de la connexion: " + t.getMessage(), t);

        // Message d'erreur plus convivial
        String errorMessage;
        if (t.getMessage() != null && t.getMessage().contains("Unable to resolve host")) {
            errorMessage = "Impossible de se connecter au serveur. Vérifiez votre connexion Internet.";
        } else if (t.getMessage() != null && t.getMessage().contains("timeout")) {
            errorMessage = "La connexion au serveur a pris trop de temps. Veuillez réessayer.";
        } else {
            errorMessage = "Erreur de connexion: " + t.getMessage();
        }

        callback.onError(errorMessage);
    }
}
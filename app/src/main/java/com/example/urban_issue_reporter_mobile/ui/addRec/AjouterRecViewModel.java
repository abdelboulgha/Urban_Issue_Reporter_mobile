package com.example.urban_issue_reporter_mobile.ui.addRec;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.urban_issue_reporter_mobile.api.ReclamationRequest;
import com.example.urban_issue_reporter_mobile.api.ReclamationResponse;
import com.example.urban_issue_reporter_mobile.api.RetrofitClient;
import com.example.urban_issue_reporter_mobile.model.Categorie;
import com.example.urban_issue_reporter_mobile.model.Reclamation;
import com.example.urban_issue_reporter_mobile.model.Region;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AjouterRecViewModel extends ViewModel {

    public interface ReclamationCallback {
        void onSuccess();
        void onError(String message);
    }

    public interface DataCallback<T> {
        void onSuccess(List<T> data);
        void onError(String message);
    }

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<List<Categorie>> categories = new MutableLiveData<>();
    private MutableLiveData<List<Region>> regions = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public AjouterRecViewModel() {
        // Charger les données au moment de la création du ViewModel
        loadCategories();
        loadRegions();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<List<Categorie>> getCategories() {
        return categories;
    }

    public LiveData<List<Region>> getRegions() {
        return regions;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // Méthode pour charger les catégories depuis l'API
    public void loadCategories() {
        isLoading.setValue(true);

        RetrofitClient.getInstance().getApiService().getAllCategories()
                .enqueue(new Callback<List<Categorie>>() {
                    @Override
                    public void onResponse(Call<List<Categorie>> call, Response<List<Categorie>> response) {
                        isLoading.setValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            categories.setValue(response.body());
                        } else {
                            errorMessage.setValue("Erreur lors du chargement des catégories: " + response.code());
                            categories.setValue(new ArrayList<>()); // Liste vide en cas d'erreur
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Categorie>> call, Throwable t) {
                        isLoading.setValue(false);
                        errorMessage.setValue("Erreur réseau: " + t.getMessage());
                        categories.setValue(new ArrayList<>()); // Liste vide en cas d'erreur
                        Log.e("AjouterRecVM", "Erreur de chargement des catégories", t);
                    }
                });
    }

    // Méthode pour charger les régions depuis l'API
    public void loadRegions() {
        isLoading.setValue(true);

        RetrofitClient.getInstance().getApiService().getAllRegions()
                .enqueue(new Callback<List<Region>>() {
                    @Override
                    public void onResponse(Call<List<Region>> call, Response<List<Region>> response) {
                        isLoading.setValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            regions.setValue(response.body());
                        } else {
                            errorMessage.setValue("Erreur lors du chargement des régions: " + response.code());
                            regions.setValue(new ArrayList<>()); // Liste vide en cas d'erreur
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Region>> call, Throwable t) {
                        isLoading.setValue(false);
                        errorMessage.setValue("Erreur réseau: " + t.getMessage());
                        regions.setValue(new ArrayList<>()); // Liste vide en cas d'erreur
                        Log.e("AjouterRecVM", "Erreur de chargement des régions", t);
                    }
                });
    }

    // Méthode pour soumettre une réclamation sans image
    public void addReclamation(Reclamation reclamation, ReclamationCallback callback) {
        isLoading.setValue(true);

        // Création de l'objet de requête
        ReclamationRequest request = new ReclamationRequest(
                reclamation.getTitre(),
                reclamation.getDescription(),
                reclamation.getLocalisation(),
                reclamation.getCitoyenId(),
                reclamation.getCategorieId(),
                reclamation.getRegionId()
        );

        // Appel à l'API
        RetrofitClient.getInstance().getApiService().createReclamation(request)
                .enqueue(new Callback<ReclamationResponse>() {
                    @Override
                    public void onResponse(Call<ReclamationResponse> call, Response<ReclamationResponse> response) {
                        isLoading.setValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess();
                        } else {
                            String errorMsg = "Erreur: " + response.code();
                            try {
                                if (response.errorBody() != null) {
                                    errorMsg = response.errorBody().string();
                                }
                            } catch (IOException e) {
                                Log.e("AjouterRecVM", "Erreur lors de la lecture de l'erreur", e);
                            }
                            callback.onError(errorMsg);
                        }
                    }

                    @Override
                    public void onFailure(Call<ReclamationResponse> call, Throwable t) {
                        isLoading.setValue(false);
                        callback.onError("Erreur de connexion: " + t.getMessage());
                        Log.e("AjouterRecVM", "Erreur de connexion", t);
                    }
                });
    }

    // Méthode pour soumettre une réclamation avec image
    public void addReclamation(Reclamation reclamation, Uri photoUri, ContentResolver contentResolver, ReclamationCallback callback) {
        isLoading.setValue(true);

        if (photoUri == null) {
            // Si pas d'image, utiliser la méthode sans image
            addReclamation(reclamation, callback);
            return;
        }

        try {
            // Conversion de la réclamation en JSON pour la partie RequestBody
            String reclamationJson = "{" +
                    "\"titre\":\"" + reclamation.getTitre() + "\"," +
                    "\"description\":\"" + reclamation.getDescription() + "\"," +
                    "\"statut\":\"en_attente\"," +
                    "\"localisation\":\"" + reclamation.getLocalisation() + "\"," +
                    "\"nombre_de_votes\":0," +
                    "\"citoyenId\":" + reclamation.getCitoyenId() + "," +
                    "\"categorieId\":" + reclamation.getCategorieId() + "," +
                    "\"regionId\":" + reclamation.getRegionId() +
                    "}";

            RequestBody reclamationBody = RequestBody.create(
                    MediaType.parse("application/json"), reclamationJson);

            // Préparation du fichier pour l'upload
            File photoFile = createTempFileFromUri(contentResolver, photoUri);
            String mimeType = getMimeType(contentResolver, photoUri);

            if (photoFile == null || mimeType == null) {
                callback.onError("Erreur lors de la préparation de l'image");
                isLoading.setValue(false);
                return;
            }

            RequestBody photoRequestBody = RequestBody.create(
                    MediaType.parse(mimeType), photoFile);

            MultipartBody.Part photoPart = MultipartBody.Part.createFormData(
                    "photo", photoFile.getName(), photoRequestBody);

            // Appel à l'API
            RetrofitClient.getInstance().getApiService().createReclamationWithImage(reclamationBody, photoPart)
                    .enqueue(new Callback<ReclamationResponse>() {
                        @Override
                        public void onResponse(Call<ReclamationResponse> call, Response<ReclamationResponse> response) {
                            isLoading.setValue(false);
                            if (response.isSuccessful() && response.body() != null) {
                                callback.onSuccess();
                            } else {
                                String errorMsg = "Erreur: " + response.code();
                                try {
                                    if (response.errorBody() != null) {
                                        errorMsg = response.errorBody().string();
                                    }
                                } catch (IOException e) {
                                    Log.e("AjouterRecVM", "Erreur lors de la lecture de l'erreur", e);
                                }
                                callback.onError(errorMsg);
                            }
                        }

                        @Override
                        public void onFailure(Call<ReclamationResponse> call, Throwable t) {
                            isLoading.setValue(false);
                            callback.onError("Erreur de connexion: " + t.getMessage());
                            Log.e("AjouterRecVM", "Erreur de connexion", t);
                        }
                    });

        } catch (Exception e) {
            isLoading.setValue(false);
            callback.onError("Erreur: " + e.getMessage());
            Log.e("AjouterRecVM", "Erreur lors de l'envoi", e);
        }
    }

    // Méthode utilitaire pour créer un fichier temporaire depuis un Uri
    private File createTempFileFromUri(ContentResolver contentResolver, Uri uri) throws IOException {
        InputStream inputStream = contentResolver.openInputStream(uri);
        if (inputStream == null) return null;

        File tempFile = File.createTempFile("photo", ".jpg", null);
        tempFile.deleteOnExit();

        OutputStream outputStream = new FileOutputStream(tempFile);
        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.close();
        inputStream.close();

        return tempFile;
    }

    // Méthode utilitaire pour obtenir le MIME type d'un fichier
    private String getMimeType(ContentResolver contentResolver, Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals("content")) {
            mimeType = contentResolver.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        return mimeType;
    }
}
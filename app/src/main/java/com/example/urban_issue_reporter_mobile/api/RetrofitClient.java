package com.example.urban_issue_reporter_mobile.api;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://urbanissuereporter-86jk0m0e.b4a.run/api/";

    public static ApiService getInstance() {
        if (retrofit == null) {
            // Logging très détaillé
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message ->
                    Log.d("RetrofitAPI", message));
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Client OkHttp avec timeouts augmentés et retries
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    // Ajouter un intercepteur pour les en-têtes et les tentatives de reconnexion
                    .addInterceptor(chain -> {
                        Request original = chain.request();

                        // Ajouter des en-têtes de requête
                        Request request = original.newBuilder()
                                .header("Accept", "application/json")
                                .header("Content-Type", "application/json")
                                .method(original.method(), original.body())
                                .build();

                        // Journaliser l'URL complète pour le débogage
                        Log.d("RetrofitClient", "Sending request to: " + request.url().toString());

                        return chain.proceed(request);
                    })
                    .connectTimeout(120, TimeUnit.SECONDS)   // Augmenté à 2 minutes
                    .readTimeout(120, TimeUnit.SECONDS)      // Augmenté à 2 minutes
                    .writeTimeout(120, TimeUnit.SECONDS)     // Augmenté à 2 minutes
                    .retryOnConnectionFailure(true)         // Activer les tentatives de reconnexion
                    .build();

            try {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Log.d("RetrofitClient", "Retrofit instance created successfully for " + BASE_URL);
            } catch (Exception e) {
                Log.e("RetrofitClient", "Error creating Retrofit instance: " + e.getMessage(), e);
            }
        }
        return retrofit.create(ApiService.class);
    }
}
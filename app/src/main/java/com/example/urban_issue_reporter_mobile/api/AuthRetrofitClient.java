package com.example.urban_issue_reporter_mobile.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthRetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://urbanissuereporter-86jk0m0e.b4a.run/api/";

    public static AuthApiService getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(AuthApiService.class);
    }
}

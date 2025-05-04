package com.example.urban_issue_reporter_mobile.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.urban_issue_reporter_mobile.model.Citoyen;
import com.example.urban_issue_reporter_mobile.model.SigninRequest;
import com.example.urban_issue_reporter_mobile.model.SigninResponse;
import com.example.urban_issue_reporter_mobile.model.SignupResponse;
import com.example.urban_issue_reporter_mobile.network.AuthApiService;
import com.example.urban_issue_reporter_mobile.network.AuthRetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private AuthApiService apiService;

    public AuthRepository() {
        apiService = AuthRetrofitClient.getInstance();
    }

    public void signup(Citoyen citoyen,
                       MutableLiveData<SignupResponse> responseData,
                       MutableLiveData<String> errorData) {

        apiService.signup(citoyen).enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    responseData.setValue(response.body());
                } else {
                    errorData.setValue("Erreur " + response.code() + (response.errorBody() != null ?
                            ": " + response.errorBody().toString() : ""));
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                errorData.setValue("Échec de connexion: " + t.getMessage());
            }
        });
    }

    public void signin(String email, String password,
                       MutableLiveData<SigninResponse> responseData,
                       MutableLiveData<String> errorData) {

        SigninRequest signinRequest = new SigninRequest(email, password);

        apiService.signin(signinRequest).enqueue(new Callback<SigninResponse>() {
            @Override
            public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    responseData.setValue(response.body());
                } else {
                    errorData.setValue("Erreur " + response.code() + (response.errorBody() != null ?
                            ": " + response.errorBody().toString() : ""));
                }
            }

            @Override
            public void onFailure(Call<SigninResponse> call, Throwable t) {
                errorData.setValue("Échec de connexion: " + t.getMessage());
            }
        });
    }
}
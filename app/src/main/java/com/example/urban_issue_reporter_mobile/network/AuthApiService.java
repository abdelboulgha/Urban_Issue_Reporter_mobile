package com.example.urban_issue_reporter_mobile.network;

import com.example.urban_issue_reporter_mobile.model.Citoyen;
import com.example.urban_issue_reporter_mobile.model.SigninRequest;
import com.example.urban_issue_reporter_mobile.model.SigninResponse;
import com.example.urban_issue_reporter_mobile.model.SignupResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApiService {
    @POST("citoyen-auth/signup")
    Call<SignupResponse> signup(@Body Citoyen citoyen);

    @POST("citoyen-auth/signin")
    Call<SigninResponse> signin(@Body SigninRequest signinRequest);
}
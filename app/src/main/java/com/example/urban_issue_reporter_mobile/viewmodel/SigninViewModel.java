package com.example.urban_issue_reporter_mobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.urban_issue_reporter_mobile.model.SigninResponse;
import com.example.urban_issue_reporter_mobile.repository.AuthRepository;

public class SigninViewModel extends ViewModel {
    private MutableLiveData<SigninResponse> signinResponse;
    private MutableLiveData<String> errorMessage;
    private MutableLiveData<Boolean> isLoading;
    private AuthRepository repository;

    public SigninViewModel() {
        signinResponse = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
        repository = new AuthRepository();
    }

    public LiveData<SigninResponse> getSigninResponse() {
        return signinResponse;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void signin(String email, String password) {
        // Validation basique des champs
        if (email.isEmpty() || password.isEmpty()) {
            errorMessage.setValue("Email et mot de passe sont obligatoires");
            return;
        }

        // Validation simple de l'email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage.setValue("Format d'email invalide");
            return;
        }

        isLoading.setValue(true);
        repository.signin(email, password, signinResponse, errorMessage);
        isLoading.setValue(false);
    }
}

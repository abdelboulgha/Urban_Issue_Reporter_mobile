package com.example.urban_issue_reporter_mobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.urban_issue_reporter_mobile.model.Citoyen;
import com.example.urban_issue_reporter_mobile.model.SignupResponse;
import com.example.urban_issue_reporter_mobile.repository.AuthRepository;

public class SignupViewModel extends ViewModel {
    private MutableLiveData<SignupResponse> signupResponse;
    private MutableLiveData<String> errorMessage;
    private MutableLiveData<Boolean> isLoading;
    private AuthRepository repository;

    public SignupViewModel() {
        signupResponse = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
        repository = new AuthRepository();
    }

    public LiveData<SignupResponse> getSignupResponse() {
        return signupResponse;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void signup(String nom, String prenom, String adresse, String cin,
                       String email, String telephone, String password) {
        // Validation basique des champs
        if (nom.isEmpty() || prenom.isEmpty() || adresse.isEmpty() || cin.isEmpty()
                || email.isEmpty() || telephone.isEmpty() || password.isEmpty()) {
            errorMessage.setValue("Tous les champs sont obligatoires");
            return;
        }

        // Validation simple de l'email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage.setValue("Format d'email invalide");
            return;
        }

        // Validation simple du numéro de téléphone (format tunisien)
        if (!telephone.matches("^216[0-9]{8}$")) {
            errorMessage.setValue("Le numéro de téléphone doit être au format: 216XXXXXXXX");
            return;
        }

        isLoading.setValue(true);
        Citoyen citoyen = new Citoyen(nom, prenom, adresse, cin, email, telephone, password);
        repository.signup(citoyen, signupResponse, errorMessage);
        isLoading.setValue(false);
    }
}

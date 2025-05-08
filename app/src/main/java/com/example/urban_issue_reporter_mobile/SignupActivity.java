package com.example.urban_issue_reporter_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.urban_issue_reporter_mobile.R;
import com.example.urban_issue_reporter_mobile.viewmodel.SignupViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class SignupActivity extends AppCompatActivity {

    private SignupViewModel viewModel;
    private TextInputEditText etNom, etPrenom, etAdresse, etCin, etEmail, etTelephone, etPassword;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialiser les vues
        etNom = findViewById(R.id.etNom);
        etPrenom = findViewById(R.id.etPrenom);
        etAdresse = findViewById(R.id.etAdresse);
        etCin = findViewById(R.id.etCin);
        etEmail = findViewById(R.id.etEmail);
        etTelephone = findViewById(R.id.etTelephone);
        etPassword = findViewById(R.id.etPassword);
        progressBar = findViewById(R.id.progressBar);

        // Initialiser le ViewModel
        viewModel = new ViewModelProvider(this).get(SignupViewModel.class);

        // Observer les changements et les erreurs
        setupObservers();

        // Configurer le bouton d'inscription
        findViewById(R.id.btnSignup).setOnClickListener(v -> performSignup());

        // Configurer le lien vers la page de connexion
        findViewById(R.id.tvLogin).setOnClickListener(v -> {
            // Rediriger vers LoginActivity (à implémenter plus tard)
            Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(this, "Fonction de connexion à implémenter", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupObservers() {
        // Observer le chargement
        viewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // Observer les erreurs
        viewModel.getErrorMessage().observe(this, errorMsg -> {
            if (errorMsg != null && !errorMsg.isEmpty()) {
                Toast.makeText(SignupActivity.this, errorMsg, Toast.LENGTH_LONG).show();
            }
        });

        // Observer la réponse d'inscription
        viewModel.getSignupResponse().observe(this, response -> {
            if (response != null) {
                // Sauvegarder le token dans les SharedPreferences
                if (response.getToken() != null) {
                    SharedPreferences prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE);
                    prefs.edit().putString("auth_token", response.getToken()).apply();
                }

                // Afficher un message de succès
                Toast.makeText(SignupActivity.this,
                        "Inscription réussie! Bienvenue " + response.getCitoyen().getPrenom(),
                        Toast.LENGTH_LONG).show();

                // Rediriger vers la page principale (à implémenter)
                // Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                // startActivity(intent);
                // finish();
            }
        });
    }

    private void performSignup() {
        String nom = etNom.getText().toString().trim();
        String prenom = etPrenom.getText().toString().trim();
        String adresse = etAdresse.getText().toString().trim();
        String cin = etCin.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String telephone = etTelephone.getText().toString().trim();
        String password = etPassword.getText().toString();

        viewModel.signup(nom, prenom, adresse, cin, email, telephone, password);
    }
}
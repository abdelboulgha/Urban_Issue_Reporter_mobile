package com.example.urban_issue_reporter_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.urban_issue_reporter_mobile.R;
import com.example.urban_issue_reporter_mobile.viewmodel.SigninViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class SigninActivity extends AppCompatActivity {

    private SigninViewModel viewModel;
    private TextInputEditText etEmail, etPassword;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // Initialiser les vues
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        progressBar = findViewById(R.id.progressBar);

        // Initialiser le ViewModel
        viewModel = new ViewModelProvider(this).get(SigninViewModel.class);

        // Observer les changements et les erreurs
        setupObservers();

        // Configurer le bouton de connexion
        findViewById(R.id.btnSignin).setOnClickListener(v -> performSignin());

        // Configurer le lien vers la page d'inscription
        findViewById(R.id.tvSignup).setOnClickListener(v -> {
            Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        // Configurer le lien "Mot de passe oublié"
        findViewById(R.id.tvForgotPassword).setOnClickListener(v -> {
            // À implémenter si nécessaire
            Toast.makeText(this, "Fonction de récupération de mot de passe à implémenter",
                    Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SigninActivity.this, errorMsg, Toast.LENGTH_LONG).show();
            }
        });

        // Observer la réponse de connexion
        viewModel.getSigninResponse().observe(this, response -> {
            if (response != null) {
                // Sauvegarder le token dans les SharedPreferences
                if (response.getToken() != null) {
                    SharedPreferences prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE);
                    prefs.edit()
                            .putString("auth_token", response.getToken())
                            .putInt("user_id", response.getCitoyen().getId())
                            .putString("user_name", response.getCitoyen().getPrenom() + " " +
                                    response.getCitoyen().getNom())
                            .apply();
                }

                // Afficher un message de succès
                Toast.makeText(SigninActivity.this,
                        "Connexion réussie! Bienvenue " + response.getCitoyen().getPrenom(),
                        Toast.LENGTH_LONG).show();

                // Rediriger vers la page principale (à implémenter)
                Intent intent = new Intent(SigninActivity.this, DrawerActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void performSignin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        viewModel.signin(email, password);
    }
}
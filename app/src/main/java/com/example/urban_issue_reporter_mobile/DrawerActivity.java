package com.example.urban_issue_reporter_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.urban_issue_reporter_mobile.databinding.ActivityDrawerBinding;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDrawerBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarDrawer.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Configuration des destinations de premier niveau
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.fragment1, R.id.fragment2, R.id.nav_reclamations, R.id.nav_mes_reclamations)
                .setOpenableLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_drawer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Important: définir cet écouteur pour gérer les clics sur les éléments de menu
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Gérer le clic sur le bouton de déconnexion
        if (id == R.id.nav_logout) {
            // Effectuer la déconnexion
            logout();
            return true;
        }

        // Gérer les autres éléments du menu avec NavigationUI
        boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
        if (handled) {
            // Fermer le drawer après sélection
            binding.drawerLayout.closeDrawers();
        }
        return handled;
    }

    private void logout() {
        try {
            // Supprimer les informations d'authentification
            SharedPreferences prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE);
            prefs.edit().clear().apply();

            // Afficher un message de déconnexion
            Toast.makeText(this, "Vous êtes déconnecté", Toast.LENGTH_SHORT).show();

            // Rediriger vers la page d'authentification
            Intent intent = new Intent(DrawerActivity.this, SigninActivity.class);
            // Effacer la pile d'activités pour empêcher le retour avec le bouton back
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            // Capturer et afficher toute erreur potentielle
            Toast.makeText(this, "Erreur lors de la déconnexion: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_drawer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
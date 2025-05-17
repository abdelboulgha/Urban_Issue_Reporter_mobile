package com.example.urban_issue_reporter_mobile.ui.addRec;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.urban_issue_reporter_mobile.R;
import com.example.urban_issue_reporter_mobile.databinding.FragmentAjouterRecBinding;
import com.example.urban_issue_reporter_mobile.model.Categorie;
import com.example.urban_issue_reporter_mobile.model.Region;
import com.example.urban_issue_reporter_mobile.ui.beans.Reclamation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AjouterRec extends Fragment {

    private FragmentAjouterRecBinding binding;
    private AjouterRecViewModel ajouterRecViewModel;
    private static final int REQUEST_IMAGE_PICK = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 2;
    private FusedLocationProviderClient fusedLocationClient;
    private Uri selectedImageUri;

    private List<Categorie> categoriesList = new ArrayList<>();
    private List<Region> regionsList = new ArrayList<>();
    private int selectedCategorieId = 0;
    private int selectedRegionId = 0;

    public AjouterRec() {
        // Required empty public constructor
    }

    public static AjouterRec newInstance() {
        return new AjouterRec();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ajouterRecViewModel = new ViewModelProvider(this).get(AjouterRecViewModel.class);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAjouterRecBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initSpinners();
        setupObservers();
        setupButtons();

        return root;
    }

    private void initSpinners() {
        // Initialiser avec des adaptateurs vides jusqu'à ce que les données soient chargées
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new ArrayList<>()
        );
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.categorieSpinner.setAdapter(categoriesAdapter);

        ArrayAdapter<String> regionsAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new ArrayList<>()
        );
        regionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.regionSpinner.setAdapter(regionsAdapter);

        // Écouter les sélections
        binding.categorieSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!categoriesList.isEmpty() && position >= 0 && position < categoriesList.size()) {
                    selectedCategorieId = categoriesList.get(position).getId();
                    Log.d("AjouterRec", "Catégorie sélectionnée: " + categoriesList.get(position).getLibelle() + " (ID: " + selectedCategorieId + ")");
                } else {
                    // Si on utilise les données par défaut (API a échoué)
                    selectedCategorieId = position + 1; // Utiliser l'index comme ID
                    Log.d("AjouterRec", "Catégorie par défaut sélectionnée à l'index: " + position + " (ID: " + selectedCategorieId + ")");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategorieId = 0;
                Log.d("AjouterRec", "Aucune catégorie sélectionnée");
            }
        });

        binding.regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!regionsList.isEmpty() && position >= 0 && position < regionsList.size()) {
                    selectedRegionId = regionsList.get(position).getId();
                    Log.d("AjouterRec", "Région sélectionnée: " + regionsList.get(position).getNom() + " (ID: " + selectedRegionId + ")");
                } else {
                    // Si on utilise les données par défaut (API a échoué)
                    selectedRegionId = position + 1; // Utiliser l'index comme ID
                    Log.d("AjouterRec", "Région par défaut sélectionnée à l'index: " + position + " (ID: " + selectedRegionId + ")");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedRegionId = 0;
                Log.d("AjouterRec", "Aucune région sélectionnée");
            }
        });
    }

    private void setupObservers() {
        // Observer le chargement
        ajouterRecViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.submitButton.setEnabled(!isLoading);
        });

        // Observer les catégories
        ajouterRecViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null && !categories.isEmpty()) {
                // Code existant pour remplir le spinner
                categoriesList = categories;
                List<String> categoriesNames = new ArrayList<>();
                for (Categorie categorie : categories) {
                    categoriesNames.add(categorie.getLibelle());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        categoriesNames
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.categorieSpinner.setAdapter(adapter);
                Log.d("AjouterRec", "Catégories chargées: " + categoriesNames.size());
            } else {
                // Solution de secours: utiliser des données codées en dur
                Log.w("AjouterRec", "Utilisation de catégories par défaut car l'API n'a pas répondu");
                List<String> defaultCategories = new ArrayList<>(Arrays.asList(
                        "Voirie", "Éclairage public", "Propreté", "Espaces verts",
                        "Mobilier urbain", "Transport", "Eau et assainissement", "Autre"
                ));

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        defaultCategories
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.categorieSpinner.setAdapter(adapter);
            }
        });

        // Observer les régions
        ajouterRecViewModel.getRegions().observe(getViewLifecycleOwner(), regions -> {
            if (regions != null && !regions.isEmpty()) {
                regionsList = regions;
                List<String> regionsNames = new ArrayList<>();
                for (Region region : regions) {
                    regionsNames.add(region.getNom());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        regionsNames
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.regionSpinner.setAdapter(adapter);
                Log.d("AjouterRec", "Régions chargées: " + regionsNames.size());
            } else {
                // Solution de secours: utiliser des données codées en dur
                Log.w("AjouterRec", "Utilisation de régions par défaut car l'API n'a pas répondu");
                List<String> defaultRegions = new ArrayList<>(Arrays.asList(
                        "Tanger-Tétouan-Al Hoceima", "Oriental", "Fès-Meknès", "Rabat-Salé-Kénitra",
                        "Béni Mellal-Khénifra", "Casablanca-Settat", "Marrakech-Safi", "Drâa-Tafilalet",
                        "Souss-Massa", "Guelmim-Oued Noun", "Laâyoune-Sakia El Hamra", "Dakhla-Oued Ed-Dahab"
                ));

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        defaultRegions
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.regionSpinner.setAdapter(adapter);
            }
        });
    }

    private void setupButtons() {
        binding.locationButton.setOnClickListener(v -> getLocation());
        binding.photoButton.setOnClickListener(v -> selectImage());
        binding.submitButton.setOnClickListener(v -> submitReclamation());
    }

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // Format location for display
                                String locationString = String.format("%.6f, %.6f",
                                        location.getLatitude(), location.getLongitude());
                                binding.localisationInput.setText(locationString);
                            } else {
                                Toast.makeText(requireContext(),
                                        "Impossible d'obtenir la localisation actuelle",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Sélectionner une image"), REQUEST_IMAGE_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
                binding.photoPreview.setImageBitmap(bitmap);
                binding.photoPreview.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Erreur lors du chargement de l'image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(requireContext(),
                        "Permission de localisation refusée",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void submitReclamation() {
        // Validation des champs
        if (binding.titreInput.getText().toString().isEmpty()) {
            binding.titreLayout.setError("Le titre est requis");
            return;
        } else {
            binding.titreLayout.setError(null);
        }

        if (binding.descriptionInput.getText().toString().isEmpty()) {
            binding.descriptionLayout.setError("La description est requise");
            return;
        } else {
            binding.descriptionLayout.setError(null);
        }

        if (binding.localisationInput.getText().toString().isEmpty()) {
            binding.localisationLayout.setError("La localisation est requise");
            return;
        } else {
            binding.localisationLayout.setError(null);
        }

        if (selectedCategorieId == 0 || selectedRegionId == 0) {
            Toast.makeText(requireContext(), "Veuillez sélectionner une catégorie et une région", Toast.LENGTH_SHORT).show();
            return;
        }

        // Création de l'objet Reclamation
        Reclamation reclamation = new Reclamation();
        reclamation.setTitre(binding.titreInput.getText().toString());
        reclamation.setDescription(binding.descriptionInput.getText().toString());
        reclamation.setLocalisation(binding.localisationInput.getText().toString());
        reclamation.setCategorieId(selectedCategorieId);
        reclamation.setRegionId(selectedRegionId);

        // Récupérer l'ID de l'utilisateur connecté depuis SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        if (userId == -1) {
            // Utilisateur non connecté - utiliser l'ID 1 pour le test
            userId = 1; // ID fictif pour le test
            Toast.makeText(requireContext(), "Utilisation d'un ID utilisateur de test (1)", Toast.LENGTH_SHORT).show();
        }

        reclamation.setCitoyenId(userId);

        // Logs dans la méthode de soumission
        Log.d("AjouterRec", "Soumission d'une réclamation...");
        Log.d("AjouterRec", "Titre: " + reclamation.getTitre());
        Log.d("AjouterRec", "Description: " + reclamation.getDescription());
        Log.d("AjouterRec", "Localisation: " + reclamation.getLocalisation());
        Log.d("AjouterRec", "CategorieId: " + selectedCategorieId);
        Log.d("AjouterRec", "RegionId: " + selectedRegionId);
        Log.d("AjouterRec", "UserId: " + userId);
        Log.d("AjouterRec", "Photo: " + (selectedImageUri != null ? selectedImageUri.toString() : "aucune"));

        // Envoi de la réclamation via le ViewModel
        ajouterRecViewModel.addReclamation(reclamation, selectedImageUri, requireContext(), new AjouterRecViewModel.ReclamationCallback() {
            @Override
            public void onSuccess() {
                Log.d("AjouterRec", "Réclamation soumise avec succès!");
                // Affichage d'un message de succès
                if (isAdded() && getContext() != null) {
                    Snackbar.make(binding.getRoot(),
                            "Réclamation soumise avec succès !",
                            Snackbar.LENGTH_LONG).show();

                    // Réinitialisation du formulaire
                    binding.titreInput.setText("");
                    binding.descriptionInput.setText("");
                    binding.localisationInput.setText("");
                    binding.categorieSpinner.setSelection(0);
                    binding.regionSpinner.setSelection(0);
                    binding.photoPreview.setVisibility(View.GONE);
                    selectedImageUri = null;
                }
            }

            @Override
            public void onError(String message) {
                Log.e("AjouterRec", "Erreur lors de la soumission: " + message);
                // Affichage d'un message d'erreur
                if (isAdded() && getContext() != null) {
                    Snackbar.make(binding.getRoot(),
                            "Erreur: " + message,
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
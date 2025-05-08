package com.example.urban_issue_reporter_mobile.ui.addRec;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.urban_issue_reporter_mobile.R;
import com.example.urban_issue_reporter_mobile.databinding.FragmentAjouterRecBinding;
import com.example.urban_issue_reporter_mobile.model.Categorie;
import com.example.urban_issue_reporter_mobile.model.Reclamation;
import com.example.urban_issue_reporter_mobile.model.Region;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AjouterRec extends Fragment {

    private FragmentAjouterRecBinding binding;
    private AjouterRecViewModel ajouterRecViewModel;
    private static final int REQUEST_IMAGE_PICK = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 2;
    private FusedLocationProviderClient fusedLocationClient;
    private Uri selectedImageUri;
    private ProgressDialog progressDialog;
    private List<Categorie> categoriesList = new ArrayList<>();
    private List<Region> regionsList = new ArrayList<>();

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

        // Observer pour afficher/masquer le dialogue de chargement
        ajouterRecViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    showProgressDialog();
                } else {
                    hideProgressDialog();
                }
            }
        });

        // Observer pour les catégories
        ajouterRecViewModel.getCategories().observe(this, new Observer<List<Categorie>>() {
            @Override
            public void onChanged(List<Categorie> categories) {
                if (categories != null) {
                    categoriesList = categories;
                    setupCategorieSpinner();
                } else {
                    Toast.makeText(requireContext(), "Erreur: Impossible de charger les catégories", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Observer pour les régions
        ajouterRecViewModel.getRegions().observe(this, new Observer<List<Region>>() {
            @Override
            public void onChanged(List<Region> regions) {
                if (regions != null) {
                    regionsList = regions;
                    setupRegionSpinner();
                } else {
                    Toast.makeText(requireContext(), "Erreur: Impossible de charger les régions", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Observer pour les messages d'erreur
        ajouterRecViewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                if (message != null && !message.isEmpty()) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
                }
            }
        });

        // Forcer le chargement initial des données
        ajouterRecViewModel.loadCategories();
        ajouterRecViewModel.loadRegions();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAjouterRecBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupButtons();

        return root;
    }

    private void setupCategorieSpinner() {
        if (getContext() == null) return;

        try {
            if (categoriesList == null || categoriesList.isEmpty()) {
                // Ajouter une catégorie par défaut si la liste est vide pour éviter les erreurs
                Toast.makeText(requireContext(), "Attention: Aucune catégorie disponible", Toast.LENGTH_SHORT).show();
                categoriesList = new ArrayList<>();
                Categorie defaultCategorie = new Categorie();
                defaultCategorie.setId(0);
                defaultCategorie.setLibelle("Catégorie par défaut");
                defaultCategorie.setDescription("Catégorie temporaire");
                categoriesList.add(defaultCategorie);
            }

            ArrayAdapter<Categorie> adapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    categoriesList
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            if (binding != null) {
                binding.categorieSpinner.setAdapter(adapter);
                binding.categorieSpinner.setSelection(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Erreur lors de l'initialisation du spinner de catégories: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setupRegionSpinner() {
        if (getContext() == null) return;

        try {
            if (regionsList == null || regionsList.isEmpty()) {
                // Ajouter une région par défaut si la liste est vide pour éviter les erreurs
                Toast.makeText(requireContext(), "Attention: Aucune région disponible", Toast.LENGTH_SHORT).show();
                regionsList = new ArrayList<>();
                Region defaultRegion = new Region();
                defaultRegion.setId(0);
                defaultRegion.setNom("Région par défaut");
                defaultRegion.setDescription("Région temporaire");
                regionsList.add(defaultRegion);
            }

            ArrayAdapter<Region> adapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    regionsList
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            if (binding != null) {
                binding.regionSpinner.setAdapter(adapter);
                binding.regionSpinner.setSelection(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Erreur lors de l'initialisation du spinner de régions: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
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

        // Vérification que les listes de catégories et régions ne sont pas vides
        if (categoriesList.isEmpty()) {
            Toast.makeText(requireContext(), "Erreur: Aucune catégorie disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        if (regionsList.isEmpty()) {
            Toast.makeText(requireContext(), "Erreur: Aucune région disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        // Récupération des objets sélectionnés directement
        Categorie categorieSelectionnee = (Categorie) binding.categorieSpinner.getSelectedItem();
        Region regionSelectionnee = (Region) binding.regionSpinner.getSelectedItem();

        if (categorieSelectionnee == null) {
            Toast.makeText(requireContext(), "Erreur: Veuillez sélectionner une catégorie", Toast.LENGTH_SHORT).show();
            return;
        }

        if (regionSelectionnee == null) {
            Toast.makeText(requireContext(), "Erreur: Veuillez sélectionner une région", Toast.LENGTH_SHORT).show();
            return;
        }

        // Création de l'objet Reclamation
        Reclamation reclamation = new Reclamation();
        reclamation.setTitre(binding.titreInput.getText().toString());
        reclamation.setDescription(binding.descriptionInput.getText().toString());
        reclamation.setLocalisation(binding.localisationInput.getText().toString());

        // Assignation directe des IDs à partir des objets sélectionnés avec conversion explicite
        // Conversion du long vers le type attendu par la méthode setCategorieId()
        long categorieId = categorieSelectionnee.getId();
        long regionId = regionSelectionnee.getId();

        // Si setCategorieId() attend un int
        reclamation.setCategorieId((int) categorieId);
        reclamation.setRegionId((int) regionId);

        // Alternative: si setCategorieId() attend une String
        // reclamation.setCategorieId(String.valueOf(categorieId));
        // reclamation.setRegionId(String.valueOf(regionId));

        // Dans une vraie application, vous récupéreriez l'ID du citoyen connecté
        reclamation.setCitoyenId(1); // ID fictif pour la démonstration

        // Envoi de la réclamation via le ViewModel
        ajouterRecViewModel.addReclamation(reclamation, selectedImageUri,
                requireActivity().getContentResolver(),
                new AjouterRecViewModel.ReclamationCallback() {
                    @Override
                    public void onSuccess() {
                        requireActivity().runOnUiThread(() -> {
                            // Affichage d'un message de succès
                            Snackbar.make(binding.getRoot(),
                                    "Réclamation soumise avec succès !",
                                    Snackbar.LENGTH_LONG).show();

                            // Réinitialisation du formulaire
                            resetForm();
                        });
                    }

                    @Override
                    public void onError(String message) {
                        requireActivity().runOnUiThread(() -> {
                            // Affichage d'un message d'erreur
                            Snackbar.make(binding.getRoot(),
                                    "Erreur: " + message,
                                    Snackbar.LENGTH_LONG).show();
                        });
                    }
                });
    }

    private void resetForm() {
        binding.titreInput.setText("");
        binding.descriptionInput.setText("");
        binding.localisationInput.setText("");
        if (!categoriesList.isEmpty()) binding.categorieSpinner.setSelection(0);
        if (!regionsList.isEmpty()) binding.regionSpinner.setSelection(0);
        binding.photoPreview.setVisibility(View.GONE);
        selectedImageUri = null;
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(requireContext());
            progressDialog.setMessage("Chargement en cours...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgressDialog();
        binding = null;
    }
}
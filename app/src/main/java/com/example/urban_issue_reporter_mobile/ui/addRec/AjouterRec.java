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
    private ProgressDialog progressDialog;

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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAjouterRecBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupCategorieSpinner();
        setupRegionSpinner();
        setupButtons();

        return root;
    }

    private void setupCategorieSpinner() {
        // Exemple de catégories - à remplacer par des données réelles
        List<String> categories = new ArrayList<>(Arrays.asList(
                "Voirie", "Éclairage public", "Propreté", "Espaces verts",
                "Mobilier urbain", "Transport", "Eau et assainissement", "Autre"
        ));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.categorieSpinner.setAdapter(adapter);
    }

    private void setupRegionSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.regions_maroc,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.regionSpinner.setAdapter(adapter);
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

        // Création de l'objet Reclamation
        Reclamation reclamation = new Reclamation();
        reclamation.setTitre(binding.titreInput.getText().toString());
        reclamation.setDescription(binding.descriptionInput.getText().toString());
        reclamation.setLocalisation(binding.localisationInput.getText().toString());

        // Récupération des IDs pour catégorie et région
        reclamation.setCategorieId(binding.categorieSpinner.getSelectedItemPosition() + 1);
        reclamation.setRegionId(binding.regionSpinner.getSelectedItemPosition() + 1);

        // Dans une vraie application, vous récupéreriez l'ID du citoyen connecté
        // Par exemple, depuis un stockage sécurisé ou une session
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
        binding.categorieSpinner.setSelection(0);
        binding.regionSpinner.setSelection(0);
        binding.photoPreview.setVisibility(View.GONE);
        selectedImageUri = null;
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(requireContext());
            progressDialog.setMessage("Soumission en cours...");
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
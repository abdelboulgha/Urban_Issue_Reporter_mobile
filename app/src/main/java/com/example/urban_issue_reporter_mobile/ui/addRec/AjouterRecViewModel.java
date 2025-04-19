package com.example.urban_issue_reporter_mobile.ui.addRec;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.example.urban_issue_reporter_mobile.ui.beans.Reclamation;

public class AjouterRecViewModel extends ViewModel {

    public interface ReclamationCallback {
        void onSuccess();
        void onError(String message);
    }

    // Cette méthode serait implémentée pour communiquer avec votre API
    public void addReclamation(Reclamation reclamation, Uri photoUri, ReclamationCallback callback) {
        // Simuler un délai réseau
        new Thread(() -> {
            try {
                // Ici vous implémenteriez l'appel à votre API REST
                // Exemple: envoi de la réclamation au serveur
                Thread.sleep(1500); // Simulating network delay

                // Pour la démonstration, on simule un succès
                if (callback != null) {
                    callback.onSuccess();
                }
            } catch (Exception e) {
                if (callback != null) {
                    callback.onError(e.getMessage());
                }
            }
        }).start();
    }

    // Dans une application réelle, vous ajouteriez ici le code pour:
    // 1. Récupérer les catégories depuis votre API
    // 2. Récupérer les régions depuis votre API
    // 3. Uploader l'image vers votre serveur
    // 4. Gérer l'état des opérations en cours (loading, error, success)
}
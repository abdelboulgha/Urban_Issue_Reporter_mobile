package com.example.urban_issue_reporter_mobile.api;

import com.google.gson.annotations.SerializedName;

public class ReclamationRequest {
    @SerializedName("titre")
    private String titre;

    @SerializedName("description")
    private String description;

    @SerializedName("statut")
    private String statut;

    @SerializedName("localisation")
    private String localisation;

    @SerializedName("nombre_de_votes")
    private int nombreDeVotes;

    @SerializedName("citoyenId")
    private long citoyenId;

    @SerializedName("categorieId")
    private long categorieId;

    @SerializedName("regionId")
    private long regionId;

    // Constructeur
    public ReclamationRequest(String titre, String description, String localisation,
                              long citoyenId, long categorieId, long regionId) {
        this.titre = titre;
        this.description = description;
        this.statut = "en_attente"; // Statut par défaut
        this.localisation = localisation;
        this.nombreDeVotes = 0;  // Par défaut
        this.citoyenId = citoyenId;
        this.categorieId = categorieId;
        this.regionId = regionId;
    }
}
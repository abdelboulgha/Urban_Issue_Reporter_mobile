package com.example.urban_issue_reporter_mobile.model;

import com.google.gson.annotations.SerializedName;

public class Reclamation {
    private int id;
    private String date_de_creation;
    private String titre;
    private String description;
    private String statut;
    @SerializedName("nombre_de_votes")
    private int nombreDeVotes;
    private String localisation;
    private Integer citoyenId;
    private Integer categorieId;
    private Integer regionId;
    private Region region; // Added Region object
    private Categorie categorie; // Added Categorie object

    // Getters and Setters for each field
    public int getId() { return id; }
    public String getDateDeCreation() { return date_de_creation; }
    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public String getStatut() { return statut; }
    public int getNombreDeVotes() { return nombreDeVotes; }
    public void setNombreDeVotes(int nombreDeVotes) {
        this.nombreDeVotes = nombreDeVotes;
    }
    public String getLocalisation() { return localisation; }
    public Integer getCitoyenId() { return citoyenId; }
    public Integer getCategorieId() { return categorieId; }
    public Integer getRegionId() { return regionId; }

    // Add getters for Region and Categorie
    public Region getRegion() { return region; }
    public Categorie getCategorie() { return categorie; }

    // Setters for Region and Categorie
    public void setRegion(Region region) { this.region = region; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", date_de_creation='" + date_de_creation + '\'' +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", statut='" + statut + '\'' +
                ", nombreDeVotes=" + nombreDeVotes +
                ", localisation='" + localisation + '\'' +
                ", citoyenId=" + citoyenId +
                ", categorieId=" + categorieId +
                ", regionId=" + regionId +
                ", region=" + region +
                ", categorie=" + categorie +
                '}';
    }

    // Corriger les setters vides
    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public void setCategorieId(int categorieId) {
        this.categorieId = categorieId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public void setCitoyenId(int citoyenId) {
        this.citoyenId = citoyenId;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
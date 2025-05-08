package com.example.urban_issue_reporter_mobile.model;

public class Reclamation {
    private int id;
    private String date_de_creation;
    private String titre;
    private String description;
    private String statut;
    private int nombre_de_votes;
    private String localisation;
    private Integer citoyenId;
    private Integer categorieId;
    private Integer regionId;

    // Getters
    public int getId() { return id; }
    public String getDate_de_creation() { return date_de_creation; }
    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public String getStatut() { return statut; }
    public int getNombre_de_votes() { return nombre_de_votes; }
    public String getLocalisation() { return localisation; }
    public Integer getCitoyenId() { return citoyenId; }
    public Integer getCategorieId() { return categorieId; }
    public Integer getRegionId() { return regionId; }

    // Setters (ajoutés)
    public void setId(int id) { this.id = id; }
    public void setDate_de_creation(String date_de_creation) { this.date_de_creation = date_de_creation; }
    public void setTitre(String titre) { this.titre = titre; }
    public void setDescription(String description) { this.description = description; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setNombre_de_votes(int nombre_de_votes) { this.nombre_de_votes = nombre_de_votes; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }
    public void setCitoyenId(Integer citoyenId) { this.citoyenId = citoyenId; }
    public void setCategorieId(Integer categorieId) { this.categorieId = categorieId; }
    public void setRegionId(Integer regionId) { this.regionId = regionId; }
}
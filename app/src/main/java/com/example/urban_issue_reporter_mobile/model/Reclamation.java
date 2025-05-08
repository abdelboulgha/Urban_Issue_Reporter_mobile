package com.example.urban_issue_reporter_mobile.model;

public class Reclamation {
    private int id;
    private String date_de_creation; // ou ZonedDateTime si tu veux gérer le fuseau
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

    // Setters (si nécessaires pour POST/PUT)
}


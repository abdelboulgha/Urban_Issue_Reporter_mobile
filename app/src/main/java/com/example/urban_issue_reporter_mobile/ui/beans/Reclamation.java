package com.example.urban_issue_reporter_mobile.ui.beans;

import java.util.Date;

public class Reclamation {
    private long id;
    private Date dateDeCreation;
    private String titre;
    private String description;
    private String statut;
    private int nombreDeVotes;
    private String localisation;
    private long citoyenId;
    private long categorieId;
    private long regionId;

    // Constantes pour les statuts
    public static final String STATUT_EN_ATTENTE = "en_attente";
    public static final String STATUT_EN_COURS = "en_cours";
    public static final String STATUT_RESOLUE = "résolue";
    public static final String STATUT_REJETEE = "rejetée";

    // Constructeurs
    public Reclamation() {
        this.dateDeCreation = new Date();
        this.statut = STATUT_EN_ATTENTE;
        this.nombreDeVotes = 0;
    }

    public Reclamation(String titre, String description, String localisation,
                       long citoyenId, long categorieId, long regionId) {
        this();
        this.titre = titre;
        this.description = description;
        this.localisation = localisation;
        this.citoyenId = citoyenId;
        this.categorieId = categorieId;
        this.regionId = regionId;
    }

    // Getters et Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateDeCreation() {
        return dateDeCreation;
    }

    public void setDateDeCreation(Date dateDeCreation) {
        this.dateDeCreation = dateDeCreation;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public int getNombreDeVotes() {
        return nombreDeVotes;
    }

    public void setNombreDeVotes(int nombreDeVotes) {
        this.nombreDeVotes = nombreDeVotes;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public long getCitoyenId() {
        return citoyenId;
    }

    public void setCitoyenId(long citoyenId) {
        this.citoyenId = citoyenId;
    }

    public long getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(long categorieId) {
        this.categorieId = categorieId;
    }

    public long getRegionId() {
        return regionId;
    }

    public void setRegionId(long regionId) {
        this.regionId = regionId;
    }
}
package com.example.urban_issue_reporter_mobile.model;

import com.google.gson.annotations.SerializedName;

public class Categorie {
    @SerializedName("id")
    private long id;

    @SerializedName("libelle")
    private String libelle;

    @SerializedName("description")
    private String description;

    public Categorie() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return libelle;
    }
}
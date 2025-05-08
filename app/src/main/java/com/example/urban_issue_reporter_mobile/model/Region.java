package com.example.urban_issue_reporter_mobile.model;

import com.google.gson.annotations.SerializedName;

public class Region {
    @SerializedName("id")
    private long id;

    @SerializedName("nom")
    private String nom;

    @SerializedName("description")
    private String description;

    public Region() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return nom;
    }
}
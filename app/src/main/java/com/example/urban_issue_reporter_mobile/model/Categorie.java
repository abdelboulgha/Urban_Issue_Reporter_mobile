package com.example.urban_issue_reporter_mobile.model;

public class Categorie {
    private int id;
    private String libelle;

    // Constructor, Getters, and Setters
    public int getId() { return id; }
    public String getLibelle() { return libelle; }

    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                '}';
    }

    public void setId(int i) {
    }

    public void setLibelle(String catégorieParDéfaut) {
    }

    public void setDescription(String catégorieTemporaire) {
    }
}

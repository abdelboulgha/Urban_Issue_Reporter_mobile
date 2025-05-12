package com.example.urban_issue_reporter_mobile.model;

public class Region {
    private int id;
    private String nom;

    // Constructor, Getters, and Setters
    public int getId() { return id; }
    public String getNom() { return nom; }

    @Override
    public String toString() {
        return "Region{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                '}';
    }

    public void setId(int i) {
    }

    public void setNom(String régionParDéfaut) {
    }

    public void setDescription(String régionTemporaire) {

    }
}

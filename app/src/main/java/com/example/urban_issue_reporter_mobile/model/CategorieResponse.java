// CategorieResponse.java
package com.example.urban_issue_reporter_mobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategorieResponse {
    @SerializedName("categorie")
    private Categorie categorie;

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }
}

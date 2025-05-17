package com.example.urban_issue_reporter_mobile.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CategoriesListResponse {
    @SerializedName("categories")
    private List<Categorie> categories;

    public List<Categorie> getCategories() {
        return categories;
    }

    public void setCategories(List<Categorie> categories) {
        this.categories = categories;
    }
}
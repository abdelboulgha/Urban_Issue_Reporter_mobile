package com.example.urban_issue_reporter_mobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReclamationResponse {
    private String message;

    @SerializedName("reclamations")
    private List<Reclamation> reclamations;

    public String getMessage() {
        return message;
    }

    public List<Reclamation> getReclamations() {
        return reclamations;
    }

    @Override
    public String toString() {
        return "ReclamationResponse{message='" + message + "', reclamations=" + (reclamations != null ? reclamations.size() : "null") + "}";
    }
}
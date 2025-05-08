package com.example.urban_issue_reporter_mobile.api;

import com.example.urban_issue_reporter_mobile.ui.beans.Reclamation;
import com.google.gson.annotations.SerializedName;

public class ReclamationResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("reclamation")
    private Reclamation reclamation;

    public String getMessage() {
        return message;
    }

    public Reclamation getReclamation() {
        return reclamation;
    }
}
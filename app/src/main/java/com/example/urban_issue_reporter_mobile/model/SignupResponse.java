package com.example.urban_issue_reporter_mobile.model;

public class SignupResponse {
    private String message;
    private Citoyen citoyen;
    private String token;

    public String getMessage() {
        return message;
    }

    public Citoyen getCitoyen() {
        return citoyen;
    }

    public String getToken() {
        return token;
    }
}
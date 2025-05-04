package com.example.urban_issue_reporter_mobile.model;

public class Citoyen {
    private Integer id;
    private String nom;
    private String prenom;
    private String adresse;
    private String cin;
    private String email;
    private String telephone;
    private String password;

    // Constructeur par défaut
    public Citoyen() {
    }

    // Constructeur pour l'inscription
    public Citoyen(String nom, String prenom, String adresse, String cin, String email, String telephone, String password) {
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.cin = cin;
        this.email = email;
        this.telephone = telephone;
        this.password = password;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getCin() {
        return cin;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
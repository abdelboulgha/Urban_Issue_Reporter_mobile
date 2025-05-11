package com.example.urban_issue_reporter_mobile.model;

public class Photo {
    private int id;
    private String url;
    private int reclamationId;

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public int getReclamationId() {
        return reclamationId;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", reclamationId=" + reclamationId +
                '}';
    }
}
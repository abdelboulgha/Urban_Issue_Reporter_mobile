package com.example.urban_issue_reporter_mobile.model;



import java.util.List;

public class PhotosResponse {
    private List<Photo> photos;

    // Si la réponse est directement un tableau de photos comme dans votre exemple,
    // alors cette classe n'est pas nécessaire, et vous pouvez utiliser directement
    // List<Photo> comme type de retour dans l'API.

    public List<Photo> getPhotos() {
        return photos;
    }
}

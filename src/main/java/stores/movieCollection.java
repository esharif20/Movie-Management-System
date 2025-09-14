package stores;

import structures.*;

public class movieCollection {
    int collectionID;
    String collectionName;
    String collectionPosterPath;
    String collectionBackdropPath;
    NewArrayList<Integer> films; // The list of film IDs in this collection.
    
    public movieCollection(int collectionID, String collectionName, String collectionPosterPath, String collectionBackdropPath) {
        this.collectionID = collectionID;
        this.collectionName = collectionName;
        this.collectionPosterPath = collectionPosterPath;
        this.collectionBackdropPath = collectionBackdropPath;
        this.films = new NewArrayList<>(); // Initialize the list of films.
    }
    
    public void addFilm(int filmID) {
        if (!films.contains(filmID)) {
            films.add(filmID);
        }
    }

    // Corrected method to check if the collection contains a given film.
    public boolean containsFilm(int filmID) {
        return films.contains(filmID);
    }

    // Getters for collection properties
    public int getCollectionID() { return collectionID; }
    public String getCollectionName() { return collectionName; }
    public String getCollectionPosterPath() { return collectionPosterPath; }
    public String getCollectionBackdropPath() { return collectionBackdropPath; }
    public NewArrayList<Integer> getFilmIDs() { return films; } // Correctly returns the list of film IDs.
}

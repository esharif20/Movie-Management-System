package interfaces;

import structures.*;

public interface ICollection {
    int getCollectionID();
    String getCollectionName();
    String getCollectionPosterPath(); 
    String getCollectionBackdropPath(); 
    NewArrayList<Integer> getFilmIDs();
}

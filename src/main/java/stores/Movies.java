package stores;

import java.time.LocalDate;
import java.util.Map;

import interfaces.IMovies;
import structures.*;
import stores.movieCollection;

public class Movies implements IMovies{
    Stores stores;
    //maps unique film id to its structure
    MyHashMap<Integer, movieStructure> movies;
    ////maps unique film id to its movies collection
    MyHashMap<Integer, movieCollection> collections;

    /**
     * The constructor for the Movies data store. This is where you should
     * initialise your data structures.
     * @param stores An object storing all the different key stores,
     *               including itself
     */
    public Movies(Stores stores) {
        this.stores = stores;
        this.movies = new MyHashMap<>();
        this.collections = new MyHashMap<>();
        // TODO Add initialisation of data structure here
    }

    /**
     * Adds data about a film to the data structure
     * 
     * @param id               The unique ID for the film
     * @param title            The English title of the film
     * @param originalTitle    The original language title of the film
     * @param overview         An overview of the film
     * @param tagline          The tagline for the film (empty string if there is no
     *                         tagline)
     * @param status           Current status of the film
     * @param genres           An array of Genre objects related to the film
     * @param release          The release date for the film
     * @param budget           The budget of the film in US Dollars
     * @param revenue          The revenue of the film in US Dollars
     * @param languages        An array of ISO 639 language codes for the film
     * @param originalLanguage An ISO 639 language code for the original language of
     *                         the film
     * @param runtime          The runtime of the film in minutes
     * @param homepage         The URL to the homepage of the film
     * @param adult            Whether the film is an adult film
     * @param video            Whether the film is a "direct-to-video" film
     * @param poster           The unique part of the URL of the poster (empty if
     *                         the URL is not known)
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean add(int id, String title, String originalTitle, String overview, String tagline, String status, Genre[] genres, LocalDate release, long budget, long revenue, String[] languages, String originalLanguage, double runtime, String homepage, boolean adult, boolean video, String poster) {
        if(movies.get(id) == null) {
            movieStructure structure = new movieStructure(id, title, originalTitle, overview, tagline, status, genres, release, budget, revenue, languages, originalLanguage, runtime, homepage, adult, video, poster);
            movies.put(id, structure);
            return true;
        } 
        return false;
    }
    

    /**
     * Removes a film from the data structure, and any data
     * added through this class related to the film
     * 
     * @param id The film ID
     * @return TRUE if the film has been removed successfully, FALSE otherwise
     */
    @Override
    public boolean remove(int id) {
        if (movies.containsKey(id)) {
            movies.remove(id);
            return true;
        }
        return false;
    }


    /**
     * Gets all the IDs for all films
     * 
     * @return An array of all film IDs stored
     */
    @Override
    public int[] getAllIDs() {
        NewArrayList<Integer> allMovieIDs = movies.getAllKeys();
        int[] ids = new int[allMovieIDs.size()];
        for (int i = 0; i < allMovieIDs.size(); i++) {
            ids[i] = allMovieIDs.get(i);
        }
        return ids;
    }

    
    /**
     * Finds the film IDs of all films released within a given range. If a film is
     * released either on the start or end dates, then that film should not be
     * included
     * 
     * @param start The start point of the range of dates
     * @param end   The end point of the range of dates
     * @return An array of film IDs that were released between start and end
     */
    public int[] getAllIDsReleasedInRange(LocalDate start, LocalDate end) {
        NewArrayList<Integer> allMovieIDs = movies.getAllKeys();
        NewArrayList<Integer> idsInRange = new NewArrayList<>();
        for (int i = 0; i < allMovieIDs.size(); i++) {
            movieStructure movie = movies.get(allMovieIDs.get(i));
            if (movie != null && movie.getRelease() != null && 
                movie.getRelease().isAfter(start) && movie.getRelease().isBefore(end)) {
                idsInRange.add(movie.getId());
            }
        }

        int[] result = new int[idsInRange.size()];
        for (int i = 0; i < idsInRange.size(); i++) {
            result[i] = idsInRange.get(i);
        }
        return result;
    }


    
    
    /**
     * Gets the title of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The title of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public String getTitle(int id) {
        movieStructure movie = movies.get(id); 
        if (movie != null) {
            return movie.getTitle();
        }
        return null;
    }
    

    /**
     * Gets the original title of a particular film, given the ID number of that
     * film
     * 
     * @param id The movie ID
     * @return The original title of the requested film. If the film cannot be
     *         found, then return null
     */
    @Override
    public String getOriginalTitle(int id) {
        movieStructure movie = movies.get(id);
        if (movie != null) {
            return movie.getOriginalTitle();
        }
        return null;
    }

    /**
     * Gets the overview of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The overview of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public String getOverview(int id) {
        movieStructure movie = movies.get(id);
        if (movie != null) {
            return movie.getOverview();
        }
        return null;
    }

    /**
     * Gets the tagline of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The tagline of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public String getTagline(int id) {
        movieStructure movie = movies.get(id);
        if (movie != null) {
            return movie.getTagline();
        }
        return null;
    }

    /**
     * Gets the status of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The status of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public String getStatus(int id) {
        movieStructure movie = movies.get(id);
        if (movie != null) {
            return movie.getStatus();
        }
        return null;
    }

    /**
     * Gets the genres of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The genres of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public Genre[] getGenres(int id) {
        movieStructure movie = movies.get(id);
        if (movie != null) {
            return movie.getGenres();
        }
        return null;
    }

    /**
     * Gets the release date of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The release date of the requested film. If the film cannot be found,
     *         then return null
     */
    @Override
    public LocalDate getRelease(int id) {
        movieStructure movie = movies.get(id);
        if (movie != null) {
            return movie.getRelease();
        }
        return null;
    }

    /**
     * Gets the budget of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The budget of the requested film. If the film cannot be found, then
     *         return -1
     */
    @Override
    public long getBudget(int id) {
        movieStructure movie = movies.get(id);
        if (movie != null) {
            return movie.getBudget();
        }
        return -1;
    }

    /**
     * Gets the revenue of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The revenue of the requested film. If the film cannot be found, then
     *         return -1
     */
    @Override
    public long getRevenue(int id) {
        movieStructure movie = movies.get(id);
        if (movie != null) {
            return movie.getRevenue();
        }
        return -1;
    }

    /**
     * Gets the languages of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The languages of the requested film. If the film cannot be found,
     *         then return null
     */
    @Override
    public String[] getLanguages(int id) {
        movieStructure movie = movies.get(id);
        if (movie != null) {
            return movie.getLanguages();
        }
        return null;
    }

    /**
     * Gets the original language of a particular film, given the ID number of that
     * film
     * 
     * @param id The movie ID
     * @return The original language of the requested film. If the film cannot be
     *         found, then return null
     */
    @Override
    public String getOriginalLanguage(int id) {
        movieStructure movie = movies.get(id);
        if (movie != null) {
            return movie.getOriginalLanguage();
        }
        return null;
    }

    /**
     * Gets the runtime of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The runtime of the requested film. If the film cannot be found, then
     *         return -1
     */
    @Override
    public double getRuntime(int id) {
        movieStructure movie = movies.get(id);
        if (movie != null) {
            return movie.getRuntime();
        }
        return -1;
    }

    /**
     * Gets the homepage of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The homepage of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public String getHomepage(int id) {
        movieStructure movie = movies.get(id);
        if (movie != null) {
            return movie.getHomepage();
        }
        return null;
    }

    /**
     * Gets weather a particular film is classed as "adult", given the ID number of
     * that film
     * 
     * @param id The movie ID
     * @return The "adult" status of the requested film. If the film cannot be
     *         found, then return false
     */
    @Override
    public boolean getAdult(int id) {
        movieStructure movie = movies.get(id);
        if (movie != null) {
            return movie.isAdult();
        }
        
        return false;
    }
    /**
     * Gets weather a particular film is classed as "direct-to-video", given the ID
     * number of that film
     * 
     * @param id The movie ID
     * @return The "direct-to-video" status of the requested film. If the film
     *         cannot be found, then return false
     */
    @Override
    public boolean getVideo(int id) {
        movieStructure movie = movies.get(id);
        if (movie != null) {
            return movie.isVideo();
        }
        
        return false;
    }

    /**
     * Gets the poster URL of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The poster URL of the requested film. If the film cannot be found,
     *         then return null
     */
    @Override
    public String getPoster(int id) {
        movieStructure movie = movies.get(id);
        if (movie != null) {
            return movie.getPoster();
        }
        
        return null;
    }

    /**
     * Sets the average IMDb score and the number of reviews used to generate this
     * score, for a particular film
     * 
     * @param id          The movie ID
     * @param voteAverage The average score on IMDb for the film
     * @param voteCount   The number of reviews on IMDb that were used to generate
     *                    the average score for the film
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean setVote(int id, double voteAverage, int voteCount) {
        movieStructure movie = movies.get(id); 
        if (movie != null) {
            movie.setVoteAverage(voteAverage); 
            movie.setVoteCount(voteCount); 
            return true;
        }
        return false;
    }
    

    /**
     * Gets the average score for IMDb reviews of a particular film, given the ID
     * number of that film
     * 
     * @param id The movie ID
     * @return The average score for IMDb reviews of the requested film. If the film
     *         cannot be found, then return -1
     */
    @Override
    public double getVoteAverage(int id) {
        movieStructure movie = movies.get(id); 
        if (movie != null) {
            return movie.getVoteAverage();
        }
        return -1; 
    }
    

    /**
     * Gets the amount of IMDb reviews used to generate the average score of a
     * particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The amount of IMDb reviews used to generate the average score of the
     *         requested film. If the film cannot be found, then return -1
     */
    @Override
    public int getVoteCount(int id) {
        movieStructure movie = movies.get(id); // Retrieve the movie by its ID
        if (movie != null) {
            return movie.getVoteCount(); 
        }
        return -1; 
    }

    /**
     * Adds a given film to a collection. The collection is required to have an ID
     * number, a name, and a URL to a poster for the collection
     * 
     * @param filmID                 The movie ID
     * @param collectionID           The collection ID
     * @param collectionName         The name of the collection
     * @param collectionPosterPath   The URL where the poster can
     *                               be found
     * @param collectionBackdropPath The URL where the backdrop can
     *                               be found
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean addToCollection(int filmID, int collectionID, String collectionName, String collectionPosterPath, String collectionBackdropPath) {
        // First, check if the movie exists
        movieStructure movie = movies.get(filmID);
        if (movie == null) {
            return false; 
        }
    
        // Retrieve or create the collection
        movieCollection collection = collections.get(collectionID);
        if (collection == null) {
            // If the collection does not exist, create it and add it to the collections map
            collection = new movieCollection(collectionID, collectionName, collectionPosterPath, collectionBackdropPath);
            collections.put(collectionID, collection);
        }
    
        // Now add the movie to the collection. This method should handle duplicates internally.
        collection.addFilm(filmID);
        movie.setCollectionID(collectionID); // Update the movie's collectionID
        movies.put(filmID, movie);
    
        return true;
    }
    
    

    /**
     * Get all films that belong to a given collection
     * 
     * @param collectionID The collection ID to be searched for
     * @return An array of film IDs that correspond to the given collection ID. If
     *         there are no films in the collection ID, or if the collection ID is
     *         not valid, return an empty array.
     */
    @Override
    public int[] getFilmsInCollection(int collectionID) {
        // Attempt to retrieve the collection from the collections map
        movieCollection collection = collections.get(collectionID);
        
        // If the collection is not found, return an empty array
        if (collection == null) {
            return new int[0];
        }
        
        // Retrieve the list of film IDs from the collection
        NewArrayList<Integer> filmIDsList = collection.getFilmIDs();
        
        // Convert the NewArrayList<Integer> to an int array
        int[] filmIDs = new int[filmIDsList.size()];
        for (int i = 0; i < filmIDsList.size(); i++) {
            filmIDs[i] = filmIDsList.get(i);
        }
        
        return filmIDs;
    }
    

    

    /**
     * Gets the name of a given collection
     * 
     * @param collectionID The collection ID
     * @return The name of the collection. If the collection cannot be found, then
     *         return null
     */
    @Override
    public String getCollectionName(int collectionID) {
        // Attempt to retrieve the collection from the collections map
        movieCollection collection = collections.get(collectionID);
        if (collection != null) {
            // Return the name of the collection
            return collection.getCollectionName();
        }
        return null;
    }
    

    /**
     * Gets the poster URL for a given collection
     * 
     * @param collectionID The collection ID
     * @return The poster URL of the collection. If the collection cannot be found,
     *         then return null
     */
    @Override
    public String getCollectionPoster(int collectionID) {
        // Assuming your collection class is movieCollection and the map is named collections
        movieCollection collection = collections.get(collectionID);
        if (collection != null) {
            return collection.getCollectionPosterPath();
        }
        return null;
    }
    

    /**
     * Gets the backdrop URL for a given collection
     * 
     * @param collectionID The collection ID
     * @return The backdrop URL of the collection. If the collection cannot be
     *         found, then return null
     */
    @Override
    public String getCollectionBackdrop(int collectionID) {
        movieCollection collection = collections.get(collectionID);
        if (collection != null) {
            return collection.getCollectionBackdropPath();
        }
        return null;
    }

    /**
     * Gets the collection ID of a given film
     * 
     * @param filmID The movie ID
     * @return The collection ID for the requested film. If the film cannot be
     *         found, then return -1
     */
    @Override
    public int getCollectionID(int filmID) {
        movieStructure movie = movies.get(filmID);
        if (movie != null) {
            Integer collectionId = movie.getCollectionID();
            // Check if the movie is associated with a collection
            if (collectionId != null) {
                return collectionId;
            }
        }
        return -1; 
    }
    

    /**
     * Sets the IMDb ID for a given film
     * 
     * @param filmID The movie ID
     * @param imdbID The IMDb ID
     * @return TRUE if the data able to be set, FALSE otherwise
     */
    @Override
    public boolean setIMDB(int filmID, String imdbID) {
        movieStructure movie = movies.get(filmID); 
        if (movie != null) {
            movie.setImdbID(imdbID); 
            return true; 
        }
        return false; 
    }
    /**
     * Gets the IMDb ID for a given film
     * 
     * @param filmID The movie ID
     * @return The IMDb ID for the requested film. If the film cannot be found,
     *         return null
     */
    @Override
    public String getIMDB(int filmID) {
        movieStructure movie = movies.get(filmID); 
        if (movie != null) {
            return movie.getImdbID(); 
        }
        return null; 
    }

    /**
     * Sets the popularity of a given film. If the popularity for a film already exists, replace it with the new value
     * 
     * @param id         The movie ID
     * @param popularity The popularity of the film
     * @return TRUE if the data able to be set, FALSE otherwise
     */
    @Override
    public boolean setPopularity(int id, double popularity) {
        movieStructure movie = movies.get(id); 
        if (movie != null) {
           movie.setPopularity(popularity); 
           return true;
        }
        return false; 
    }

    /**
     * Gets the popularity of a given film
     * 
     * @param id The movie ID
     * @return The popularity value of the requested film. If the film cannot be
     *         found, then return -1.0. If the popularity has not been set, return 0.0
     */
    @Override
    public double getPopularity(int id) {
        movieStructure movie = movies.get(id); 
        if (movie != null) {
            double popularity = movie.getPopularity(); 
            return popularity; 
        }
        return -1.0; 
    }

    /**
     * Adds a production company to a given film
     * 
     * @param id      The movie ID
     * @param company A Company object that represents the details on a production
     *                company
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean addProductionCompany(int id, Company company) {
        movieStructure movie = movies.get(id);
        if (movie != null) {
            movie.addProductionCompany(company); 
            return true;
        }
        return false;
    }
    
    /**
     * Adds a production country to a given film
     * 
     * @param id      The movie ID
     * @param country A ISO 3166 string containing the 2-character country code
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean addProductionCountry(int id, String country) {
        movieStructure movie = movies.get(id);
        if (movie != null) {
            movie.addProductionCountry(country); 
            return true;
        }
        return false;
    }

    /**
     * Gets all the production companies for a given film
     * 
     * @param id The movie ID
     * @return An array of Company objects that represent all the production
     *         companies that worked on the requested film. If the film cannot be
     *         found, then return null
     */
    @Override
    public Company[] getProductionCompanies(int id) {
        movieStructure movie = movies.get(id);
        if(movie != null){
            return movie.getProductionCompanies();
        }
        return null;
    }

    /**
     * Gets all the production companies for a given film
     * 
     * @param id The movie ID
     * @return An array of Strings that represent all the production countries (in
     *         ISO 3166 format) that worked on the requested film. If the film
     *         cannot be found, then return null
     */
    @Override
    public String[] getProductionCountries(int id) {
        movieStructure movie = movies.get(id);
        if(movie != null){
            return movie.getProductionCountries();
        }
        return null;
    }

    /**
     * States the number of movies stored in the data structure
     * 
     * @return The number of movies stored in the data structure
     */
    @Override
    public int size() {
        return movies.size();
    }

    /**
     * Produces a list of movie IDs that have the search term in their title,
     * original title or their overview
     * 
     * @param searchTerm The term that needs to be checked
     * @return An array of movie IDs that have the search term in their title,
     *         original title or their overview. If no movies have this search term,
     *         then an empty array should be returned
     */
    @Override
    public int[] findFilms(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return new int[0];
        }

        String lowerCaseSearchTerm = searchTerm.toLowerCase();
        NewArrayList<Integer> matchingIDs = new NewArrayList<>();

        // Use getAllKeys to iterate over all movie IDs in the movies map
        NewArrayList<Integer> allMovieIDs = movies.getAllKeys();
        for (Integer movieID : allMovieIDs) {
            movieStructure movie = movies.get(movieID);
            // Check if the search term is contained in the title, original title, or overview
            if (movie.getTitle().toLowerCase().contains(lowerCaseSearchTerm) ||
                movie.getOriginalTitle().toLowerCase().contains(lowerCaseSearchTerm) ||
                movie.getOverview().toLowerCase().contains(lowerCaseSearchTerm)) {
                // If a match is found, add the movie's ID to the list
                matchingIDs.add(movieID);
            }
        }

        // Convert the NewArrayList to an array
        int[] array = new int[matchingIDs.size()];
        for (int i = 0; i < matchingIDs.size(); i++) {
            array[i] = matchingIDs.get(i);
        }

        return array;
    }

    
}

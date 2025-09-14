package stores;

import java.time.LocalDateTime;

import interfaces.IRatings;
import structures.*;

public class Ratings implements IRatings {
    Stores stores;


    // Maps a composite key of user ID and movie ID to a RatingEntry object
    private MyHashMap<UserMovieKey, RatingEntry> directRatings;
    // Maps a user ID to a set of movie IDs they've rated
    private MyHashMap<Integer, MyHashSet<Integer>> moviesRatedByUser;
    // Maps a movie ID to a set of user IDs who have rated it
    private MyHashMap<Integer, MyHashSet<Integer>> usersWhoRatedMovie;
    // Map for rating average for quick access
    private MyHashMap<Integer, RatingSumAndCount> ratingsAverage;


    
    
    
    /**
     * The constructor for the Ratings data store. Initializes the data structures.
     * @param stores An object storing all the different key stores, including itself
     */
    public Ratings(Stores stores) {
        this.stores = stores;
        this.directRatings = new MyHashMap<>(); // Initialize direct access to ratings
        this.moviesRatedByUser = new MyHashMap<>(); // Initialize mapping of users to the movies they've rated
        this.usersWhoRatedMovie = new MyHashMap<>(); // Initialize mapping of movies to the users who have rated them
        this.ratingsAverage = new MyHashMap<>(); // FOR KEEPING TRACK OF MOVIE RATING AVERAGES
    }


    /**
     * Adds a rating to the data structure. The rating is made unique by its user ID
     * and its movie ID
     * 
     * @param userID    The user ID
     * @param movieID   The movie ID
     * @param rating    The rating gave to the film by this user (between 0 and 5
     *                  inclusive)
     * @param timestamp The time at which the rating was made
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    // Example of optimizing add method with computeIfAbsent
    @Override
    public boolean add(int userID, int movieID, float rating, LocalDateTime timestamp) {
        UserMovieKey key = new UserMovieKey(userID, movieID);
        if (directRatings.containsKey(key)) return false;
        
        directRatings.put(key, new RatingEntry(rating, timestamp));
        moviesRatedByUser.computeIfAbsent(userID, k -> new MyHashSet<>()).add(movieID);
        usersWhoRatedMovie.computeIfAbsent(movieID, k -> new MyHashSet<>()).add(userID);

        // Update the ratingsAverage map
        ratingsAverage.computeIfAbsent(movieID, k -> new RatingSumAndCount()).addRating(rating);

        return true;
    }

    

    /**
     * Removes a given rating, using the user ID and the movie ID as the unique
     * identifier
     * 
     * @param userID  The user ID
     * @param movieID The movie ID
     * @return TRUE if the data was removed successfully, FALSE otherwise
     */
    @Override
    public boolean remove(int userID, int movieID) {
        UserMovieKey key = new UserMovieKey(userID, movieID);
        if (!directRatings.containsKey(key)) return false;
        
        // Get the old rating to update the ratingsAverage map
        RatingEntry ratingEntry = directRatings.get(key);
        directRatings.remove(key);

        moviesRatedByUser.get(userID).remove(movieID);
        usersWhoRatedMovie.get(movieID).remove(userID);

        // Update the ratingsAverage map
        if (ratingsAverage.containsKey(movieID) && ratingEntry != null) {
            RatingSumAndCount sumAndCount = ratingsAverage.get(movieID);
            sumAndCount.removeRating(ratingEntry.getRating());
            if (sumAndCount.countRatings == 0) {
                ratingsAverage.remove(movieID); 
            }
        }

        return true;
    }

    


    /**
     * Sets a rating for a given user ID and movie ID. Therefore, should the given
     * user have already rated the given movie, the new data should overwrite the
     * existing rating. However, if the given user has not already rated the given
     * movie, then this rating should be added to the data structure
     * 
     * @param userID    The user ID
     * @param movieID   The movie ID
     * @param rating    The new rating to be given to the film by this user (between
     *                  0 and 5 inclusive)
     * @param timestamp The time at which the rating was made
     * @return TRUE if the data able to be added/updated, FALSE otherwise
     */
    @Override
    public boolean set(int userID, int movieID, float rating, LocalDateTime timestamp) {
        UserMovieKey key = new UserMovieKey(userID, movieID);
        RatingEntry oldEntry = directRatings.get(key);
        directRatings.put(key, new RatingEntry(rating, timestamp));
    
        moviesRatedByUser.computeIfAbsent(userID, k -> new MyHashSet<>()).add(movieID);
        usersWhoRatedMovie.computeIfAbsent(movieID, k -> new MyHashSet<>()).add(userID);
    
        // Update the ratingsAverage map
        RatingSumAndCount sumAndCount = ratingsAverage.computeIfAbsent(movieID, k -> new RatingSumAndCount());
        if (oldEntry != null) {
            sumAndCount.removeRating(oldEntry.getRating());
        }
        sumAndCount.addRating(rating);
    
        return true;
    }
    
    



    /**
     * Get all the ratings for a given film
     * 
     * @param movieID The movie ID
     * @return An array of ratings. If there are no ratings or the film cannot be
     *         found, then return an empty array
     */
    @Override
    public float[] getMovieRatings(int movieID) {
        // Early exit: Check if any users rated the movie to avoid unnecessary processing.
        MyHashSet<Integer> users = usersWhoRatedMovie.get(movieID);
        if (users == null) {
            return new float[0]; // Immediately return an empty array if no users rated the movie.
        }
    
        // Initialize the ratings list. 
        NewArrayList<Float> ratingsList = new NewArrayList<>();
    
        // Efficiently loop through each user who rated the movie.
        for (Integer userID : users) {
            UserMovieKey key = new UserMovieKey(userID, movieID);
            RatingEntry entry = directRatings.get(key);
            // Check if an entry exists for this user and movie combination.
            if (entry != null) {
                ratingsList.add(entry.getRating()); // Add the rating to the list.
            }
        }
    
        // Convert NewArrayList<Float> to float[] efficiently.
        float[] ratings = new float[ratingsList.size()];
        for (int i = 0; i < ratings.length; i++) {
            ratings[i] = ratingsList.get(i); // Retrieve each rating and store it in the array.
        }
        return ratings;
    }
    

    

    /**
     * Get all the ratings for a given user
     * 
     * @param userID The user ID
     * @return An array of ratings. If there are no ratings or the user cannot be
     *         found, then return an empty array
     */
    @Override
    public float[] getUserRatings(int userID) {
        MyHashSet<Integer> movies = moviesRatedByUser.get(userID);
        if (movies == null) {
            return new float[0];
        }
    
        NewArrayList<Float> ratingsList = new NewArrayList<>();
        for (Integer movieID : movies) {
            UserMovieKey key = new UserMovieKey(userID, movieID);
            RatingEntry entry = directRatings.get(key);
            if (entry != null) {
                ratingsList.add(entry.getRating());
            }
        }
    
        float[] ratings = new float[ratingsList.size()];
        for (int i = 0; i < ratings.length; i++) {
            ratings[i] = ratingsList.get(i);
        }
        return ratings;
    }
    


    /**
     * Get the average rating for a given film
     * 
     * @param movieID The movie ID
     * @return Produces the average rating for a given film. 
     *         If the film cannot be found in ratings, but does exist in the movies store, return 0.0f. 
     *         If the film cannot be found in ratings or movies stores, return -1.0f.
     */
    @Override
    public float getMovieAverageRating(int movieID) {
        RatingSumAndCount sumAndCount = ratingsAverage.get(movieID);
        return sumAndCount != null ? sumAndCount.getAverageRating() : -1.0f;
    }
    
    
    
    
    /**
     * Get the average rating for a given user
     * 
     * @param userID The user ID
     * @return Produces the average rating for a given user. If the user cannot be
     *         found, or there are no rating, return -1
     */
    public float getUserAverageRating(int userID) {
        float sum = 0;
        int count = 0;
        float[] allratings = getUserRatings(userID);
        for(int i = 0 ; i < allratings.length ; i++){
            sum += allratings[i];
            count++;
        }
        return count > 0 ? sum / count : -1.0f; // Return -1.0f if there are no ratings
    }
    
    

    /**
     * Gets the top N movies with the most ratings, in order from most to least
     * 
     * @param num The number of movies that should be returned
     * @return A sorted array of movie IDs with the most ratings. The array should be
     *         no larger than num. If there are less than num movies in the store,
     *         then the array should be the same length as the number of movies
     */
    @Override
    public int[] getMostRatedMovies(int num) {
        // PriorityQueue that orders movies based on the number of ratings (descending)
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>((a, b) -> 
            usersWhoRatedMovie.getOrDefault(b, new MyHashSet<>()).size() - 
            usersWhoRatedMovie.getOrDefault(a, new MyHashSet<>()).size());

        // Retrieve all movie IDs using the getAllKeys method
        NewArrayList<Integer> allMovieIDs = usersWhoRatedMovie.getAllKeys();
        for (int i = 0; i < allMovieIDs.size(); i++) {
            Integer movieID = allMovieIDs.get(i);
            pq.offer(movieID);
        }

        // Determine the size of the result array (cannot be larger than the number of movies or num)
        int resultSize = Math.min(num, pq.size());
        int[] topMovies = new int[resultSize];

        // Poll the queue to get the top N movies
        for (int i = 0; i < resultSize; i++) {
            topMovies[i] = pq.poll(); // Retrieve and remove the top element (movie ID)
        }

        return topMovies;
    }



    /**
     * Gets the top N users with the most ratings, in order from most to least
     * 
     * @param num The number of users that should be returned
     * @return A sorted array of user IDs with the most ratings. The array should be
     *         no larger than num. If there are less than num users in the store,
     *         then the array should be the same length as the number of users
     */
    @Override
    public int[] getMostRatedUsers(int num) {
        // Initialize your custom priority queue with a comparator for sorting users by their rating counts (descending)
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>((a, b) -> 
            moviesRatedByUser.getOrDefault(b, new MyHashSet<>()).size() - 
            moviesRatedByUser.getOrDefault(a, new MyHashSet<>()).size());
    
        // Retrieve all user IDs using the getAllKeys method from moviesRatedByUser
        NewArrayList<Integer> allUserIDs = moviesRatedByUser.getAllKeys();
        for (int i = 0; i < allUserIDs.size(); i++) {
            Integer userID = allUserIDs.get(i);
            pq.offer(userID); // Offer each user ID to the priority queue
        }
    
        // Determine the size of the result array
        int resultSize = Math.min(num, pq.size());
        int[] topUsers = new int[resultSize];
    
        // Poll the queue to get the top N users
        for (int i = 0; i < resultSize; i++) {
            topUsers[i] = pq.poll(); // Retrieve and remove the top element (user ID)
        }
    
        return topUsers;
    }
    


    /**
     * Gets the number of ratings in the data structure
     * 
     * @return The number of ratings in the data structure
     */
    @Override
    public int size() {
        return directRatings.size(); // Directly use the size method of MyHashMap
    }
    

    /**
     * Get the number of ratings that a movie has
     * 
     * @param movieid The movie id to be found
     * @return The number of ratings the specified movie has. 
     *         If the movie exists in the movies store, but there
     *         are no ratings for it, then return 0. If the movie
     *         does not exist in the ratings or movies store, then
     *         return -1
     */
    @Override
    public int getNumRatings(int movieid) {
        RatingSumAndCount ratingInfo = ratingsAverage.get(movieid);
        if (ratingInfo == null) {
            // If the movie exists in the movies store, but there are no ratings for it, return -1.
            return -1;
        }
        return ratingInfo.countRatings; // Directly return the count of ratings.
    }


    
    

    /**
     * Get the highest average rated film IDs, in order of there average rating
     * (hightst first).
     * 
     * @param numResults The maximum number of results to be returned
     * @return An array of the film IDs with the highest average ratings, highest
     *         first. If there are less than num movies in the store,
     *         then the array should be the same length as the number of movies
     */
    @Override
    public int[] getTopAverageRatedMovies(int numResults) {
        // Priority queue to sort movies by average rating in descending order.
        MyPriorityQueue<Object[]> pq = new MyPriorityQueue<>((a, b) -> Float.compare((Float)b[1], (Float)a[1]));

        // Fetch all movie IDs and calculate average ratings.
        NewArrayList<Integer> allMovieIDs = usersWhoRatedMovie.getAllKeys();
        for (Integer movieID : allMovieIDs) {
            float averageRating = getMovieAverageRating(movieID);
            // Add movies with positive average ratings to the priority queue.
            if (averageRating > 0) {
                pq.offer(new Object[]{movieID, averageRating});
            }
        }

        // Determine the size of the result array based on available movies and requested number of results.
        int resultSize = Math.min(numResults, pq.size());
        int[] topMovies = new int[resultSize];

        // Extract top movies based on average ratings.
        for (int i = 0; i < resultSize; i++) {
            topMovies[i] = (Integer)pq.poll()[0]; // Polling automatically removes and returns the highest rated movie.
        }

        return topMovies; // Return the sorted array of top-rated movie IDs.
    }


}

//FOR CALCULATING MOVIE AVERAGE RATING EFFICIENTLY
class RatingSumAndCount {
    float sumRatings = 0;
    int countRatings = 0;

    void addRating(float rating) {
        this.sumRatings += rating;
        this.countRatings++;
    }

    void removeRating(float rating) {
        this.sumRatings -= rating;
        this.countRatings--;
    }

    float getAverageRating() {
        return countRatings > 0 ? sumRatings / countRatings : 0;
    }
}

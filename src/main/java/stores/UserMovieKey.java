package stores;

import java.util.Objects;

class UserMovieKey implements Comparable<UserMovieKey> {
    private final int userID;
    private final int movieID;

    public UserMovieKey(int userID, int movieID) {
        this.userID = userID;
        this.movieID = movieID;
    }

    public int getMovieID() {
        return movieID;
    }
    public int getUserID() {
        return userID;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserMovieKey)) return false;
        UserMovieKey that = (UserMovieKey) o;
        return userID == that.userID && movieID == that.movieID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, movieID);
    }


    @Override
    public int compareTo(UserMovieKey other) {
        // First compare userID
        int userIDComparison = Integer.compare(this.userID, other.userID);
        
        // If userID is the same, compare movieID
        if (userIDComparison == 0) {
            return Integer.compare(this.movieID, other.movieID);
        }
        
        // Otherwise, return the result of comparing userID
        return userIDComparison;
    }
}

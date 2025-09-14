package stores;
import java.time.LocalDateTime;
import java.util.Objects;

class RatingEntry {
    private final float rating;
    private final LocalDateTime timestamp;

    public RatingEntry( float rating, LocalDateTime timestamp) {
        this.rating = rating;
        this.timestamp = timestamp;
    }

    // Getters  
    public float getRating() { return rating; }
}   
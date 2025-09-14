package stores;

import java.time.LocalDateTime;
import java.util.Objects;

class AggregateRatingInfo {
    private float sumOfRatings = 0;

    private int numberOfRatings = 0;

    // Update the aggregate info with a new rating
    public void addRating(float rating) {
        this.sumOfRatings += rating;
        this.numberOfRatings++;
    }

    // Remove a rating from the aggregate info
    public void removeRating(float rating) {
        this.sumOfRatings -= rating;
        this.numberOfRatings = Math.max(0, this.numberOfRatings - 1);
    }

    // Get the average rating
    public float getAverageRating() {
        return (this.numberOfRatings > 0) ? (this.sumOfRatings / this.numberOfRatings) : 0;
    }

    // Getters for sumOfRatings and numberOfRatings if necessary
    public float getSumOfRatings() {
        return sumOfRatings;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }
}

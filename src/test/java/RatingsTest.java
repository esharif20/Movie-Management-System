import stores.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RatingsTest {

    private Stores stores = new Stores();
    
    private int fakeUserID = 110;
    private int fakeMovieID = 210;

    @BeforeAll
    void setUp(){
        // ratings for uid 101
        stores.getRatings().add(101, 201, 0.1f, LocalDateTime.of(1989, 1, 1, 0, 0));

        stores.getRatings().add(101, 202, 1.1f, LocalDateTime.of(1991, 1, 1, 0, 0));

        stores.getRatings().add(101, 203, 2.1f, LocalDateTime.of(1993, 1, 1, 0, 0));

        stores.getRatings().add(101, 204, 3.1f, LocalDateTime.of(1995, 1, 1, 0, 0));

        stores.getRatings().add(101, 205, 4.1f, LocalDateTime.of(1997, 1, 1, 0, 0));

        // ratings for uid 102 
        
        stores.getRatings().add(102, 201, 2.2f, LocalDateTime.of(2001, 1, 1, 0, 0));

        stores.getRatings().add(102, 202, 3.2f, LocalDateTime.of(2005, 1, 1, 0, 0));

        stores.getRatings().add(102, 203, 4.2f, LocalDateTime.of(2009, 1, 1, 0, 0));

        // ratings for uid 103 
        stores.getRatings().add(103, 201, 1.3f, LocalDateTime.of(2003, 1, 1, 0, 0));

        stores.getRatings().add(103, 202, 2.3f, LocalDateTime.of(2007, 1, 1, 0, 0));

        stores.getRatings().add(103, 203, 3.3f, LocalDateTime.of(2013, 1, 1, 0, 0));

        stores.getRatings().add(103, 204, 4.3f, LocalDateTime.of(2013, 1, 1, 0, 0));

        // ratings for uid 104
        stores.getRatings().add(104, 201, 4.4f, LocalDateTime.of(2013, 1, 1, 0, 0));

        // ratings for uid 105
        stores.getRatings().add(105, 201, 3.5f, LocalDateTime.of(2013, 1, 1, 0, 0));

        stores.getRatings().add(105, 202, 4.5f, LocalDateTime.of(2013, 1, 1, 0, 0));

    }

    LocalDateTime calendarYear(int year){
        return LocalDateTime.of(year, 1, 1, 0, 0);
    }

    boolean checkContentsOfArray(float[] test, float[] result) {
        boolean finalFlag = true;
        if (test.length != result.length) {
            return false;
        }
        for (int i = 0; i < test.length; i++) {
            boolean innerFlag = false;
            for (int j = 0; j < result.length; j++) {
                if (test[i] == result[j]) {
                    innerFlag = true;
                    break;
                }
            }
            finalFlag &= innerFlag;
        }
        return finalFlag;
    }

    boolean checkContentsOfArray(int[] test, int[] result) {
        boolean finalFlag = true;
        if (test.length != result.length) {
            return false;
        }
        for (int i = 0; i < test.length; i++) {
            boolean innerFlag = false;
            for (int j = 0; j < result.length; j++) {
                if (test[i] == result[j]) {
                    innerFlag = true;
                    break;
                }
            }
            finalFlag &= innerFlag;
        }
        return finalFlag;
    }
    
    /**
     * Correct value for add when adding a unique combination of user ID and movie 
     * ID is TRUE
     */
    @Test void testAddPos() {
        System.out.println("\nStarting testAddPos...");
        assertTrue(stores.getRatings().add(106, 206, 2.5f, LocalDateTime.of(2020, 1, 1, 0, 0)), "Data should be able to be added as unique user and movie ID combination");
    }

    /**
     * Correct value for add when adding a non-unique combination of user ID and movie
     * ID is FALSE
     */
    @Test void testAddNeg() {
        System.out.println("\nStarting testAddNeg...");
        assertFalse(stores.getRatings().add(101, 201, 1.0f, LocalDateTime.of(2021, 1, 1, 0, 0)), "Data should not be able to be added as the user and movie combination is not unique");
    }

    /**
     * Correct value for removing a known rating is TRUE
     */
    @Test void testRemovePos() {
        System.out.println("\nStarting testRemovePos...");
        assertTrue(stores.getRatings().remove(101, 203), "Data should be able to be removed as the user and movie combination exists");
    }

    /**
     * Correct value for removing a unknown rating is FALSE
     */
    @Test
    void testRemoveNeg() {
        System.out.println("\nStarting testRemoveNeg...");
        assertFalse(stores.getRatings().remove(108, 208), "Data should not be able to be removed as unique user and movie ID combination is not in the data structure");
    }

    /**
     * Correct value for set when adding a combination of user ID and movie
     * ID that is not already known to the dataset is TRUE
     */
    @Test
    void testSetAdd() {
        System.out.println("\nStarting testSetAdd...");
        assertTrue(stores.getRatings().set(106, 206, 2.5f, LocalDateTime.of(2020, 1, 1, 0, 0)), "Data should be able to be added via set as unique user and movie ID combination");
    }

    /**
     * Correct value for set when adding a combination of user ID and
     * movie ID that is already known is TRUE
     */
    @Test
    void testSetPos() {
        System.out.println("\nStarting testSetPos...");
        assertTrue(stores.getRatings().set(101, 201, 1.0f, LocalDateTime.of(2021, 1, 1, 0, 0)), "Data should be able to be set as the user and movie combination is unique");
    }

    /**
     * Correct values for movie 201 are 1.0f, 2.2f, 1.3f, 4.4f, 3.5f.
     */
    @Test void testGetMovieRatingsPos(){

        System.out.println("\nStarting testGetMovieRatingsPos...");

        float[] tmpRatings = {1.0f, 2.2f, 1.3f, 3.5f, 4.4f};

        float[] results = stores.getRatings().getMovieRatings(201);
        for (int i = 0; i < results.length; i++) {
            System.out.println(results[i]);
        }

        assertTrue(checkContentsOfArray(tmpRatings, stores.getRatings().getMovieRatings(201)), "Not returning correct ratings for movie.");
    }

    /**
     * Fake ID should return empty array.
     */
    @Test void testGetMovieRatingsNeg(){

        System.out.println("\nStarting testGetMovieRatingsNeg...");

        assertArrayEquals(new float[0], stores.getRatings().getMovieRatings(fakeMovieID), "Non existent ID should return empty array.");
    }

    /**
     * Correct values for user 101 are 0.1f, 1.1f, 2.1f, 3.1f, 4.1f.
     */
    @Test void testGetUserRatingsPos(){

        System.out.println("\nStarting testGetUserRatingsPos...");

        float[] tmpRatings = {0.1f, 1.1f, 2.1f, 3.1f, 4.1f};

        assertTrue(checkContentsOfArray(tmpRatings, stores.getRatings().getUserRatings(101)), "Not returning correct ratings for user.");
    }

    /**
     * Fake ID should return empty array.
     */
    @Test void testGetUserRatingsNeg(){

        System.out.println("\nStarting testGetUserRatingsNeg...");

        assertArrayEquals(new float[0], stores.getRatings().getUserRatings(fakeUserID), "Non existent ID should return empty array.");
    }

    /**
     * Top 3 users are 101, 103, 102 in order with 5,4,3 respectively.
     */
    @Test void testGetMostRatedUsersPos (){
        System.out.println("\nStarting testGetMostRatedUsersPos...");
        int num = 3;
        int[] tmpMostRatedUsers = {101, 103, 102};

        assertArrayEquals(tmpMostRatedUsers, stores.getRatings().getMostRatedUsers(num), "Incorrect values returned.");

    }

    /**
     * If asked for none there should be an empty array.
     */
    @Test void testGetMostRatedUsersNeg (){
        
        System.out.println("\nStarting testGetMostRatedUsers ...");
        int num = 0;

        assertArrayEquals(new int[0], stores.getRatings().getMostRatedUsers(num), "Empty array should be returned when most rated parameter is 0.");

    }


    /**
     * Top 3 movies are 205, 204, 203 in order with 4.1f, 3.7f, 3.2f respectively.
     */
    @Test void testGetTopAverageRatedMoviesPos (){
        System.out.println("\nStarting testGetTopAverageMoviesPos...");
        int num = 3;
        int[] tmpMostRatedUsers = {205, 204, 203};

        assertArrayEquals(tmpMostRatedUsers, stores.getRatings().getTopAverageRatedMovies(num), "Incorrect values returned.");

    }

    /**
     * If asked for none there should be an empty array.
     */
    @Test void testGetTopAverageRatedMoviesNeg (){
        
        System.out.println("\nStartingGetTopAverageMoviesUsers ...");
        int num = 0;

        assertArrayEquals(new int[0], stores.getRatings().getTopAverageRatedMovies(num), "Empty array should be returned when top parameter is 0.");

    }

    /**
     * Average for fake ID should be 2.1f as (0.1f + 1.1f + 2.1f + 3.1f + 4.1f) / 5.0f = 2.1f.
     */
    @Test void testGetUserAverageRatingPos (){
        
        System.out.println("\nStarting testGetUserAverageRatings Pos ...");

        assertEquals(2.1f, stores.getRatings().getUserAverageRating(101), "Incorrect average given.");

    }

    /**
     * Average for fake ID should be -1.0f as there will be no results.
     */
    @Test void testGetUserAverageRatingNeg (){
        
        System.out.println("\nStarting testGetUserAverageRatingsNeg...");

        assertEquals(-1.0f, stores.getRatings().getUserAverageRating(fakeUserID), "Value should be -1.0f when no ratings present.");

    }

    /**
     * Average is 2.3f floats cause approximation, so rounding applied.
     */
    @Test void testGetMovieAverageRatingPos (){
        
        System.out.println("\nStarting testGetMovieAverageRatings Pos ...");

        float number = BigDecimal.valueOf(stores.getRatings().getMovieAverageRating(201))
            .setScale(1, RoundingMode.HALF_EVEN)
            .floatValue();

        assertEquals(2.3f, number, "Incorrect average given.");

    }

    /**
     * Average for fake ID should be 0.0f as there will be no results.
     */
    @Test void testGetMovieAverageRatingNeg() {
        System.out.println("\nStarting testGetMovieAverageRatingsNeg...");

        assertEquals(-1.0f, stores.getRatings().getMovieAverageRating(fakeMovieID), "Value should be 0.0 when no ratings present.");

    }

    /**
     * Each movie has been added in a different number of times 
     *      201 --> 5
     *      202 --> 4
     *      203 --> 3
     *      204 --> 2
     *      205 --> 1
     */
    @Test void testGetNumRatingsPos() {
        System.out.println("\nStarting testGetNumRatingsPos...");

        assertEquals(5, stores.getRatings().getNumRatings(201), "5 ratings were expected for movie 201 in unit test data");
        assertEquals(4, stores.getRatings().getNumRatings(202), "4 ratings were expected for movie 202 in unit test data");
        assertEquals(3, stores.getRatings().getNumRatings(203), "3 ratings were expected for movie 203 in unit test data");
        assertEquals(2, stores.getRatings().getNumRatings(204), "2 ratings were expected for movie 204 in unit test data");
        assertEquals(1, stores.getRatings().getNumRatings(205), "1 ratings were expected for movie 205 in unit test data");
    }

    /**
     * If the movie doesn't exist in the dataset, return -1
     */
    @Test void testGetNumRatingsNeg() {
        System.out.println("\nStarting testGetNumRatingsNeg...");

        assertEquals(-1, stores.getRatings().getNumRatings(206), "206 doesn't exist, so should return -1");
    }

    @Test void testSize(){
        assertEquals(15, stores.getRatings().size(), "Incorrect size.");
    }
    
    
}

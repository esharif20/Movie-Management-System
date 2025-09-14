import stores.*;
import java.util.Arrays;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class CreditsTest {

    private Stores removedCreditsStores = new Stores();
    private Stores emptyCreditsStores = new Stores();
    private Stores manyCreditsStores = new Stores();
    private Stores oneCreditsStores = new Stores();
    private Stores starCreditsStores = new Stores();

    // negative ID
    private int fakeMovieID = 210;

    // cast and crew for tests
    private CrewCredit johnLasseter = new CrewCredit("1","Directing", 1, "Director", "John Lasseter", "John Lasseter profilepath");
    private CrewCredit janeDoe = new CrewCredit("2","Lighting", 2, "Light Manager", "Jane Doe", "Jane Doe profilepath");
    
    private CastCredit woody = new CastCredit(1, "Woody(Voice)", "1", 1, "Tom Hanks", 0, "Woody profilepath");
    private CastCredit buzz = new CastCredit(2, "Buzz(Voice)", "2", 2, "Tim Allen", 1, "Buzz profilepath");
    private CastCredit isastar = new CastCredit(3, "isastar(Voice)", "3", 3, "isastar", 2, "isastar profilepath");
    private CastCredit notAStar = new CastCredit(4, "notAStar(Voice)", "4", 4, "notAStar", 3, "notAStar profilepath");

    @BeforeAll
    void setUp(){

        CrewCredit[] tmpCrewID1 = {johnLasseter};
        CastCredit[] tmpCastID1 = {woody};

        int tmpID = 201;

        manyCreditsStores.getCredits().add(tmpCastID1, tmpCrewID1, tmpID);

        oneCreditsStores.getCredits().add(tmpCastID1, tmpCrewID1, tmpID);

        removedCreditsStores.getCredits().add(tmpCastID1, tmpCrewID1, 201);
        removedCreditsStores.getCredits().add(tmpCastID1, tmpCrewID1, 202);
        removedCreditsStores.getCredits().add(tmpCastID1, tmpCrewID1, 203);

        CrewCredit[] tmpCrewID2 = {janeDoe};
        CastCredit[] tmpCastID2 = {buzz};
        manyCreditsStores.getCredits().add(tmpCastID2, tmpCrewID1, 202);
        manyCreditsStores.getCredits().add(tmpCastID1, tmpCrewID2, 203);

        CrewCredit[] tmpCrewID12 = {johnLasseter, janeDoe};
        CastCredit[] tmpCastID12 = {woody, buzz};
        manyCreditsStores.getCredits().add(tmpCastID12, tmpCrewID12, 204);

        CastCredit[] notAStarCast = {notAStar};

        CastCredit[] tmpStarCast = {woody, buzz, isastar};        
        starCreditsStores.getCredits().add(tmpStarCast, tmpCrewID12, 201);
        starCreditsStores.getCredits().add(tmpStarCast, tmpCrewID12, 202);
        starCreditsStores.getCredits().add(tmpStarCast, tmpCrewID12, 203);
        starCreditsStores.getCredits().add(notAStarCast, tmpCrewID1, 204);

    }



    LocalDateTime calendarYear(int year){
        return LocalDateTime.of(year, 1, 1, 0, 0);
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

    boolean checkContentsOfArray(String[] test, String[] result) {
        boolean finalFlag = true;
        if (test.length != result.length) {
            return false;
        }
        for (int i = 0; i < test.length; i++) {
            boolean innerFlag = false;
            for (int j = 0; j < result.length; j++) {
                if (test[i].equals(result[j])) {
                    innerFlag = true;
                    break;
                }
            }
            finalFlag &= innerFlag;
        }
        return finalFlag;
    }

    /**
     * Should be able to add a film with a unique ID
     */
    @Test void testAddPos() {
        System.out.println("\nStarting testAddPos...");

        CastCredit[] tmpCast = {woody};
        CrewCredit[] tmpCrew = {johnLasseter};

        assertTrue(oneCreditsStores.getCredits().add(tmpCast, tmpCrew, 299), "This film has not been added to the credits store, so should be able to be added");
    }

    /**
     * Should not be able to add a film with a non-unique ID
     */
    @Test
    void testAddNeg() {
        System.out.println("\nStarting testAddNeg...");

        CastCredit[] tmpCast = {woody};
        CrewCredit[] tmpCrew = {johnLasseter};

        assertFalse(oneCreditsStores.getCredits().add(tmpCast, tmpCrew, 201), "This film has been added to the credits store, so should not be able to be added");
    }

    /**
     * Checks that the cast for cast id 1 is the same using compareTo
     */
    @Test void testGetCastPos(){

        CastCredit[] tmpCast = {woody};

        System.out.println("\nStarting testGetCastPos...");

        Person result = oneCreditsStores.getCredits().getCast(1);

        assertEquals(tmpCast[0].getID(), result.getID(), "Incorrect ID returned.");
        assertEquals(tmpCast[0].getName(), result.getName(), "Incorrect name returned.");
        assertEquals(tmpCast[0].getProfilePath(), result.getProfilePath(), "Incorrect profile path returned.");

    }

    /**
     * Should return null for a not existent ID.
     */
    @Test void testGetCastNeg(){

        System.out.println("\nStarting testGetCastNeg...");

        assertNull(oneCreditsStores.getCredits().getCast(fakeMovieID), "Should return null for a not existent ID.");
    }

    /**
     * Checks that the crew for crew id 1 is the same using compareTo
     */
    @Test void testGetCrewPos(){
        
        CrewCredit[] tmpCrew = {johnLasseter};

        System.out.println("\nStarting testGetCastPos...");

        Person result = oneCreditsStores.getCredits().getCrew(1);

        assertEquals(tmpCrew[0].getID(), result.getID(), "Incorrect ID returned.");
        assertEquals(tmpCrew[0].getName(), result.getName(), "Incorrect name returned.");
        assertEquals(tmpCrew[0].getProfilePath(), result.getProfilePath(), "Incorrect profile path returned.");

    }

    /**
     * Should return null for a not existent ID.
     */
    @Test void testGetCrewNeg(){

        System.out.println("\nStarting testGetCrewNeg...");

        assertNull(oneCreditsStores.getCredits().getCrew(fakeMovieID), "Should return null for a not existent ID.");
    }

    /**
     * Removing a valid ID returns true
     */
    @Test void testRemovePos(){
        System.out.println("\nStarting testRemovePos...");
        assertTrue(removedCreditsStores.getCredits().remove(203), "Removed credit not returning true.");
    }



    /**
     * Removing 202 should leave 201, the only ID in oneCredits, this relies on getFilmIDs working.
     */
    @Test void testRemovePosValues(){
        System.out.println("\nStarting testRemovePosValues...");

        removedCreditsStores.getCredits().remove(202);

        assertArrayEquals(new CastCredit[0], removedCreditsStores.getCredits().getFilmCast(202), "The IDs according to getFilmCast is not as expected after removal, this could be an issue with remove() or an issue with getFilmCast().");
        assertArrayEquals(new CrewCredit[0], removedCreditsStores.getCredits().getFilmCrew(202), "The IDs according to getFilmCrew is not as expected after removal, this could be an issue with remove() or an issue with getFilmCrew().");
        assertEquals(-1, removedCreditsStores.getCredits().sizeOfCast(202), "The IDs according to sizeOfCast is not as expected after removal, this could be an issue with remove() or an issue with sizeOfCast().");
        assertEquals(-1, removedCreditsStores.getCredits().sizeofCrew(202), "The IDs according to sizeOfCrew is not as expected after removal, this could be an issue with remove() or an issue with sizeOfCrew().");
    }
    

    /**
     * Removing a not valid id should return a False
     */
    @Test void testRemoveNeg(){
        System.out.println("\nStarting testRemoveNeg...");

        assertFalse(removedCreditsStores.getCredits().remove(fakeMovieID), "Not valid credit removal should return false.");
    }

    /**
     * Get the cast members for film 201 in "order" order
     */
    @Test void testGetFilmCastPos() {
        System.out.println("\nStarting testGetFilmCastPos...");

        CastCredit[] expected = {woody, buzz};
        CastCredit[] result = manyCreditsStores.getCredits().getFilmCast(204);
    
        assertEquals(expected.length, result.length, "There should only be 2 cast members attached to this film");
        for (int i = 0; i < result.length; i++){
            assertEquals(expected[i].getID(), result[i].getID(), "ID in element " + i + " is not correct");
            assertEquals(expected[i].getName(), result[i].getName(), "Name in element " + i + " is not correct");
            assertEquals(expected[i].getCreditID(), result[i].getCreditID(), "CreditID in element " + i + " is not correct");
            assertEquals(expected[i].getCharacter(), result[i].getCharacter(), "Character in element " + i + " is not correct");
            assertEquals(expected[i].getElementID(), result[i].getElementID(), "ElementID in element " + i + " is not correct");
            assertEquals(expected[i].getOrder(), result[i].getOrder(), "Order in element " + i + " is not correct");
            assertEquals(expected[i].getProfilePath(), result[i].getProfilePath(), "Profile Path in element " + i + " is not correct");
        }
    }

    /**
     * Getting the cast of a film that doesn't exist shoudl return an empty array
     */
    @Test void testGetFilmCastNeg() {
        System.out.println("\nStarting testGetFilmCastNeg...");

        assertArrayEquals(new CastCredit[0], manyCreditsStores.getCredits().getFilmCast(299), "There should be no cast members for a film that does not exist in the store");
    }

    /**
     * Get the cast members for film 201 in "order" order
     */
    @Test
    void testGetFilmCrewPos() {
        System.out.println("\nStarting testGetFilmCrewPos...");

        CrewCredit[] expected = {johnLasseter, janeDoe};
        CrewCredit[] result = manyCreditsStores.getCredits().getFilmCrew(204);

        assertEquals(expected.length, result.length, "There should only be 2 cast members attached to this film");
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected[i].getID(), result[i].getID(), "ID in element " + i + " is not correct");
            assertEquals(expected[i].getName(), result[i].getName(), "Name in element " + i + " is not correct");
            assertEquals(expected[i].getDepartment(), result[i].getDepartment(), "Department in element " + i + " is not correct");
            assertEquals(expected[i].getJob(), result[i].getJob(), "Job in element " + i + " is not correct");
            assertEquals(expected[i].getElementID(), result[i].getElementID(), "ElementID in element " + i + " is not correct");
            assertEquals(expected[i].getProfilePath(), result[i].getProfilePath(), "Profile Path in element " + i + " is not correct");
        }
    }

    /**
     * Getting the cast of a film that doesn't exist shoudl return an empty array
     */
    @Test
    void testGetFilmCrewNeg() {
        System.out.println("\nStarting testGetFilmCrewNeg...");

        assertArrayEquals(new CastCredit[0], manyCreditsStores.getCredits().getFilmCrew(299),
                "There should be no cast members for a film that does not exist in the store");
    }

    /**
     * This method takes in a castID int and returns all films int[], to do this, make 3 films, 2 with the same cast
     * Calling cast id 1, should return films 201, 203, student output is sorted to compare arrays as not explicityly stated
     */
    @Test void testGetCastFilmsPos(){
        System.out.println("\nStarting testGetCastFilmsPos...");

        int[] tmpFilmIDs = {201, 203, 204};
        int[] testFilmIDs = manyCreditsStores.getCredits().getCastFilms(1);
        Arrays.sort(testFilmIDs);

        assertArrayEquals(tmpFilmIDs, testFilmIDs, "Incorrect values returned.");

    }
    /**
     * Not valid castID should returm empty array
     *  
     */
    @Test void testGetCastFilmsNeg(){
        System.out.println("\nStarting testGetCastFilmsNeg...");

        int[] tmpFilmIDs = {};
        int[] testFilmIDs = manyCreditsStores.getCredits().getCastFilms(1000);

        assertArrayEquals(tmpFilmIDs, testFilmIDs, "Not valid castID should returm empty array.");

    }

    /**
     * This method takes in a crewID int and returns all films int[], to do this, make 3 films, 2 with the same crew
     * Calling crew id 1, should return films 201, 202, student output is sorted to compare arrays as not explicityly stated
     */
    @Test void testGetCrewFilmsPos(){
        System.out.println("\nStarting testGetCrewFilmsPos...");

        int[] tmpFilmIDs = {201, 202, 204};
        int[] testFilmIDs = manyCreditsStores.getCredits().getCrewFilms(1);
        Arrays.sort(testFilmIDs);

        assertArrayEquals(tmpFilmIDs, testFilmIDs, "Incorrect values returned.");

    }
    /**
     * Not valid crewID should returm empty array
     *  
     */
    @Test void testGetCrewFilmsNeg(){
        System.out.println("\nStarting testGetCrewFilmsNeg...");

        int[] tmpFilmIDs = {};
        int[] testFilmIDs = manyCreditsStores.getCredits().getCrewFilms(1000);

        assertArrayEquals(tmpFilmIDs, testFilmIDs, "Not valid crewID should returm empty array.");
    }

    /**
     * Should return 1 when calling 201
     */
    @Test void testSizeOfCastPos(){
        System.out.println("\nStarting testSizeOfCastPos...");

        assertEquals(1, oneCreditsStores.getCredits().sizeOfCast(201), "Incorrect value returned.");
    }

    /**
     * Should return -1 when calling not validID
     */
    @Test void testSizeOfCastNeg(){
        System.out.println("\nStarting testSizeOfCastNeg...");

        assertEquals(-1, oneCreditsStores.getCredits().sizeOfCast(fakeMovieID), "Should return -1 for a not existent ID.");
    }

    /**
     * Should return 1 when calling 201
     */
    @Test void testSizeOfCrewPos(){
        System.out.println("\nStarting testSizeOfCrewPos...");

        assertEquals(1, oneCreditsStores.getCredits().sizeofCrew(201), "Incorrect value returned.");
    }

    /**
     * Should return -1 when calling not validID
     */
    @Test void testSizeOfCrewNeg(){
        System.out.println("\nStarting testSizeOfCrewNeg...");

        assertEquals(-1, oneCreditsStores.getCredits().sizeofCrew(fakeMovieID), "Should return -1 for a not existent ID.");
    }

    /**
     * Size of manyCredits should be 4
     */
    @Test void testSizePos() {
        System.out.println("\nStarting testSizePos...");

        assertEquals(4, manyCreditsStores.getCredits().size(), "Should return 4, as 4 films have been added to this dataset");
    }

    /**
     * Size of emptyCredits should be 0
     */
    @Test void testSizeNeg() {
        System.out.println("\nStarting testSizeNeg...");

        assertEquals(0, emptyCreditsStores.getCredits().size(), "There should be no credits when the store is empty");
    }

    /**
     * Should return Person objects for cast IDs {1, 2}
     */
    @Test void testGetUniqueCastPos(){
        System.out.println("\nStarting testGetUniqueCastPos...");

        int[] tmpCastIDs = {1, 2};
        String[] tmpCastNames = {"Tom Hanks", "Tim Allen"};
        String[] tmpCastProfilePaths = {"Woody profilepath", "Buzz profilepath" };

        Person[] resultCast = manyCreditsStores.getCredits().getUniqueCast();
        int[] resultCastIDs = new int[resultCast.length];
        String[] resultCastNames = new String[resultCast.length];
        String[] resultCastProfilePaths = new String[resultCast.length];

        for (int i = 0; i < resultCast.length; i++) {
            resultCastIDs[i] = resultCast[i].getID();
            resultCastNames[i] = resultCast[i].getName();
            resultCastProfilePaths[i] = resultCast[i].getProfilePath();
        }

        assertTrue(checkContentsOfArray(tmpCastIDs, resultCastIDs), "Incorrect IDs returned.");
        assertTrue(checkContentsOfArray(tmpCastNames, resultCastNames), "Incorrect names returned.");
        assertTrue(checkContentsOfArray(tmpCastProfilePaths, resultCastProfilePaths), "Incorrect profile paths returned.");
    }
    
    /**
     * Return empty array if there is nothing there.
     */
    @Test void testGetUniqueCastNeg(){
        System.out.println("\nStarting testGetUniqueCastNeg...");

        assertArrayEquals(new Person[0], emptyCreditsStores.getCredits().getUniqueCast());
    }

    /**
     * Should return Person objects for crew IDs {1, 2}
     */
    @Test void testGetUniqueCrewPos(){
        System.out.println("\nStarting testGetUniqueCrewPos...");

        int[] tmpCrewIDs = {1, 2};
        String[] tmpCrewNames = {"John Lasseter", "Jane Doe"};
        String[] tmpCrewProfilePaths = {"John Lasseter profilepath", "Jane Doe profilepath"};

        Person[] resultCrew = manyCreditsStores.getCredits().getUniqueCrew();
        int[] resultCrewIDs = new int[resultCrew.length];
        String[] resultCrewNames = new String[resultCrew.length];
        String[] resultCrewProfilePaths = new String[resultCrew.length];

        for (int i = 0; i < resultCrew.length; i++) {
            resultCrewIDs[i] = resultCrew[i].getID();
            resultCrewNames[i] = resultCrew[i].getName();
            resultCrewProfilePaths[i] = resultCrew[i].getProfilePath();
        }

        assertTrue(checkContentsOfArray(tmpCrewIDs, resultCrewIDs), "Incorrect IDs returned.");
        assertTrue(checkContentsOfArray(tmpCrewNames, resultCrewNames), "Incorrect names returned.");
        assertTrue(checkContentsOfArray(tmpCrewProfilePaths, resultCrewProfilePaths), "Incorrect profile paths returned.");
    }
    
    /**
     * Return empty array if there is nothing there.
     */
    @Test void testGetUniqueCrewNeg(){
        System.out.println("\nStarting testGetUniqueCrewNeg...");

        assertArrayEquals(new Person[0], emptyCreditsStores.getCredits().getUniqueCrew(), "Should return empty array if there is nothing there.");
    }

    /**
     * Find all casts that have buzz in them
     */
    @Test void testFindCastPos(){

        Person[] buzzPerson = {new Person(buzz.getID(), buzz.getName(), buzz.getProfilePath())};

        System.out.println("\nStarting testFindCastPos...");

        Person[] result = manyCreditsStores.getCredits().findCast("Tim Allen");
        
        assertEquals(buzzPerson.length, result.length, "Expected 1 element in the array");
        assertEquals(buzzPerson[0].getID(), result[0].getID(), "The ID is not correct.");
        assertEquals(buzzPerson[0].getName(), result[0].getName(), "The name is not correct.");
        assertEquals(buzzPerson[0].getProfilePath(), result[0].getProfilePath(), "The profile path is not correct.");
    }

    
    /**
     * Checks that the cast array returned has length equal to 0, so is empty.
     */
    @Test void testFindCastNeg(){

        System.out.println("\nStarting testFindCastNeg...");

        assertEquals(0, manyCreditsStores.getCredits().findCast("Unreasonably long check").length, "Finding Cast where there are not.");

    }

    /**
     * Find all crews that have janedoe in them
     */
    @Test void testFindCrewPos(){

        Person[] janeDoePerson = {new Person(janeDoe.getID(), janeDoe.getName(), janeDoe.getProfilePath())};

        System.out.println("\nStarting testFindCastPos...");

        Person[] result = manyCreditsStores.getCredits().findCrew("Jane Doe");

        assertEquals(janeDoePerson.length, result.length, "Expected 1 element in the array");
        assertEquals(janeDoePerson[0].getID(), result[0].getID(), "The ID is not correct.");
        assertEquals(janeDoePerson[0].getName(), result[0].getName(), "The name is not correct.");
        assertEquals(janeDoePerson[0].getProfilePath(), result[0].getProfilePath(), "The profile path is not correct.");
    }
    
    /**
     * Checks that the cast array returned has length equal to 0, so is empty.
     */
    @Test void testFindCrewNeg(){

        System.out.println("\nStarting testFindCrewNeg...");

        assertEquals(0, manyCreditsStores.getCredits().findCrew("Unreasonably long check").length, "Finding Crew where there are not.");

    }

    /**
     * Stars should be ids 201, 202, 203
     */
    @Test void testGetCastStarsInFilmsPos(){
        System.out.println("\nStarting testGetCastStarsInFilmsPos...");

        int[] tmpStarCast = {201, 202, 203};
        int[] starCast = starCreditsStores.getCredits().getCastStarsInFilms(1);
        Arrays.sort(starCast);


        assertArrayEquals(tmpStarCast, starCast, "Incorrect values returned.");
    }
    
    /**
     * Should return empty array since there are no credits.
     */
    @Test void testGetCastStarsInFilmsNeg(){
        System.out.println("\nStarting testGetCastStarsInFilmsNeg...");

        assertArrayEquals(new int[0], emptyCreditsStores.getCredits().getCastStarsInFilms(1), "Should return empty array if there are no credits.");
    }

    /**
     * Cast ID 1 should have appeared in the most films, followed by Cast ID 2
     */
    @Test void testGetMostCastCreditsPos() {
        System.out.println("\nStarting testGetMostCastCreditsPos...");

        Person[] expected = {
            new Person(woody.getID(), woody.getName(), woody.getProfilePath()),
            new Person(buzz.getID(), buzz.getName(), buzz.getProfilePath())
        };
        Person[] result = manyCreditsStores.getCredits().getMostCastCredits(2);

        assertEquals(expected.length, result.length, "The array returned should be 2");
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected[i].getID(), result[i].getID(), "ID in element " + i + " is not correct");
            assertEquals(expected[i].getName(), result[i].getName(), "Name in element " + i + " is not correct");
            assertEquals(expected[i].getProfilePath(), result[i].getProfilePath(),
                    "Profile Path in element " + i + " is not correct");
        }
    }

    /**
     * Shoudl return an empty array, as no elements
     */
    @Test void testGetMostCastCreditsNeg() {
        System.out.println("\nStarting testGetMostCastCreditsNeg...");

        assertArrayEquals(new Person[0], emptyCreditsStores.getCredits().getMostCastCredits(2), "An empty array should be returned, as there are no cast members");
    }

    /**
     * The cast member 1 should have 3 credits
     */
    @Test void testGetNumCastCreditsPos() {
        System.out.println("\nStarting testGetNumCastCreditsPos...");

        assertEquals(3, manyCreditsStores.getCredits().getNumCastCredits(1), "The value is incorrect");
    }

    /**
     * The case member 9 should not exist, so -1 credits
     */
    @Test void testGetNumCastCreditsNeg() {
        System.out.println("\nStarting testGetNumCastCreditsNeg...");

        assertEquals(-1, manyCreditsStores.getCredits().getNumCastCredits(9), "The value is incorrect");
    }
}

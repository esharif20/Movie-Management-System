package stores;


import java.util.Comparator;

import interfaces.ICredits;
import structures.*;

public class Credits implements ICredits{
    Stores stores;

    // Maps movie IDs to their corresponding credits information.
    private MyHashMap<Integer, MovieCredits> movies;

    // Maps cast member IDs to the sets of movie IDs they have appeared in.
    private MyHashMap<Integer, MyHashSet<Integer>> castInMovies;

    // Maps crew member IDs to the sets of movie IDs they have worked on.
    private MyHashMap<Integer, MyHashSet<Integer>> crewInMovies;

    

    


    /**
     * The constructor for the Credits data store. This is where you should
     * initialise your data structures.
     *
     * @param stores An object storing all the different key stores,
     *               including itself
     */
    public Credits (Stores stores) {
        this.stores = stores;

        this.stores = stores;
        this.movies = new MyHashMap<>();
        this.castInMovies = new MyHashMap<>();
        this.crewInMovies = new MyHashMap<>();
    }

    /**
     * Adds data about the people who worked on a given film. The movie ID should be
     * unique
     *
     * @param cast An array of all cast members that starred in the given film
     * @param crew An array of all crew members that worked on a given film
     * @param id   The (unique) movie ID
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean add(CastCredit[] cast, CrewCredit[] crew, int id) {
        if (movies.containsKey(id)) {
            return false; // Ensure movie ID is unique
        }

        MovieCredits newMovie = new MovieCredits();
        movies.put(id, newMovie); // Add the new movie with its credits

        // Add cast members to the movie and update global mapping
        for (CastCredit castMember : cast) {
            newMovie.addCastMember(castMember); // Add to MovieCredits
            castInMovies.computeIfAbsent(castMember.getID(), k -> new MyHashSet<>()).add(id);
        }

        // Add crew members to the movie and update global mapping
        for (CrewCredit crewMember : crew) {
            newMovie.addCrewMember(crewMember); // Add to MovieCredits
            crewInMovies.computeIfAbsent(crewMember.getID(), k -> new MyHashSet<>()).add(id);
        }


        return true;
    }

    /**
     * Remove a given films data from the data structure
     *
     * @param id The movie ID
     * @return TRUE if the data was removed, FALSE otherwise
     */
    @Override
    public boolean remove(int id) {
        if (!movies.containsKey(id)) {
            return false; // Movie doesn't exist
        }

        // Since we can't get the movie directly from remove, get it first
        MovieCredits movie = movies.get(id);
        if (movie == null) {
            return false; // Just in case, check if the movie is actually there
        }

        // Now remove the movie from the map
        if (!movies.remove(id)) {
            return false; // If removal wasn't successful for some reason
        }

        // Assuming castInMovies and crewInMovies are maps mapping IDs to sets of movie IDs
        // Update castInMovies and crewInMovies mappings
        if (movie.getCast() != null) {
            movie.getCast().forEach(castMember ->
                    castInMovies.get(castMember.getID()).remove(Integer.valueOf(id)));
        }
        if (movie.getCrew() != null) {
            movie.getCrew().forEach(crewMember ->
                    crewInMovies.get(crewMember.getID()).remove(Integer.valueOf(id)));
        }

        return true;
    }


    /**
     * Gets all the cast members for a given film
     *
     * @param filmID The movie ID
     * @return An array of CastCredit objects, one for each member of cast that is
     *         in the given film. The cast members should be in "order" order. If
     *         there is no cast members attached to a film, or the film canot be
     *         found, then return an empty array
     */
    @Override
    public CastCredit[] getFilmCast(int filmID) {
        if (!movies.containsKey(filmID)) {
            return new CastCredit[0]; // Movie not found
        }

        MovieCredits movie = movies.get(filmID);
        // Convert MyHashSet<CastCredit> to NewArrayList<CastCredit>
        NewArrayList<CastCredit> castList = new NewArrayList<>();
        for (CastCredit castCredit : movie.getCast()) {
            castList.add(castCredit);
        }

        // Now that castList is a NewArrayList, sort it via merge sort 
        castList.sort(Comparator.comparingInt(CastCredit::getOrder));

        // Convert the sorted NewArrayList to an array and return it
        return castList.toArray(new CastCredit[castList.size()]);
    }



    /**
     * Gets all the crew members for a given film
     *
     * @param filmID The movie ID
     * @return An array of CrewCredit objects, one for each member of crew that is
     *         in the given film. The crew members should be in ID order. If there
     *         is no crew members attached to a film, or the film canot be found,
     *         then return an empty array
     */
    @Override
    public CrewCredit[] getFilmCrew(int filmID) {
        if (!movies.containsKey(filmID)) {
            return new CrewCredit[0]; // Movie not found
        }

        MovieCredits movie = movies.get(filmID);
        // Convert MyHashSet<CrewCredit> to NewArrayList<CrewCredit>
        NewArrayList<CrewCredit> crewList = new NewArrayList<>();
        for (CrewCredit crewCredit : movie.getCrew()) {
            crewList.add(crewCredit);
        }

        // Sort crewList based on their 'ID' - merge sort 
        crewList.sort(Comparator.comparingInt(CrewCredit::getID));

        // Convert the sorted NewArrayList to an array and return it
        return crewList.toArray(new CrewCredit[crewList.size()]);
    }


    /**
     * Gets the number of cast that worked on a given film
     *
     * @param filmID The movie ID
     * @return The number of cast member that worked on a given film. If the film
     *         cannot be found, then return -1
     */
    @Override
    public int sizeOfCast(int filmID) {
        MovieCredits movie = movies.get(filmID);
        return movie == null ? -1 : movie.getCast().size();
    }

    /**
     * Gets the number of crew that worked on a given film
     *
     * @param filmID The movie ID
     * @return The number of crew member that worked on a given film. If the film
     *         cannot be found, then return -1
     */
    @Override
    public int sizeofCrew(int filmID) {
        MovieCredits movie = movies.get(filmID);
        return movie == null ? -1 : movie.getCrew().size();
    }

    /**
     * Gets the number of films stored in this data structure
     *
     * @return The number of films in the data structure
     */
    @Override
    public int size() {
        return movies.size();
    }

    /**
     * Gets a list of all unique cast members present in the data structure
     *
     * @return An array of all unique cast members as Person objects. If there are
     *         no cast members, then return an empty array
     */
    @Override
    public Person[] getUniqueCast() {
        MyHashMap<Integer, Person> uniquePersonsById = new MyHashMap<>();

        // Iterate through all movies to access their cast members
        for (MovieCredits movie : movies.values()) {
            for (CastCredit castCredit : movie.getCast()) {
                // Use the cast member's ID as the key to ensure uniqueness
                uniquePersonsById.putIfAbsent(castCredit.getID(),
                    new Person(castCredit.getID(), castCredit.getName(), castCredit.getProfilePath()));
            }
        }

        // Extract the unique Person instances into an array
        return uniquePersonsById.values().toArray(new Person[0]);
    }

    /**
     * Gets a list of all unique crew members present in the data structure
     *
     * @return An array of all unique crew members as Person objects. If there are
     *         no crew members, then return an empty array
     */
    @Override
    public Person[] getUniqueCrew() {
        MyHashMap<Integer, Person> uniqueCrewById = new MyHashMap<>();

        // Iterate through all movies to access their crew members
        for (MovieCredits movie : movies.values()) {
            for (CrewCredit crewCredit : movie.getCrew()) {
                // Use the crew member's ID as the key to ensure uniqueness
                uniqueCrewById.putIfAbsent(crewCredit.getID(),
                    new Person(crewCredit.getID(), crewCredit.getName(), crewCredit.getProfilePath()));
            }
        }

        // Extract the unique Person instances into an array
        return uniqueCrewById.values().toArray(new Person[0]);
    }

    /**
     * Get all the cast members that have the given string within their name
     *
     * @param cast The string that needs to be found
     * @return An array of unique Person objects of all cast members that have the
     *         requested string in their name
     */
    @Override
    public Person[] findCast(String cast) {
        MyHashMap<Integer, Person> uniquePersonsById = new MyHashMap<>();
        for (MovieCredits movie : movies.values()) {
            for (CastCredit castCredit : movie.getCast()) {
                if (castCredit.getName().contains(cast)) {
                    // Use the cast member's ID as the key to ensure uniqueness
                    uniquePersonsById.putIfAbsent(castCredit.getID(),
                        new Person(castCredit.getID(), castCredit.getName(), castCredit.getProfilePath()));
                }
            }
        }
        // Extract the unique Person instances into an array
        return uniquePersonsById.values().toArray(new Person[0]);
    }

    /**
     * Get all the crew members that have the given string within their name
     *
     * @param crew The string that needs to be found
     * @return An array of unique Person objects of all crew members that have the
     *         requested string in their name
     */
    @Override
    public Person[] findCrew(String crew) {
        MyHashMap<Integer, Person> uniqueCrewById = new MyHashMap<>();
        for (MovieCredits movie : movies.values()) {
            for (CrewCredit crewCredit : movie.getCrew()) {
                if (crewCredit.getName().contains(crew)) {
                    // Use the crew member's ID as the key to ensure uniqueness
                    uniqueCrewById.putIfAbsent(crewCredit.getID(),
                        new Person(crewCredit.getID(), crewCredit.getName(), crewCredit.getProfilePath()));
                }
            }
        }
        // Extract the unique Person instances into an array
        return uniqueCrewById.values().toArray(new Person[0]);
    }

    /**
     * Gets the Person object corresponding to the cast ID
     *
     * @param castID The cast ID of the person to be found
     * @return The Person object corresponding to the cast ID provided.
     *         If a person cannot be found, then return null
     */
    @Override
    public Person getCast(int castID) {
        MyHashSet<Integer> movieIDs = castInMovies.get(castID);
        if (movieIDs == null || movieIDs.isEmpty()) {
            return null; // The cast member hasn't been found in any movie.
        }

        // Iterate over a smaller set of movies where the castID appears rather than all movies
        for (Integer movieID : movieIDs) {
            MovieCredits movieCredits = movies.get(movieID);
            for (CastCredit castCredit : movieCredits.getCast()) {
                if (castCredit.getID() == castID) {
                    return new Person(castCredit.getID(), castCredit.getName(), castCredit.getProfilePath());
                }
            }
        }
        return null;
    }


    /**
     * Gets the Person object corresponding to the crew ID
     *
     * @param crewID The crew ID of the person to be found
     * @return The Person object corresponding to the crew ID provided.
     *         If a person cannot be found, then return null
     */
    @Override
    public Person getCrew(int crewID) {
        MyHashSet<Integer> movieIDs = crewInMovies.get(crewID);
        if (movieIDs == null || movieIDs.isEmpty()) {
            return null; // Early exit if the crew member hasn't been found in any movie.
        }

        // Iterate over a smaller set of movies where the crewID appears rather than all movies
        for (Integer movieID : movieIDs) {
            MovieCredits movieCredits = movies.get(movieID);
            for (CrewCredit crewCredit : movieCredits.getCrew()) {
                if (crewCredit.getID() == crewID) {
                    // If found, return a new Person object corresponding to the crew member
                    return new Person(crewCredit.getID(), crewCredit.getName(), crewCredit.getProfilePath());
                }
            }
        }
        return null; // Return null if no matching crew member is found
    }


    /**
     * Get an array of film IDs where the cast member has starred in
     *
     * @param castID The cast ID of the person
     * @return An array of all the films the member of cast has starred
     *         in. If there are no films attached to the cast member,
     *         then return an empty array
     */
    @Override
    public int[] getCastFilms(int castID) {
        // Retrieve the set of film IDs where the specified cast member has starred
        MyHashSet<Integer> filmIds = castInMovies.get(castID);
        if (filmIds == null) return new int[0]; // Return an empty array if no films are found

        // Convert the set of film IDs to an array
        int[] ids = new int[filmIds.size()];
        int i = 0;
        for (Integer id : filmIds) {
            ids[i++] = id;
        }
        return ids; // Return the array of film IDs
    }

    /**
     * Get an array of film IDs where the crew member has starred in
     *
     * @param crewID The crew ID of the person
     * @return An array of all the films the member of crew has starred
     *         in. If there are no films attached to the crew member,
     *         then return an empty array
     */
    @Override
    public int[] getCrewFilms(int crewID) {
        // Retrieve the set of film IDs where the specified crew member has contributed
        MyHashSet<Integer> filmIds = crewInMovies.get(crewID);
        if (filmIds == null) return new int[0]; // Return an empty array if no films are found

        // Convert the set of film IDs to an array
        int[] ids = new int[filmIds.size()];
        int i = 0;
        for (Integer id : filmIds) {
            ids[i++] = id;
        }
        return ids; // Return the array of film IDs
    }

    /**
     * Get the films that this cast member stars in (in the top 3 cast
     * members/top 3 billing). This is determined by the order field in
     * the CastCredit class
     *
     * @param castID The cast ID of the cast member to be searched for
     * @return An array of film IDs where the the cast member stars in.
     *         If there are no films where the cast member has starred in,
     *         or the cast member does not exist, return an empty array
     */
    @Override
    public int[] getCastStarsInFilms(int castID) {
        MyArrayList<Integer> starFilmsList = new MyArrayList<>();
        // Iterate through the movies to find films where the cast member is in the top 3 billing
        for (Integer movieID : movies.keySet()) {
            MovieCredits movieCredits = movies.get(movieID);
            for (CastCredit castCredit : movieCredits.getCast()) {
                if (castCredit.getID() == castID && castCredit.getOrder() <= 3) {
                    starFilmsList.add(movieID); // Add the movie ID to the list
                    break; // Stop searching in the current movie as the cast member has been found
                }
            }
        }

        // Convert the list of film IDs to an array and return it
        int[] starFilms = new int[starFilmsList.size()];
        for (int i = 0; i < starFilmsList.size(); i++) {
            starFilms[i] = starFilmsList.get(i);
        }
        return starFilms;
    }



    
    /**
     * Get Person objects for cast members who have appeared in the most
     * films. If the cast member has multiple roles within the film, then
     * they would get a credit per role played. For example, if a cast
     * member performed as 2 roles in the same film, then this would count
     * as 2 credits. The list should be ordered by the highest number of credits.
     *
     * @param numResults The maximum number of elements that should be returned
     * @return An array of Person objects corresponding to the cast members
     *         with the most credits, ordered by the highest number of credits.
     *         If there are less cast members that the number required, then the
     *         list should be the same number of cast members found.
     */
    @Override
    public Person[] getMostCastCredits(int numResults) {
        MyHashMap<Integer, Integer> creditCounts = new MyHashMap<>();
        MyHashMap<Integer, Person> idToPerson = new MyHashMap<>();

        // Step 1: Populate creditCounts and idToPerson maps
        for (MovieCredits movie : movies.values()) {
            for (CastCredit castCredit : movie.getCast()) {
                int currentCount = creditCounts.getOrDefault(castCredit.getID(), 0) + 1;
                creditCounts.put(castCredit.getID(), currentCount);
                idToPerson.putIfAbsent(castCredit.getID(), new Person(castCredit.getID(), castCredit.getName(), castCredit.getProfilePath()));
            }
        }
        
        // Step 2: Use a PriorityQueue to sort people by their credit counts in descending order
        MyPriorityQueue<Person> queue = new MyPriorityQueue<>(new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                Integer credits1 = creditCounts.getOrDefault(p1.getID(), 0);
                Integer credits2 = creditCounts.getOrDefault(p2.getID(), 0);
                return credits2.compareTo(credits1); // handles potential null values safely
            }
        });

        // Populate the PriorityQueue with Person objects
        for (Person p : idToPerson.values()) {
            queue.offer(p);
        }

        // Extract the top numResults cast members
        int resultSize = Math.min(numResults, queue.size());
        Person[] topCastMembers = new Person[resultSize];
        for (int i = 0; i < resultSize; i++) {
            topCastMembers[i] = queue.poll();
        }

        return topCastMembers;
    }
    


    /**
     * Get the number of credits for a given cast member. If the cast member has
     * multiple roles within the film, then they would get a credit per role
     * played. For example, if a cast member performed as 2 roles in the same film,
     * then this would count as 2 credits.
     *
     * @param castID A cast ID representing the cast member to be found
     * @return The number of credits the given cast member has. If the cast member
     *         cannot be found, return -1
     */
    @Override
    public int getNumCastCredits(int castID) {
        MyHashSet<Integer> movieIDs = castInMovies.get(castID);
        if (movieIDs == null) {
            // If the cast member is not found in any movie, return -1.
            return -1;
        }
        return movieIDs.size(); // Return the count of movies the cast member has appeared in.
    }

}


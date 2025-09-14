package stores;

import structures.*; // Ensure this includes MyHashSet

public class MovieCredits {
    private MyHashSet<CastCredit> cast;
    private MyHashSet<CrewCredit> crew;

    public MovieCredits() {
        this.cast = new MyHashSet<>();
        this.crew = new MyHashSet<>();
    }

    public void addCastMember(CastCredit castCredit) {
        cast.add(castCredit);
    }

    public void addCrewMember(CrewCredit crewCredit) {
        crew.add(crewCredit);
    }

    // Directly return the MyHashSet of cast members
    public MyHashSet<CastCredit> getCast() {
        return cast;
    }

    // Directly return the MyHashSet of crew members
    public MyHashSet<CrewCredit> getCrew() {
        return crew;
    }
}

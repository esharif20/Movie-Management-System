package stores;

import java.time.LocalDate;
import structures.*; 

public class movieStructure { 
    private int id;
    private String title;
    private String originalTitle;
    private String overview;
    private String tagline;
    private String status;
    private Genre[] genres; 
    private LocalDate release;
    private long budget;
    private long revenue;
    private String[] languages;
    private String originalLanguage;
    private double runtime;
    private String homepage;
    private boolean adult;
    private boolean video;
    private String poster;
    private double voteAverage;
    private int voteCount;
    private Integer collectionID;
    private String imdbID; 
    private double popularity;
    private NewArrayList<Company> productionCompanies = new NewArrayList<>(); 
    private NewArrayList<String> productionCountries = new NewArrayList<>();

    // Full constructor
    public movieStructure(int id, String title, String originalTitle, String overview, String tagline, String status, Genre[] genres, LocalDate release, long budget, long revenue, String[] languages, String originalLanguage, double runtime, String homepage, boolean adult, boolean video, String poster, double voteAverage, int voteCount, String imdbID) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.tagline = tagline;
        this.status = status;
        this.genres = genres;
        this.release = release;
        this.budget = budget;
        this.revenue = revenue;
        this.languages = languages;
        this.originalLanguage = originalLanguage;
        this.runtime = runtime;
        this.homepage = homepage;
        this.adult = adult;
        this.video = video;
        this.poster = poster;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.imdbID = imdbID; 
    }

    // Overloaded constructor without voteAverage, voteCount, and imdbID
    public movieStructure(int id, String title, String originalTitle, String overview, String tagline, String status, Genre[] genres, LocalDate release, long budget, long revenue, String[] languages, String originalLanguage, double runtime, String homepage, boolean adult, boolean video, String poster) {
        this(id, title, originalTitle, overview, tagline, status, genres, release, budget, revenue, languages, originalLanguage, runtime, homepage, adult, video, poster, 0.0, 0, "");
    }

    // Method to set collection ID when adding a film to a collection
    public void setCollectionID(Integer collectionID) {
        this.collectionID = collectionID;
    }

    // Method to get collection ID
    public Integer getCollectionID() {
        return this.collectionID;
    }


    public void addProductionCountry(String country) {
        if (!productionCountries.contains(country)) {
            productionCountries.add(country);
        }
    }
    
    public void addProductionCompany(Company company) {
        if (company != null && !productionCompanies.contains(company)) {
            productionCompanies.add(company);
        }
    }

    public Company[] getProductionCompanies() {
        return productionCompanies.toArray(new Company[0]);
    }

    public String[] getProductionCountries() {
        return productionCountries.toArray(new String[0]);
    }

    public String getImdbID() { 
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }


    // Getters
    public String getTitle() {
        return title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getTagline() {
        return tagline;
    }

    public String getStatus() {
        return status;
    }

    public Genre[] getGenres() {
        return genres;
    }

    public LocalDate getRelease() {
        return release;
    }

    public long getBudget() {
        return budget;
    }

    public long getRevenue() {
        return revenue;
    }

    public String[] getLanguages() {
        return languages;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public double getRuntime() {
        return runtime;
    }

    public String getHomepage() {
        return homepage;
    }

    public boolean isAdult() {
        return adult;
    }

    public boolean isVideo() {
        return video;
    }

    public String getPoster() {
        return poster;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setGenres(Genre[] genres) {
        this.genres = genres;
    }

    public void setRelease(LocalDate release) {
        this.release = release;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public void setLanguages(String[] languages) {
        this.languages = languages;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public void setRuntime(double runtime) {
        this.runtime = runtime;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}

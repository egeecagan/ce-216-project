import java.io.File;
import java.util.List;

public class Game {
    private String title;
    private String genre;
    private String developer;
    private String publisher;
    private List<String> platforms;
    private List<String> translators;
    private String steamId;
    private int releaseYear;
    private int playtime; // in hours
    private String format; // e.g., Digital, Physical
    private String language;
    private String rating;
    private List<String> tags;
    private String coverImagePath;

    // Constructor
    public Game(String title, String genre, String developer, String publisher, List<String> platforms,
                List<String> translators, String steamId, int releaseYear, int playtime, String format,
                String language, String rating, List<String> tags, String coverImagePath) {
        this.title = title;
        this.genre = genre;
        this.developer = developer;
        this.publisher = publisher;
        this.platforms = platforms;
        this.translators = translators;
        this.steamId = steamId;
        this.releaseYear = releaseYear;
        this.playtime = playtime;
        this.format = format;
        this.language = language;
        this.rating = rating;
        this.tags = tags;
        this.coverImagePath = coverImagePath;
    }

    // Getters and Setters for all attributes
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public List<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<String> platforms) {
        this.platforms = platforms;
    }

    public List<String> getTranslators() {
        return translators;
    }

    public void setTranslators(List<String> translators) {
        this.translators = translators;
    }

    public String getSteamId() {
        return steamId;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getPlaytime() {
        return playtime;
    }

    public void setPlaytime(int playtime) {
        this.playtime = playtime;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCoverImagePath() {
        return coverImagePath;
    }

    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    public String getFormattedCoverImagePath() {
        if (coverImagePath == null) {
            return null;
        }
        String cleanedName = this.getTitle().toLowerCase().replaceAll("\\s+", "");
        File imageFile = new File("images", cleanedName + ".jpg");
        String absPath = imageFile.toURI().getPath();

        return "file:" + absPath;
    }

    // Helper methods for UI
    public String getYearString() {
        return String.valueOf(releaseYear);
    }

    public String getImagePath() {
        return coverImagePath;
    }

    public String getPlatformsString() {
        return String.join(", ", platforms);
    }

    public String getTagsString() {
        return String.join(", ", tags);
    }

    // toString method for easy representation
    @Override
    public String toString() {
        return "Game{" +
                "title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", developer='" + developer + '\'' +
                ", publisher='" + publisher + '\'' +
                ", releaseYear=" + releaseYear +
                '}';
    }
}

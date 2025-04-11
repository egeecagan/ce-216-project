import java.util.List;

public class GameManager {
    private Catalog catalog;


    public GameManager(Catalog catalog) {
        this.catalog = catalog;
    }


    public void addGame(String title, String genre, String developer, String publisher, List<String> platforms,
                        List<String> translators, String steamId, int releaseYear, int playtime, String format,
                        String language, String rating, List<String> tags, String coverImagePath) {
        Game newGame = new Game(title, genre, developer, publisher, platforms, translators, steamId, releaseYear,
                playtime, format, language, rating, tags, coverImagePath);
        catalog.addGame(newGame);
    }

 
    public void editGame(Game game, String title, String genre, String developer, String publisher,
                         List<String> platforms, List<String> translators, String steamId, int releaseYear,
                         int playtime, String format, String language, String rating, List<String> tags,
                         String coverImagePath) {
        game.setTitle(title);
        game.setGenre(genre);
        game.setDeveloper(developer);
        game.setPublisher(publisher);
        game.setPlatforms(platforms);
        game.setTranslators(translators);
        game.setSteamId(steamId);
        game.setReleaseYear(releaseYear);
        game.setPlaytime(playtime);
        game.setFormat(format);
        game.setLanguage(language);
        game.setRating(rating);
        game.setTags(tags);
        game.setCoverImagePath(coverImagePath);
    }

 
    public void deleteGame(Game game) {
        catalog.removeGame(game);
    }


    public List<Game> searchGames(String keyword) {
        return catalog.searchGames(keyword);
    }

   
    public List<Game> filterGamesByTag(String tag) {
        return catalog.filterByTag(tag);
    }
}

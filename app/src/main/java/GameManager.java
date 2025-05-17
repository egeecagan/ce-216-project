import java.io.File;
import java.util.List;

public class GameManager {
    private Catalog catalog;
    private JSONHandler jsonHandler;

    public GameManager() {
        this.catalog = new Catalog();
        this.jsonHandler = new JSONHandler();
    }

    public void addGame(Game game) {
        catalog.addGame(game);
    }

    public void deleteGame(Game game) {
        catalog.removeGame(game);
    }

    public void updateGame(Game original, Game updated) {
        deleteGame(original);
        addGame(updated);
    }

    public List<Game> getAllGames() {
        return catalog.listGames();
    }

    public List<String> getGenres() {
        return catalog.getAllGenres();
    }

    public List<String> getTags() {
        return catalog.getAllTags();
    }

    public List<Game> searchGames(String keyword) {
        return catalog.searchGames(keyword);
    }

    public List<Game> filterGames(String genre, String year, List<String> tags) {
        return catalog.filterGames(genre, year, tags);
    }

    public boolean importFromJSON(File file) {
        // JSONHandler’dan dosyayı okuyup Game listesi alıyoruz
        List<Game> importedGames = jsonHandler.importFromJSON(file);
        if (importedGames == null) return false;

        for (Game game : importedGames) {
            boolean exists = catalog.listGames().stream()
                    .anyMatch(g -> g.getTitle().equalsIgnoreCase(game.getTitle()));
            if (!exists) {
                catalog.addGame(game);
            }
        }
        return true;
    }


    public boolean exportToJSON(File file) {
        return jsonHandler.exportToJSON(getAllGames(), file);
    }
}
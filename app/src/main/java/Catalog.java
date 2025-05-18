import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Catalog {
    private final List<Game> games;

    public Catalog() {
        this.games = new ArrayList<>();
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public void removeGame(Game game) {
        games.remove(game);
    }

    public List<Game> listGames() {
        return new ArrayList<>(games);
    }

    public List<String> getAllGenres() {
        return games.stream()
                .map(Game::getGenre)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getAllTags() {
        return games.stream()
                .flatMap(game -> game.getTags().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Game> searchGames(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return listGames();
        }

        String lowerKeyword = keyword.toLowerCase();
        return games.stream()
                .filter(game -> game.getTitle().toLowerCase().contains(lowerKeyword) ||
                        game.getGenre().toLowerCase().contains(lowerKeyword) ||
                        game.getDeveloper().toLowerCase().contains(lowerKeyword) ||
                        game.getPublisher().toLowerCase().contains(lowerKeyword) ||
                        game.getPlatforms().stream().anyMatch(p -> p.toLowerCase().contains(lowerKeyword)) ||
                        String.valueOf(game.getReleaseYear()).contains(lowerKeyword) ||
                        game.getTags().stream().anyMatch(t -> t.toLowerCase().contains(lowerKeyword)))
                .collect(Collectors.toList());
    }

    public List<Game> filterGames(List<String> genres,
            List<String> years,
            List<String> tags) {
        return games.stream()

                .filter(g -> genres == null || genres.isEmpty()
                        || genres.contains(g.getGenre()))

                .filter(g -> years == null || years.isEmpty()
                        || years.contains(String.valueOf(g.getReleaseYear())))

                .filter(g -> tags == null || tags.isEmpty()
                        || g.getTags().stream().anyMatch(tags::contains))
                .collect(Collectors.toList());
    }
}
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Catalog {
    private List<Game> games;

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
                .filter(game ->
                        game.getTitle().toLowerCase().contains(lowerKeyword) ||
                                game.getGenre().toLowerCase().contains(lowerKeyword) ||
                                game.getDeveloper().toLowerCase().contains(lowerKeyword) ||
                                game.getPublisher().toLowerCase().contains(lowerKeyword) ||
                                game.getPlatforms().stream().anyMatch(p -> p.toLowerCase().contains(lowerKeyword)) ||
                                String.valueOf(game.getReleaseYear()).contains(lowerKeyword) ||
                                game.getTags().stream().anyMatch(t -> t.toLowerCase().contains(lowerKeyword)))
                .collect(Collectors.toList());
    }

    public List<Game> filterGames(String genre, String year, List<String> tags) {
        return games.stream()
                .filter(game -> genre == null || genre.isEmpty() ||
                        game.getGenre().toLowerCase().contains(genre.toLowerCase()))
                .filter(game -> year == null || year.isEmpty() ||
                        String.valueOf(game.getReleaseYear()).equals(year))
                .filter(game -> tags == null || tags.isEmpty() ||
                        tags.stream().anyMatch(
                                tag -> game.getTags().stream().anyMatch(gameTag -> gameTag.equalsIgnoreCase(tag))))
                .collect(Collectors.toList());
}
}

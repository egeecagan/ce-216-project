import java.util.ArrayList;
import java.util.Arrays;
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
                .flatMap(game -> Arrays.stream(game.getGenre().split(",")))
                .map(String::trim)
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

    public List<Game> filterGames(List<String> genres, List<String> years, List<String> tags) {

        return games.stream()
                .filter(game -> {
                    if (genres == null || genres.isEmpty())
                        return true;
                    List<String> gameGenres = Arrays.asList(game.getGenre().split(",")).stream().map(String::trim)
                            .collect(Collectors.toList());
                    return genres.stream().allMatch(gameGenres::contains);
                })
                .filter(game -> {
                    if (years == null || years.isEmpty())
                        return true;
                    return years.contains(String.valueOf(game.getReleaseYear()));
                })
                .filter(game -> {
                    if (tags == null || tags.isEmpty())
                        return true;
                    return tags.stream().allMatch(game.getTags()::contains);
                })
                .collect(Collectors.toList());
    }
}
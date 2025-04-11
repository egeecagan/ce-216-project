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
        return games;
    }


    public List<Game> searchGames(String keyword) {
        return games.stream()
                .filter(game -> game.getTitle().contains(keyword) ||
                        game.getGenre().contains(keyword) ||
                        game.getDeveloper().contains(keyword) ||
                        game.getPublisher().contains(keyword))
                .collect(Collectors.toList());
    }

 
    public List<Game> filterByTag(String tag) {
        return games.stream()
                .filter(game -> game.getTags().contains(tag))
                .collect(Collectors.toList());
    }
}

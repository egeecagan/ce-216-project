import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class JSONHandler {
    private Gson gson;

    public JSONHandler() {
      
        gson = new GsonBuilder().setPrettyPrinting().create();
    }


    public void addGameToJSON(Game game, File file) {
        List<Game> games = importFromJSON(file);
        if (games == null) {
            games = new ArrayList<>();
        }
        games.add(game);
        exportToJson(games, file);
    }


    public void deleteGameFromJSON(Game game, File file) {
        List<Game> games = importFromJSON(file);
        if (games != null) {
            games.removeIf(g -> g.getTitle().equals(game.getTitle()));
            exportToJson(games, file);
        }
    }

  
    public void addGameByName(String title, Game game, File file) {
        List<Game> games = importFromJSON(file);
        if (games == null) {
            games = new ArrayList<>();
        }
        boolean exists = games.stream().anyMatch(g -> g.getTitle().equals(title));
        if (!exists) {
            games.add(game);
            exportToJson(games, file);
        }
    }

 
    public void deleteGameByName(String title, File file) {
        List<Game> games = importFromJSON(file);
        if (games != null) {
            games.removeIf(g -> g.getTitle().equals(title));
            exportToJson(games, file);
        }
    }

   
    public List<Game> importFromJSON(File file) {
        List<Game> games = new ArrayList<>();
        try (FileReader reader = new FileReader(file)) {
            Type gameListType = new TypeToken<List<Game>>() {}.getType();
            games = gson.fromJson(reader, gameListType);
            if (games == null) {
                games = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return games;
    }

 
    public void exportToJson(List<Game> games, File file) {
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(games, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

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
    private final Gson gson;

    

    public JSONHandler() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public List<Game> importFromJSON(File file) {
        try (FileReader reader = new FileReader(file)) {
            Type gameListType = new TypeToken<List<Game>>() {
            }.getType();
            List<Game> games = gson.fromJson(reader, gameListType);
            
            return games != null ? games : new ArrayList<>();

            

        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean exportToJSON(List<Game> games, File file) {
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(games, writer);
            return true;
        } catch (IOException e) {
            System.err.println("Error writing JSON file: " + e.getMessage());
            return false;
        }
    }
}
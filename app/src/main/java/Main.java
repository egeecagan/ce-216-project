import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
    
        JSONHandler jsonHandler = new JSONHandler();

     
        File file = new File("games_test.json");

   
        Game game1 = new Game(
                "Test Game",        
                "Adventure",           
                "TestDev",           
                "TestPublisher",        
                Arrays.asList("PC", "Xbox"), 
                Arrays.asList("Translator1"), 
                "12345",                 
                2025,                     
                50,                       
                "Digital",                
                "English",                
                "E",                      
                Arrays.asList("Action", "Indie"),
                "path/to/cover.jpg"      
        );

       
        jsonHandler.addGameToJSON(game1, file);
        System.out.println("Game added to JSON.");

       
        List<Game> importedGames = jsonHandler.importFromJSON(file);
        System.out.println("Imported Games:");
        for (Game g : importedGames) {
            System.out.println(g);
        }
    }
}

/*import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class GameCatalogUI extends Application {

    private GameManager gameManager;
    private Catalog catalog;
    private TableView<Game> gameTable;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        catalog = new Catalog();
        gameManager = new GameManager(catalog);

        // UI components
        gameTable = new TableView<>();
        Button addButton = new Button("Add Game");
        Button editButton = new Button("Edit Game");
        Button deleteButton = new Button("Delete Game");
        Button searchButton = new Button("Search");
        Button filterButton = new Button("Filter by Tag");
        Button importButton = new Button("Import JSON");
        Button exportButton = new Button("Export JSON");

        // Set up the TableView columns
        TableColumn<Game, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().getTitleProperty());
        gameTable.getColumns().add(titleColumn);

        // Button actions
        addButton.setOnAction(e -> openAddEditGameDialog(null));  // For adding a new game
        editButton.setOnAction(e -> openAddEditGameDialog(gameTable.getSelectionModel().getSelectedItem()));  // Edit selected game
        deleteButton.setOnAction(e -> deleteSelectedGame());
        searchButton.setOnAction(e -> searchGames());
        filterButton.setOnAction(e -> filterGames());
        importButton.setOnAction(e -> importGames());
        exportButton.setOnAction(e -> exportGames());

        // Layout
        VBox vbox = new VBox();
        vbox.getChildren().addAll(addButton, editButton, deleteButton, searchButton, filterButton, importButton, exportButton, gameTable);

        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setTitle("Game Catalog");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openAddEditGameDialog(Game game) {
        // Open a new dialog for adding/editing a game
        // For now, we will use a simple Alert box; in the future, you could use a new window or custom dialog.
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add/Edit Game");
        alert.setHeaderText("Enter game details");

        // Here you can add fields for title, genre, etc. For now, just a placeholder.
        alert.setContentText("Game details form goes here");

        alert.showAndWait();
    }

    private void deleteSelectedGame() {
        Game selectedGame = gameTable.getSelectionModel().getSelectedItem();
        if (selectedGame != null) {
            gameManager.deleteGame(selectedGame);
            refreshGameList();
        }
    }

    private void searchGames() {
        // Open a dialog to enter a search query and then search the games
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Games");
        dialog.setHeaderText("Enter a search keyword");
        dialog.showAndWait().ifPresent(keyword -> {
            List<Game> foundGames = gameManager.searchGames(keyword);
            gameTable.getItems().setAll(foundGames);
        });
    }

    private void filterGames() {
        // Open a dialog to select tags and filter games by tag
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Filter by Tag");
        dialog.setHeaderText("Enter tag to filter games");
        dialog.showAndWait().ifPresent(tag -> {
            List<Game> filteredGames = gameManager.filterGamesByTag(tag);
            gameTable.getItems().setAll(filteredGames);
        });
    }

    private void importGames() {
        // Use FileChooser to select and import a JSON file
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                List<Game> importedGames = new JSONHandler().importFromJSON(selectedFile);
                importedGames.forEach(game -> gameManager.addGame(game.getTitle(), game.getGenre(), game.getDeveloper(),
                        game.getPublisher(), game.getPlatforms(), game.getTranslators(), game.getSteamId(),
                        game.getReleaseYear(), game.getPlaytime(), game.getFormat(), game.getLanguage(),
                        game.getRating(), game.getTags(), game.getCoverImagePath()));
                refreshGameList();
            } catch (Exception e) {
                showError("Error importing JSON file", e.getMessage());
            }
        }
    }

    private void exportGames() {
        // Use FileChooser to select the location for saving the JSON file
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            try {
                new JSONHandler().exportToJSON(gameTable.getItems(), selectedFile);
            } catch (Exception e) {
                showError("Error exporting JSON file", e.getMessage());
            }
        }
    }

    private void refreshGameList() {
        gameTable.getItems().setAll(gameManager.searchGames(""));  // Refresh the game list (empty query shows all)
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
*/
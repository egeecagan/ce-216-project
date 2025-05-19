import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;



public class GameCatalogUI extends Application {
    private final GameManager gameManager = new GameManager();
    private final VBox gameCatalog = new VBox(20);
    private TextField searchField;

    @Override
    public void start(Stage primaryStage) {
        // Header with logo and search
        Text logo = new Text("Game Collection Catalog");
        logo.setFont(Font.font(24));
        logo.setFill(Color.WHITE);

        // Search field
        searchField = new TextField();
        searchField.setPromptText("Search games...");
        searchField.setPrefWidth(300);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> renderCatalog());

        // Home button: reset filters / search
        Button homeButton = new Button("Home");
        homeButton.setOnAction(e -> {
            searchField.clear();
            renderCatalog();
        });
        homeButton.setPrefWidth(60);

        // Filter & Help buttons
        Button filterButton = new Button("Filter");
        filterButton.setOnAction(e -> showFilterDialog());

        Button helpButton = new Button("Help");
        helpButton.setOnAction(e -> showHelpDialog());

        // Assemble search bar: Home | Search | Filter | Help
        HBox searchBox = new HBox(10,
                homeButton,
                searchField,
                filterButton,
                helpButton);
        searchBox.setAlignment(Pos.CENTER);

        VBox header = new VBox(10, logo, searchBox);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #2c3e50;");

        // Game catalog area
        gameCatalog.setPadding(new Insets(10));
        gameCatalog.setStyle("-fx-background-color: #34495e;");
        ScrollPane mainScroll = new ScrollPane(gameCatalog);
        mainScroll.setFitToWidth(true);
        mainScroll.setStyle("-fx-background: #34495e;");

        // Left panel with actions
        Button addGameButton = new Button("Add Game");
        addGameButton.setMaxWidth(Double.MAX_VALUE);
        addGameButton.setOnAction(e -> showAddEditGameDialog(null));

        Button importButton = new Button("Import JSON");
        importButton.setMaxWidth(Double.MAX_VALUE);
        importButton.setOnAction(e -> importGames(primaryStage));

        Button exportButton = new Button("Export JSON");
        exportButton.setMaxWidth(Double.MAX_VALUE);
        exportButton.setOnAction(e -> exportGames(primaryStage));

        VBox leftPanel = new VBox(10, addGameButton, importButton, exportButton);
        leftPanel.setPadding(new Insets(10));
        leftPanel.setStyle("-fx-background-color: #2c3e50;");
        leftPanel.setPrefWidth(150);

        // Main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(header);
        mainLayout.setCenter(mainScroll);
        mainLayout.setLeft(leftPanel);
        mainLayout.setStyle("-fx-background-color: #34495e;");

        // Seed and initial render
        seedDefaultGames();
        renderCatalog();

        // Show stage
        Scene scene = new Scene(mainLayout, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Video Game Catalog");
        primaryStage.show();
    }

    private String pathCreator(String gameName) {
        String cleanedName = gameName.toLowerCase().replaceAll("\\s+", "");
        //File imageFile = new File("images", cleanedName + ".jpg");
        //String ppp = imageFile.toURI().toString();
        String ppp = cleanedName + ".jpg";
        return ppp;
    }

    private void renderCatalog() {
        gameCatalog.getChildren().clear();

        String searchText = searchField.getText();
        List<Game> gamesToDisplay = gameManager.searchGames(searchText);

        // Group by genre
        gamesToDisplay.stream()
                .collect(Collectors.groupingBy(Game::getGenre))
                .forEach((genre, games) -> {
                    if (games.isEmpty())
                        return;

                    Text genreTitle = new Text(genre);
                    genreTitle.setFont(Font.font(18));
                    genreTitle.setFill(Color.WHITE);

                    HBox gameRow = new HBox(20);
                    gameRow.setAlignment(Pos.CENTER_LEFT);
                    gameRow.setPadding(new Insets(10));

                    for (Game game : games) {
                        gameRow.getChildren().add(createGameCard(game));
                    }

                    ScrollPane genreScroll = new ScrollPane(gameRow);
                    genreScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                    genreScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                    genreScroll.setFitToHeight(true);
                    genreScroll.setStyle("-fx-background: transparent;");

                    VBox genreSection = new VBox(10, genreTitle, genreScroll);
                    gameCatalog.getChildren().add(genreSection);
                });
    }



    private void seedDefaultGames() {
        // Vice City
        Game viceCity = new Game(
                "GTA Vice City",
                "Action-Adventure, Open World",
                "Rockstar North",
                "Rockstar Games",
                Arrays.asList("PC", "PS2", "Xbox"),
                Collections.emptyList(),
                "",
                2002,
                20,
                "Digital",
                "English",
                "M",
                Arrays.asList("Open World", "Classic"),
                pathCreator("GTA Vice City") ); // this is the image path
        gameManager.addGame(viceCity);

        // Path of Exile
        Game poe = new Game(
                "Path of Exile",
                "Action RPG, Free-to-Play",
                "Grinding Gear Games",
                "Grinding Gear Games",
                Arrays.asList("PC", "PS4", "Xbox One"),
                Collections.emptyList(),
                "",
                2013,
                100,
                "Digital",
                "English",
                "M",
                Arrays.asList("RPG", "Open World", "Free-to-Play"),
                pathCreator("Path of Exile"));
        gameManager.addGame(poe);
    }

    private VBox createGameCard(Game game) {
        ImageView imageView = new ImageView();
        try {
            Image image = new Image(game.getFormattedCoverImagePath());
            imageView.setImage(image);
        } catch (Exception e) {
            // Use placeholder if image fails to load
            imageView.setImage(new Image("file:placeholder.png"));
        }
        imageView.setFitWidth(150);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        Text titleText = new Text(game.getTitle());
        titleText.setFill(Color.WHITE);
        titleText.setFont(Font.font(14));

        Text yearText = new Text(game.getYearString());
        yearText.setFill(Color.LIGHTGRAY);
        yearText.setFont(Font.font(12));

        VBox card = new VBox(10, imageView, titleText, yearText);
        card.setStyle("-fx-background-color: #2c3e50; -fx-padding: 10; -fx-alignment: center;");
        card.setOnMouseClicked(e -> showGameDetails(game));

        return card;
    }

    private void showGameDetails(Game game) {
        Stage stage = new Stage();
        stage.setTitle(game.getTitle());

        ImageView imageView = new ImageView();
        try {
            imageView.setImage(new Image(game.getFormattedCoverImagePath()));
        } catch (Exception e) {
            imageView.setImage(new Image("file:placeholder.png"));
        }
        imageView.setFitWidth(250);
        imageView.setPreserveRatio(true);

        Label titleLabel = new Label("Title: " + game.getTitle());
        Label genreLabel = new Label("Genre: " + game.getGenre());
        Label developerLabel = new Label("Developer: " + game.getDeveloper());
        Label publisherLabel = new Label("Publisher: " + game.getPublisher());
        Label yearLabel = new Label("Year: " + game.getYearString());
        Label platformsLabel = new Label("Platforms: " + game.getPlatformsString());
        Label playtimeLabel = new Label("Playtime: " + game.getPlaytime() + " hours");
        Label tagsLabel = new Label("Tags: " + game.getTagsString());

        // Style labels
        Font labelFont = Font.font(14);
        titleLabel.setFont(Font.font(16));
        titleLabel.setTextFill(Color.WHITE);
        genreLabel.setFont(labelFont);
        genreLabel.setTextFill(Color.WHITE);
        developerLabel.setFont(labelFont);
        developerLabel.setTextFill(Color.WHITE);
        publisherLabel.setFont(labelFont);
        publisherLabel.setTextFill(Color.WHITE);
        yearLabel.setFont(labelFont);
        yearLabel.setTextFill(Color.WHITE);
        platformsLabel.setFont(labelFont);
        platformsLabel.setTextFill(Color.WHITE);
        playtimeLabel.setFont(labelFont);
        playtimeLabel.setTextFill(Color.WHITE);
        tagsLabel.setFont(labelFont);
        tagsLabel.setTextFill(Color.WHITE);

        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {
            stage.close();
            showAddEditGameDialog(game);
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #e74c3c;");
        deleteButton.setOnAction(e -> {
            gameManager.deleteGame(game);
            renderCatalog();
            stage.close();
        });

        HBox buttonBox = new HBox(10, editButton, deleteButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(15, imageView, titleLabel, genreLabel, developerLabel,
                publisherLabel, yearLabel, platformsLabel, playtimeLabel,
                tagsLabel, buttonBox);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #34495e;");

        Scene scene = new Scene(layout, 400, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void showAddEditGameDialog(Game gameToEdit) {
        Stage stage = new Stage();
        stage.setTitle(gameToEdit == null ? "Add New Game" : "Edit Game");

        // Create form fields
        TextField titleField = new TextField();
        TextField genreField = new TextField();
        TextField developerField = new TextField();
        TextField publisherField = new TextField();
        TextField yearField = new TextField();
        TextField playtimeField = new TextField();
        TextField platformsField = new TextField();
        TextField tagsField = new TextField();


        // Pre-fill fields if editing
        if (gameToEdit != null) {
            titleField.setText(gameToEdit.getTitle());
            genreField.setText(gameToEdit.getGenre());
            developerField.setText(gameToEdit.getDeveloper());
            publisherField.setText(gameToEdit.getPublisher());
            yearField.setText(String.valueOf(gameToEdit.getReleaseYear()));
            playtimeField.setText(String.valueOf(gameToEdit.getPlaytime()));
            platformsField.setText(String.join(", ", gameToEdit.getPlatforms()));
            tagsField.setText(String.join(", ", gameToEdit.getTags()));

        }

        // Create form
        GridPane form = new GridPane();
        form.setVgap(10);
        form.setHgap(10);
        form.setPadding(new Insets(20));

        form.add(new Label("Title:"), 0, 0);
        form.add(titleField, 1, 0);
        form.add(new Label("Genre:"), 0, 1);
        form.add(genreField, 1, 1);
        form.add(new Label("Developer:"), 0, 2);
        form.add(developerField, 1, 2);
        form.add(new Label("Publisher:"), 0, 3);
        form.add(publisherField, 1, 3);
        form.add(new Label("Release Year:"), 0, 4);
        form.add(yearField, 1, 4);
        form.add(new Label("Playtime (hours):"), 0, 5);
        form.add(playtimeField, 1, 5);
        form.add(new Label("Platforms (comma separated):"), 0, 6);
        form.add(platformsField, 1, 6);
        form.add(new Label("Tags (comma separated):"), 0, 7);
        form.add(tagsField, 1, 7);


        // Add buttons
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            try {
                List<String> platforms = List.of(platformsField.getText().split("\\s*,\\s*"));
                List<String> tags = List.of(tagsField.getText().split("\\s*,\\s*"));

                Game game = new Game(
                        titleField.getText(),
                        genreField.getText(),
                        developerField.getText(),
                        publisherField.getText(),
                        platforms,
                        new ArrayList<>(), // translators - not in form
                        "", // steamId - not in form
                        Integer.parseInt(yearField.getText()),
                        Integer.parseInt(playtimeField.getText()),
                        "Digital", // format - not in form
                        "English", // language - not in form
                        "E", // rating - not in form
                        tags,
                        titleField.getText().toLowerCase().replaceAll("\\s+", "") + ".jpg");

                if (gameToEdit != null) {
                    gameManager.updateGame(gameToEdit, game);
                } else {
                    gameManager.addGame(game);
                }

                renderCatalog();
                stage.close();
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter valid numbers for year and playtime.");
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> stage.close());

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, form, buttonBox);
        layout.setPadding(new Insets(10));
        layout.setStyle("-fx-background-color: #ecf0f1;");

        Scene scene = new Scene(layout, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    private void showFilterDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Filter Games");
        dialog.getDialogPane().getButtonTypes().addAll(
                new ButtonType("Apply", ButtonBar.ButtonData.OK_DONE),
                ButtonType.CANCEL);

        List<String> allGenres = gameManager.getGenres();
        Map<String, BooleanProperty> genreChecked = new HashMap<>();
        for (String genre : allGenres) {
            genreChecked.put(genre, new SimpleBooleanProperty(false));
        }

        // Genre ListView with checkboxes
        ListView<String> genreList = new ListView<>(FXCollections.observableArrayList(allGenres));
        genreList.setCellFactory(CheckBoxListCell.forListView(genreChecked::get));
        genreList.setPrefSize(300, 200);

        // Year TextField
        TextField yearField = new TextField();
        yearField.setPromptText("Enter Year (e.g. 2020)");

        List<String> allTags = gameManager.getTags();
        Map<String, BooleanProperty> tagsChecked = new HashMap<>();
        for (String tag : allTags) {
            tagsChecked.put(tag, new SimpleBooleanProperty(false));
        }

        // Tags ListView with checkboxes
        ListView<String> tagsList = new ListView<>(FXCollections.observableArrayList(allTags));
        tagsList.setCellFactory(CheckBoxListCell.forListView(tagsChecked::get));
        tagsList.setPrefSize(300, 200);

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setMinWidth(80);
        c1.setHalignment(HPos.RIGHT);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(c1, c2);

        grid.add(new Label("Genres:"), 0, 0);
        grid.add(genreList, 1, 0);
        grid.add(new Label("Year:"), 0, 1);
        grid.add(yearField, 1, 1);
        grid.add(new Label("Tags:"), 0, 2);
        grid.add(tagsList, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().setPrefSize(400, 500);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {

            List<String> selGenres = genreChecked.entrySet().stream()
                    .filter(entry -> entry.getValue().get())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            String yearText = yearField.getText().trim();
            List<String> selYears = yearText.isEmpty()
                    ? Collections.emptyList()
                    : Collections.singletonList(yearText);

            List<String> selTags = tagsChecked.entrySet().stream()
                    .filter(entry -> entry.getValue().get())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            List<Game> filtered = gameManager.filterGames(selGenres, selYears, selTags);
            gameCatalog.getChildren().clear();
            if (filtered.isEmpty()) {
                gameCatalog.getChildren().add(new Text("No games found matching the selected filters!"));
            } else {
                filtered.forEach(g -> gameCatalog.getChildren().add(createGameCard(g)));
            }
        }
    }

    private void importGames(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Games from JSON");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            boolean success = gameManager.importFromJSON(file);
            if (success) {
                showAlert("Import Successful", "Games imported successfully!");
                renderCatalog();
            } else {
                showAlert("Import Failed", "Failed to import games from the selected file.");
            }
        }
    }

    private void exportGames(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Games to JSON");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            boolean success = gameManager.exportToJSON(file);
            if (success) {
                showAlert("Export Successful", "Games exported successfully!");
            } else {
                showAlert("Export Failed", "Failed to export games to the selected file.");
            }
        }
    }

    private void showHelpDialog() {
        Alert helpDialog = new Alert(Alert.AlertType.INFORMATION);
        helpDialog.setTitle("User Manual - Game Catalog");
        helpDialog.setHeaderText("Welcome to the Game Collection Catalog Help Section");

        String content = """
        This application helps you manage your video game collection efficiently.

        🔹 Adding a Game:
           - Click the 'Add' button.
           - Fill in the fields such as title, genre, developer, etc.
           - Click 'Save' to add the game to your collection.

        🔹 Editing a Game:
           - Select a game from the list.
           - Click the 'Edit' button.
           - Modify the fields and click 'Save'.

        🔹 Deleting a Game:
           - Select a game from the list.
           - Click the 'Delete' button.

        🔹 Searching:
           - Use the search bar to find games by title, developer, publisher, or tags.

        🔹 Filtering:
           - Use genre dropdown, year input or tags list to filter games.
           - Multiple filters can be applied together.

        🔹 Importing/Exporting:
           - Click 'Import' to load games from a JSON file.
           - Click 'Export' to save the current list to a JSON file.

        🔹 Cover Image:
           - Add a cover image when adding or editing a game.
           - Image must be a valid file path (e.g., D:\\covers\\game.jpg)

        💡 Tip:
           - Hover over fields for tooltips.
           - All changes are saved in JSON format locally.

        For further help, contact: support@gamecatalogapp.com
        """;

        TextArea textArea = new TextArea(content);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setPrefSize(600, 400);

        DialogPane dialogPane = helpDialog.getDialogPane();
        dialogPane.setContent(textArea);
        helpDialog.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
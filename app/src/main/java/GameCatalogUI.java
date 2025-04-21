import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.text.html.ListView;

public class GameCatalogUI extends Application {
    private GameManager gameManager = new GameManager();
    private VBox gameCatalog = new VBox(20);
    private TextField searchField;
    private ComboBox<String> genreFilter;
    private TextField yearFilter;
    private ListView<String> tagsFilter;

    @Override
    public void start(Stage primaryStage) {
        // Header with logo and search
        Text logo = new Text("Game Collection Catalog");
        logo.setFont(Font.font(24));
        logo.setFill(Color.WHITE);

        searchField = new TextField();
        searchField.setPromptText("Search games...");
        searchField.setPrefWidth(300);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> renderCatalog());

        Button filterButton = new Button("Filter");
        filterButton.setOnAction(e -> showFilterDialog());

        Button helpButton = new Button("Help");
        helpButton.setOnAction(e -> showHelpDialog());

        HBox searchBox = new HBox(10, searchField, filterButton, helpButton);
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

        // Initial render
        renderCatalog();

        // Set up the scene and stage
        Scene scene = new Scene(mainLayout, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Video Game Catalog");
        primaryStage.show();
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
        TextField imageField = new TextField();

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
            imageField.setText(gameToEdit.getImagePath());
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
        form.add(new Label("Cover Image Path:"), 0, 8);
        form.add(imageField, 1, 8);

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
                        imageField.getText());

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
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Filter Games");
        dialog.setHeaderText("Select filters to apply");

        // Set the button types
        ButtonType applyButtonType = new ButtonType("Apply", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(applyButtonType, ButtonType.CANCEL);

        // Create filter controls
        genreFilter = new ComboBox<>();
        genreFilter.getItems().addAll(gameManager.getGenres());
        genreFilter.setPromptText("Select Genre");

        yearFilter = new TextField();
        yearFilter.setPromptText("Enter Year");

        tagsFilter = new ListView<>();
        tagsFilter.getItems().addAll(gameManager.getTags());
        tagsFilter.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tagsFilter.setPrefHeight(150);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Genre:"), 0, 0);
        grid.add(genreFilter, 1, 0);
        grid.add(new Label("Year:"), 0, 1);
        grid.add(yearFilter, 1, 1);
        grid.add(new Label("Tags:"), 0, 2);
        grid.add(tagsFilter, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a list of selected tags when the apply button is
        // clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == applyButtonType) {
                return new ArrayList<>(tagsFilter.getSelectionModel().getSelectedItems());
            }
            return null;
        });

        Optional<List<String>> result = dialog.showAndWait();

        result.ifPresent(selectedTags -> {
            String genre = genreFilter.getValue();
            String year = yearFilter.getText().trim();
            if (year.isEmpty()) {
                year = null;
            }
            List<Game> filteredGames = gameManager.filterGames(genre, year, selectedTags);

            gameCatalog.getChildren().clear();
            if (!filteredGames.isEmpty()) {
                Text filterTitle = new Text("Filtered Results");
                filterTitle.setFont(Font.font(18));
                filterTitle.setFill(Color.WHITE);

                HBox gameRow = new HBox(20);
                gameRow.setAlignment(Pos.CENTER_LEFT);
                gameRow.setPadding(new Insets(10));

                for (Game game : filteredGames) {
                    gameRow.getChildren().add(createGameCard(game));
                }

                ScrollPane scrollPane = new ScrollPane(gameRow);
                scrollPane.setFitToHeight(true);
                scrollPane.setStyle("-fx-background: transparent;");

                VBox filterSection = new VBox(10, filterTitle, scrollPane);
                gameCatalog.getChildren().add(filterSection);
            } else {
                Text noResults = new Text("No games match the selected filters");
                noResults.setFont(Font.font(16));
                noResults.setFill(Color.WHITE);
                gameCatalog.getChildren().add(noResults);
            }
        });
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Catalog Help");
        alert.setHeaderText("How to use the Game Catalog");

        String helpText = "1. Adding Games:\n" +
                "   - Click 'Add Game' and fill in all required fields\n" +
                "   - Required fields: Title, Genre, Developer, Publisher, Year\n\n" +
                "2. Editing Games:\n" +
                "   - Click on a game to view details\n" +
                "   - Click 'Edit' to modify game information\n\n" +
                "3. Deleting Games:\n" +
                "   - Click on a game to view details\n" +
                "   - Click 'Delete' to remove the game\n\n" +
                "4. Searching:\n" +
                "   - Type in the search box to find games by title, genre, etc.\n\n" +
                "5. Filtering:\n" +
                "   - Click 'Filter' to filter by genre, year, or tags\n\n" +
                "6. Import/Export:\n" +
                "   - Use these buttons to load or save your game collection as JSON\n\n" +
                "Note: For cover images, provide the full path to the image file.";

        alert.setContentText(helpText);
        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(500, 400);
        alert.showAndWait();
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
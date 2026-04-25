package com.flowboard;

import com.flowboard.controller.MainController;
import com.flowboard.util.JsonExporter;
import com.flowboard.view.SettingsPanel;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        MainController controller = new MainController();
        ScrollPane board = controller.buildUI();

        SettingsPanel settingsPanel = new SettingsPanel();

        Label titleLabel = new Label("FlowBoard");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #212121; -fx-cursor: hand;");

        TextField titleEdit = new TextField("FlowBoard");
        titleEdit.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #212121;" +
                        "-fx-background-color: transparent;" +
                        "-fx-border-color: #1976d2;" +
                        "-fx-border-radius: 4;" +
                        "-fx-border-width: 0 0 2 0;" +
                        "-fx-padding: 0 4 0 4;"
        );
        titleEdit.setPrefWidth(160);
        titleEdit.setVisible(false);
        titleEdit.setManaged(false);

        titleLabel.setOnMouseClicked(e -> {
            titleEdit.setText(titleLabel.getText());
            titleLabel.setVisible(false);
            titleLabel.setManaged(false);
            titleEdit.setVisible(true);
            titleEdit.setManaged(true);
            titleEdit.requestFocus();
            titleEdit.selectAll();
        });

        titleEdit.setOnAction(e -> {
            if (!titleEdit.getText().isBlank()) titleLabel.setText(titleEdit.getText());
            titleEdit.setVisible(false);
            titleEdit.setManaged(false);
            titleLabel.setVisible(true);
            titleLabel.setManaged(true);
            stage.setTitle(titleLabel.getText());
        });

        titleEdit.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused) {
                if (!titleEdit.getText().isBlank()) titleLabel.setText(titleEdit.getText());
                titleEdit.setVisible(false);
                titleEdit.setManaged(false);
                titleLabel.setVisible(true);
                titleLabel.setManaged(true);
                stage.setTitle(titleLabel.getText());
            }
        });

        TextField searchField = new TextField();
        searchField.setPromptText("Buscar tarjetas...");
        searchField.setPrefWidth(200);
        searchField.setStyle(
                "-fx-background-radius: 6;" +
                        "-fx-border-radius: 6;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-width: 1;" +
                        "-fx-padding: 5 10 5 10;" +
                        "-fx-font-size: 12px;"
        );
        searchField.textProperty().addListener((obs, oldVal, newVal) ->
                controller.filterCards(newVal.trim().toLowerCase())
        );

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button exportBtn = new Button("Exportar JSON");
        exportBtn.setStyle(
                "-fx-background-color: #1976d2;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 6 14 6 14;"
        );
        exportBtn.setOnMouseEntered(e -> exportBtn.setStyle(
                "-fx-background-color: #1565c0;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 6 14 6 14;"
        ));
        exportBtn.setOnMouseExited(e -> exportBtn.setStyle(
                "-fx-background-color: #1976d2;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 6 14 6 14;"
        ));
        exportBtn.setOnAction(e -> JsonExporter.export(controller.getBoardModel(), stage));

        Button settingsBtn = new Button("Ajustes");
        settingsBtn.setStyle(
                "-fx-background-color: #f5f5f5;" +
                        "-fx-text-fill: #424242;" +
                        "-fx-font-size: 12px;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 6 14 6 14;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 6;"
        );
        settingsBtn.setOnAction(e -> settingsPanel.toggle());

        HBox topBar = new HBox(12);
        topBar.setPadding(new Insets(12, 20, 12, 20));
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 0 0 1 0;");
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.getChildren().addAll(titleLabel, titleEdit, spacer, searchField, exportBtn, settingsBtn);

        settingsPanel.setOnDarkMode(dark -> {
            controller.setDarkMode(dark, board);
            settingsPanel.applyTheme(dark);

            String topBg = dark ? "#13131f" : "white";
            String topBorder = dark ? "#2d2d44" : "#e0e0e0";
            String titleColor = dark ? "#e0e0e0" : "#212121";
            String searchBorder = dark ? "#2d2d44" : "#e0e0e0";
            String searchBg = dark ? "#1e1e2e" : "white";
            String searchText = dark ? "#e0e0e0" : "#212121";

            topBar.setStyle("-fx-background-color: " + topBg + "; -fx-border-color: " + topBorder + "; -fx-border-width: 0 0 1 0;");
            titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + titleColor + "; -fx-cursor: hand;");
            searchField.setStyle(
                    "-fx-background-radius: 6;" +
                            "-fx-border-radius: 6;" +
                            "-fx-border-color: " + searchBorder + ";" +
                            "-fx-border-width: 1;" +
                            "-fx-padding: 5 10 5 10;" +
                            "-fx-font-size: 12px;" +
                            "-fx-background-color: " + searchBg + ";" +
                            "-fx-text-fill: " + searchText + ";"
            );
            settingsBtn.setStyle(
                    "-fx-background-color: " + (dark ? "#2d2d44" : "#f5f5f5") + ";" +
                            "-fx-text-fill: " + (dark ? "#e0e0e0" : "#424242") + ";" +
                            "-fx-font-size: 12px;" +
                            "-fx-cursor: hand;" +
                            "-fx-background-radius: 6;" +
                            "-fx-padding: 6 14 6 14;" +
                            "-fx-border-color: " + (dark ? "#3d3d5c" : "#e0e0e0") + ";" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 6;"
            );
        });

        settingsPanel.setOnFontSize(size -> controller.setFontSize(size));
        settingsPanel.setOnSortByPriority(() -> controller.sortAllByPriority());
        settingsPanel.setOnSortByDate(() -> controller.sortAllByDate());

        StackPane centerStack = new StackPane();
        centerStack.getChildren().add(board);
        StackPane.setAlignment(settingsPanel, Pos.CENTER_RIGHT);
        centerStack.getChildren().add(settingsPanel);

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(centerStack);

        Scene scene = new Scene(root, 1100, 680);
        stage.setTitle("FlowBoard");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
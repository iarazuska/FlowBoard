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
        titleLabel.getStyleClass().add("app-title");

        TextField titleEdit = new TextField("FlowBoard");
        titleEdit.getStyleClass().add("title-edit");
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
        searchField.setPrefWidth(220);
        searchField.getStyleClass().add("search-field");
        searchField.textProperty().addListener((obs, oldVal, newVal) ->
                controller.filterCards(newVal.trim().toLowerCase())
        );

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button exportBtn = new Button("Exportar JSON");
        exportBtn.getStyleClass().addAll("btn", "btn-primary");
        exportBtn.setOnAction(e -> JsonExporter.export(controller.getBoardModel(), stage));

        Button settingsBtn = new Button("Ajustes");
        settingsBtn.getStyleClass().addAll("btn", "btn-secondary");
        settingsBtn.setOnAction(e -> settingsPanel.toggle());

        HBox topBar = new HBox(12);
        topBar.getStyleClass().add("top-bar");
        topBar.setPadding(new Insets(14, 20, 14, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.getChildren().addAll(titleLabel, titleEdit, spacer, searchField, exportBtn, settingsBtn);

        StackPane centerStack = new StackPane();
        centerStack.getChildren().add(board);
        StackPane.setAlignment(settingsPanel, Pos.CENTER_RIGHT);
        centerStack.getChildren().add(settingsPanel);

        BorderPane root = new BorderPane();
        root.getStyleClass().add("app-root");
        root.setTop(topBar);
        root.setCenter(centerStack);

        settingsPanel.setOnFontSize(size -> {
            root.getStyleClass().removeAll("font-small", "font-large");
            if (size.equals("small") || size.equals("large")) {
                root.getStyleClass().add("font-" + size);
            }
        });

        settingsPanel.setOnSortByPriority(() -> controller.sortAllByPriority());
        settingsPanel.setOnSortByDate(() -> controller.sortAllByDate());

        Scene scene = new Scene(root, 1100, 680);
        scene.getStylesheets().add(getClass().getResource("flowboard.css").toExternalForm());
        stage.setTitle("FlowBoard");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
package com.flowboard.controller;

import com.flowboard.model.BoardModel;
import com.flowboard.model.Card;
import com.flowboard.model.Column;
import com.flowboard.view.ColumnView;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainController {

    private BoardModel boardModel;
    private HBox columnsContainer;
    private boolean darkMode = false;

    public MainController() {
        this.boardModel = new BoardModel();
    }

    public ScrollPane buildUI() {
        columnsContainer = new HBox();
        columnsContainer.setSpacing(16);
        columnsContainer.setPadding(new Insets(20));
        columnsContainer.setStyle("-fx-background-color: #ebecf0;");

        for (Column column : boardModel.getColumns()) {
            addColumnView(column);
        }

        columnsContainer.getChildren().add(buildAddColumnButton());

        ScrollPane scrollPane = new ScrollPane(columnsContainer);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: #ebecf0; -fx-background-color: #ebecf0;");
        return scrollPane;
    }

    private Button buildAddColumnButton() {
        Button addColumnBtn = new Button("+ Nueva columna");
        String lightStyle =
                "-fx-background-color: #ffffff55;" +
                        "-fx-text-fill: #424242;" +
                        "-fx-font-size: 13px;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10 16 10 16;";
        String darkStyle =
                "-fx-background-color: #16213e;" +
                        "-fx-text-fill: #e8e8f0;" +
                        "-fx-font-size: 13px;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: #0f3460;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-padding: 10 16 10 16;";
        addColumnBtn.setStyle(darkMode ? darkStyle : lightStyle);
        addColumnBtn.setOnAction(e -> showAddColumnDialog());
        return addColumnBtn;
    }

    private void addColumnView(Column column) {
        ColumnView columnView = new ColumnView(
                column,
                this::showAddCardDialog,
                this::showEditCardDialog,
                card -> deleteCard(card)
        );
        columnView.setOnCardDropped(cardId -> {
            Card card = boardModel.findCardById(cardId);
            Column sourceColumn = boardModel.findColumnByCardId(cardId);
            if (card != null && sourceColumn != null && !sourceColumn.getId().equals(column.getId())) {
                sourceColumn.removeCard(card);
                column.addCard(card);
                refreshAllColumns();
            }
        });
        columnView.refreshCards(column.getCards());
        columnsContainer.getChildren().add(columnView);
    }

    private void refreshAllColumns() {
        columnsContainer.getChildren().clear();
        for (Column column : boardModel.getColumns()) {
            addColumnView(column);
        }
        columnsContainer.getChildren().add(buildAddColumnButton());

        if (darkMode) {
            String columnBg = "#16213e";
            String columnBorder = "#0f3460";
            String titleColor = "#e8e8f0";
            String countBg = "#0f3460";
            String countText = "#a0a0c0";
            String cardBg = "#1e2a4a";
            String cardBorder = "#2d3d6b";
            String cardText = "#e8e8f0";
            String cardDesc = "#8888aa";
            String addBtn = "#8888aa";

            for (javafx.scene.Node node : columnsContainer.getChildren()) {
                if (node instanceof ColumnView columnView) {
                    columnView.applyTheme(columnBg, columnBorder, titleColor, countBg, countText, cardBg, cardBorder, cardText, cardDesc, addBtn);
                }
            }
        }
    }

    private void showAddCardDialog(Column column) {
        Dialog<Card> dialog = new Dialog<>();
        dialog.setTitle("Nueva tarjeta");
        dialog.setHeaderText("Añadir tarjeta a: " + column.getName());

        ButtonType createBtn = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createBtn, ButtonType.CANCEL);

        TextField titleField = new TextField();
        titleField.setPromptText("Título");

        TextArea descField = new TextArea();
        descField.setPromptText("Descripción");
        descField.setPrefRowCount(3);

        ColorPicker colorPicker = new ColorPicker(javafx.scene.paint.Color.web("#1976d2"));

        ChoiceBox<Card.Priority> priorityBox = new ChoiceBox<>();
        priorityBox.getItems().addAll(Card.Priority.values());
        priorityBox.setValue(Card.Priority.MEDIUM);

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Fecha límite (opcional)");

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.getChildren().addAll(
                new Label("Título:"), titleField,
                new Label("Descripción:"), descField,
                new Label("Color:"), colorPicker,
                new Label("Prioridad:"), priorityBox,
                new Label("Fecha límite:"), datePicker
        );

        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(btn -> {
            if (btn == createBtn && !titleField.getText().isBlank()) {
                return new Card(
                        titleField.getText(),
                        descField.getText(),
                        toHex(colorPicker.getValue()),
                        datePicker.getValue(),
                        priorityBox.getValue()
                );
            }
            return null;
        });

        dialog.showAndWait().ifPresent(card -> {
            column.addCard(card);
            refreshAllColumns();
        });
    }

    private void showEditCardDialog(Card card) {
        Dialog<Card> dialog = new Dialog<>();
        dialog.setTitle("Editar tarjeta");
        dialog.setHeaderText("Editando: " + card.getTitle());

        ButtonType saveBtn = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        TextField titleField = new TextField(card.getTitle());
        TextArea descField = new TextArea(card.getDescription());
        descField.setPrefRowCount(3);

        ColorPicker colorPicker = new ColorPicker(javafx.scene.paint.Color.web(card.getColor()));

        ChoiceBox<Card.Priority> priorityBox = new ChoiceBox<>();
        priorityBox.getItems().addAll(Card.Priority.values());
        priorityBox.setValue(card.getPriority());

        DatePicker datePicker = new DatePicker(card.getDueDate());

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.getChildren().addAll(
                new Label("Título:"), titleField,
                new Label("Descripción:"), descField,
                new Label("Color:"), colorPicker,
                new Label("Prioridad:"), priorityBox,
                new Label("Fecha límite:"), datePicker
        );

        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn && !titleField.getText().isBlank()) {
                card.setTitle(titleField.getText());
                card.setDescription(descField.getText());
                card.setColor(toHex(colorPicker.getValue()));
                card.setPriority(priorityBox.getValue());
                card.setDueDate(datePicker.getValue());
                return card;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(c -> refreshAllColumns());
    }

    private void deleteCard(Card card) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Borrar tarjeta");
        alert.setHeaderText("¿Borrar \"" + card.getTitle() + "\"?");
        alert.setContentText("Esta acción no se puede deshacer.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Column column = boardModel.findColumnByCardId(card.getId());
                if (column != null) {
                    column.removeCard(card);
                    refreshAllColumns();
                }
            }
        });
    }

    private void showAddColumnDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nueva columna");
        dialog.setHeaderText("Nombre de la columna");
        dialog.setContentText("Nombre:");
        dialog.showAndWait().ifPresent(name -> {
            if (!name.isBlank()) {
                boardModel.addColumn(name);
                refreshAllColumns();
            }
        });
    }

    private String toHex(javafx.scene.paint.Color color) {
        return String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
    }

    public BoardModel getBoardModel() { return boardModel; }

    public void filterCards(String query) {
        for (javafx.scene.Node node : columnsContainer.getChildren()) {
            if (node instanceof ColumnView columnView) {
                columnView.filterCards(query);
            }
        }
    }

    public void setDarkMode(boolean dark, javafx.scene.control.ScrollPane board) {
        this.darkMode = dark;
        String bg = dark ? "#1a1a2e" : "#ebecf0";
        String columnBg = dark ? "#16213e" : "#f4f5f7";
        String columnBorder = dark ? "#0f3460" : "#e0e0e0";
        String titleColor = dark ? "#e8e8f0" : "#424242";
        String countBg = dark ? "#0f3460" : "#e0e0e0";
        String countText = dark ? "#a0a0c0" : "#616161";
        String cardBg = dark ? "#1e2a4a" : "white";
        String cardBorder = dark ? "#2d3d6b" : "#e0e0e0";
        String cardText = dark ? "#e8e8f0" : "#212121";
        String cardDesc = dark ? "#8888aa" : "#757575";
        String addBtn = dark ? "#8888aa" : "#757575";

        columnsContainer.setStyle("-fx-background-color: " + bg + ";");
        board.setStyle("-fx-background: " + bg + "; -fx-background-color: " + bg + ";");

        for (javafx.scene.Node node : columnsContainer.getChildren()) {
            if (node instanceof ColumnView columnView) {
                columnView.applyTheme(columnBg, columnBorder, titleColor, countBg, countText, cardBg, cardBorder, cardText, cardDesc, addBtn);
            }
        }
    }

    public void setFontSize(String size) {
        int titleSize = size.equals("small") ? 12 : size.equals("large") ? 16 : 14;
        int descSize = size.equals("small") ? 10 : size.equals("large") ? 13 : 11;
        for (javafx.scene.Node node : columnsContainer.getChildren()) {
            if (node instanceof ColumnView columnView) {
                columnView.setFontSize(titleSize, descSize);
            }
        }
    }

    public void sortAllByPriority() {
        for (Column col : boardModel.getColumns()) {
            col.getCards().sort((a, b) -> a.getPriority().ordinal() - b.getPriority().ordinal());
        }
        refreshAllColumns();
    }

    public void sortAllByDate() {
        for (Column col : boardModel.getColumns()) {
            col.getCards().sort((a, b) -> {
                if (a.getDueDate() == null && b.getDueDate() == null) return 0;
                if (a.getDueDate() == null) return 1;
                if (b.getDueDate() == null) return -1;
                return a.getDueDate().compareTo(b.getDueDate());
            });
        }
        refreshAllColumns();
    }
}
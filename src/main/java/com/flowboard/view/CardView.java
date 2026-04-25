package com.flowboard.view;

import com.flowboard.model.Card;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.function.Consumer;

public class CardView extends VBox {

    private Card card;
    private Consumer<Card> onEdit;
    private Consumer<Card> onDelete;

    private String cardStyle =
            "-fx-background-color: white;" +
                    "-fx-background-radius: 8;" +
                    "-fx-border-radius: 8;" +
                    "-fx-border-color: #e0e0e0;" +
                    "-fx-border-width: 1;" +
                    "-fx-cursor: hand;";

    private String cardHoverStyle =
            "-fx-background-color: #f9f9f9;" +
                    "-fx-background-radius: 8;" +
                    "-fx-border-radius: 8;" +
                    "-fx-border-color: #bdbdbd;" +
                    "-fx-border-width: 1;" +
                    "-fx-cursor: hand;";

    public CardView(Card card) {
        this.card = card;
        build();
    }

    private void build() {
        setStyle(cardStyle);
        setPadding(new Insets(10));
        setSpacing(6);
        setPrefWidth(220);

        setOnMouseEntered(e -> setStyle(cardHoverStyle));
        setOnMouseExited(e -> setStyle(cardStyle));

        HBox colorBar = new HBox();
        colorBar.setPrefHeight(4);
        colorBar.setStyle("-fx-background-color: " + card.getColor() + ";" +
                "-fx-background-radius: 4;");

        Label titleLabel = new Label(card.getTitle());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #212121;");
        titleLabel.setWrapText(true);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        Button editBtn = new Button("✎");
        editBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #9e9e9e; -fx-cursor: hand; -fx-padding: 0 4 0 4;");
        editBtn.setOnMouseEntered(e -> editBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #424242; -fx-cursor: hand; -fx-padding: 0 4 0 4;"));
        editBtn.setOnMouseExited(e -> editBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #9e9e9e; -fx-cursor: hand; -fx-padding: 0 4 0 4;"));
        editBtn.setOnAction(e -> {
            if (onEdit != null) onEdit.accept(card);
            e.consume();
        });

        Button deleteBtn = new Button("✕");
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #9e9e9e; -fx-cursor: hand; -fx-padding: 0 4 0 4;");
        deleteBtn.setOnMouseEntered(e -> deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #e53935; -fx-cursor: hand; -fx-padding: 0 4 0 4;"));
        deleteBtn.setOnMouseExited(e -> deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #9e9e9e; -fx-cursor: hand; -fx-padding: 0 4 0 4;"));
        deleteBtn.setOnAction(e -> {
            if (onDelete != null) onDelete.accept(card);
            e.consume();
        });

        HBox topBar = new HBox();
        topBar.setSpacing(4);
        topBar.getChildren().addAll(titleLabel, editBtn, deleteBtn);

        Label descLabel = new Label(card.getDescription());
        descLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #757575;");
        descLabel.setWrapText(true);

        HBox footer = new HBox();
        footer.setSpacing(6);

        Label priorityBadge = buildPriorityBadge();
        footer.getChildren().add(priorityBadge);

        if (card.getDueDate() != null) {
            Label dateLabel = new Label(card.getDueDate().toString());
            boolean overdue = card.getDueDate().isBefore(LocalDate.now());
            dateLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: " +
                    (overdue ? "#e53935" : "#757575") + ";");
            footer.getChildren().add(dateLabel);
        }

        getChildren().addAll(colorBar, topBar, descLabel, footer);
    }

    private Label buildPriorityBadge() {
        Label badge = new Label();
        switch (card.getPriority()) {
            case HIGH -> {
                badge.setText("Alta");
                badge.setStyle("-fx-background-color: #ffebee; -fx-text-fill: #c62828;" +
                        "-fx-font-size: 10px; -fx-padding: 2 6 2 6; -fx-background-radius: 4;");
            }
            case MEDIUM -> {
                badge.setText("Media");
                badge.setStyle("-fx-background-color: #fff3e0; -fx-text-fill: #e65100;" +
                        "-fx-font-size: 10px; -fx-padding: 2 6 2 6; -fx-background-radius: 4;");
            }
            case LOW -> {
                badge.setText("Baja");
                badge.setStyle("-fx-background-color: #e8f5e9; -fx-text-fill: #2e7d32;" +
                        "-fx-font-size: 10px; -fx-padding: 2 6 2 6; -fx-background-radius: 4;");
            }
        }
        return badge;
    }

    public Card getCard() { return card; }

    public void setOnEdit(Consumer<Card> onEdit) { this.onEdit = onEdit; }

    public void setOnDelete(Consumer<Card> onDelete) { this.onDelete = onDelete; }

    public void updateFontSize(int titleSize, int descSize) {
        getChildren().forEach(node -> {
            if (node instanceof HBox hbox) {
                hbox.getChildren().forEach(child -> {
                    if (child instanceof Label label && label.getStyle().contains("font-weight: bold")) {
                        label.setStyle("-fx-font-weight: bold; -fx-font-size: " + titleSize + "px; -fx-text-fill: #212121;");
                    }
                });
            }
            if (node instanceof Label label && label.getStyle().contains("#757575")) {
                label.setStyle("-fx-font-size: " + descSize + "px; -fx-text-fill: #757575;");
            }
        });
    }

    public void applyTheme(String cardBg, String cardBorder, String cardText, String cardDesc) {
        this.cardStyle =
                "-fx-background-color: " + cardBg + ";" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-radius: 8;" +
                        "-fx-border-color: " + cardBorder + ";" +
                        "-fx-border-width: 1;" +
                        "-fx-cursor: hand;";

        this.cardHoverStyle =
                "-fx-background-color: " + (cardBg.equals("white") ? "#f9f9f9" : "#263561") + ";" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-radius: 8;" +
                        "-fx-border-color: " + cardBorder + ";" +
                        "-fx-border-width: 1;" +
                        "-fx-cursor: hand;";

        setStyle(cardStyle);

        getChildren().forEach(node -> {
            if (node instanceof HBox hbox) {
                hbox.getChildren().forEach(child -> {
                    if (child instanceof Label label && label.getStyle().contains("font-weight: bold")) {
                        label.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: " + cardText + ";");
                    }
                });
            }
            if (node instanceof Label label && label.getStyle().contains("#757575")) {
                label.setStyle("-fx-font-size: 11px; -fx-text-fill: " + cardDesc + ";");
            }
        });
    }
}
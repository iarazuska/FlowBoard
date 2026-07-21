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

    public CardView(Card card) {
        this.card = card;
        build();
    }

    private void build() {
        getStyleClass().add("card");
        setPadding(new Insets(12));
        setSpacing(8);
        setPrefWidth(220);

        HBox colorBar = new HBox();
        colorBar.setPrefHeight(4);
        colorBar.getStyleClass().add("card-color-bar");
        colorBar.setStyle("-fx-background-color: " + card.getColor() + ";");

        Label titleLabel = new Label(card.getTitle());
        titleLabel.getStyleClass().add("card-title");
        titleLabel.setWrapText(true);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        Button editBtn = new Button("✎");
        editBtn.getStyleClass().add("icon-btn");
        editBtn.setOnAction(e -> {
            if (onEdit != null) onEdit.accept(card);
            e.consume();
        });

        Button deleteBtn = new Button("✕");
        deleteBtn.getStyleClass().addAll("icon-btn", "icon-btn-delete");
        deleteBtn.setOnAction(e -> {
            if (onDelete != null) onDelete.accept(card);
            e.consume();
        });

        HBox topBar = new HBox();
        topBar.setSpacing(4);
        topBar.getChildren().addAll(titleLabel, editBtn, deleteBtn);

        Label descLabel = new Label(card.getDescription());
        descLabel.getStyleClass().add("card-desc");
        descLabel.setWrapText(true);

        HBox footer = new HBox();
        footer.setSpacing(6);

        Label priorityBadge = buildPriorityBadge();
        footer.getChildren().add(priorityBadge);

        if (card.getDueDate() != null) {
            Label dateLabel = new Label(card.getDueDate().toString());
            boolean overdue = card.getDueDate().isBefore(LocalDate.now());
            dateLabel.getStyleClass().add(overdue ? "card-date-overdue" : "card-date");
            footer.getChildren().add(dateLabel);
        }

        getChildren().addAll(colorBar, topBar, descLabel, footer);
    }

    private Label buildPriorityBadge() {
        Label badge = new Label();
        badge.getStyleClass().add("priority-badge");
        switch (card.getPriority()) {
            case HIGH -> {
                badge.setText("Alta");
                badge.getStyleClass().add("priority-high");
            }
            case MEDIUM -> {
                badge.setText("Media");
                badge.getStyleClass().add("priority-medium");
            }
            case LOW -> {
                badge.setText("Baja");
                badge.getStyleClass().add("priority-low");
            }
        }
        return badge;
    }

    public Card getCard() { return card; }

    public void setOnEdit(Consumer<Card> onEdit) { this.onEdit = onEdit; }

    public void setOnDelete(Consumer<Card> onDelete) { this.onDelete = onDelete; }
}
package com.flowboard.view;

import com.flowboard.model.Card;
import com.flowboard.model.Column;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class ColumnView extends VBox {

    private Column column;
    private VBox cardsContainer;
    private Consumer<Column> onAddCard;
    private Consumer<Card> onEditCard;
    private Consumer<Card> onDeleteCard;

    public ColumnView(Column column, Consumer<Column> onAddCard,
                      Consumer<Card> onEditCard, Consumer<Card> onDeleteCard) {
        this.column = column;
        this.onAddCard = onAddCard;
        this.onEditCard = onEditCard;
        this.onDeleteCard = onDeleteCard;
        build();
    }

    private void build() {
        getStyleClass().add("column");
        setPadding(new Insets(14));
        setSpacing(10);
        setPrefWidth(260);

        Label titleLabel = new Label(column.getName());
        titleLabel.getStyleClass().add("column-title");

        Label countBadge = new Label(String.valueOf(column.getCards().size()));
        countBadge.getStyleClass().add("column-count");

        HBox titleRow = new HBox(8);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        titleRow.getChildren().addAll(titleLabel, countBadge);

        cardsContainer = new VBox();
        cardsContainer.setSpacing(10);

        ScrollPane scrollPane = new ScrollPane(cardsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);
        scrollPane.getStyleClass().add("column-scroll");

        Button addButton = new Button("+ Añadir tarjeta");
        addButton.getStyleClass().add("add-card-btn");
        addButton.setMaxWidth(Double.MAX_VALUE);
        addButton.setOnAction(e -> onAddCard.accept(column));

        getChildren().addAll(titleRow, scrollPane, addButton);

        setupDragTarget();
    }

    private void setupDragTarget() {
        setOnDragOver(e -> {
            if (e.getDragboard().hasString()) {
                e.acceptTransferModes(javafx.scene.input.TransferMode.MOVE);
                if (!getStyleClass().contains("drag-over")) getStyleClass().add("drag-over");
            }
            e.consume();
        });

        setOnDragExited(e -> getStyleClass().remove("drag-over"));

        setOnDragDropped(e -> {
            String cardId = e.getDragboard().getString();
            e.setDropCompleted(true);
            e.consume();
        });
    }

    public void refreshCards(java.util.List<Card> cards) {
        cardsContainer.getChildren().clear();
        for (Card card : cards) {
            CardView cardView = new CardView(card);
            setupCardDrag(cardView);
            if (onEditCard != null) cardView.setOnEdit(onEditCard);
            if (onDeleteCard != null) cardView.setOnDelete(onDeleteCard);
            cardsContainer.getChildren().add(cardView);
        }
    }

    private void setupCardDrag(CardView cardView) {
        cardView.setOnDragDetected(e -> {
            javafx.scene.input.Dragboard db = cardView.startDragAndDrop(javafx.scene.input.TransferMode.MOVE);
            javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
            content.putString(cardView.getCard().getId());
            db.setContent(content);
            e.consume();
        });
    }

    public Column getColumn() { return column; }

    public Consumer<String> getOnCardDropped() { return null; }

    public void setOnCardDropped(Consumer<String> onCardDropped) {
        setOnDragDropped(e -> {
            String cardId = e.getDragboard().getString();
            onCardDropped.accept(cardId);
            e.setDropCompleted(true);
            e.consume();
        });
    }

    public void filterCards(String query) {
        for (javafx.scene.Node node : cardsContainer.getChildren()) {
            if (node instanceof CardView cardView) {
                boolean matches = query.isEmpty() ||
                        cardView.getCard().getTitle().toLowerCase().contains(query) ||
                        cardView.getCard().getDescription().toLowerCase().contains(query);
                cardView.setVisible(matches);
                cardView.setManaged(matches);
            }
        }
    }
}
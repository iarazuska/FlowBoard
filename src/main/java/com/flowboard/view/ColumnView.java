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

    private static final String COLUMN_STYLE =
            "-fx-background-color: #f4f5f7;" +
                    "-fx-background-radius: 10;" +
                    "-fx-border-radius: 10;" +
                    "-fx-border-color: #e0e0e0;" +
                    "-fx-border-width: 1;";

    private static final String COLUMN_DRAG_STYLE =
            "-fx-background-color: #e3f2fd;" +
                    "-fx-background-radius: 10;" +
                    "-fx-border-radius: 10;" +
                    "-fx-border-color: #1976d2;" +
                    "-fx-border-width: 2;";

    public ColumnView(Column column, Consumer<Column> onAddCard,
                      Consumer<Card> onEditCard, Consumer<Card> onDeleteCard) {
        this.column = column;
        this.onAddCard = onAddCard;
        this.onEditCard = onEditCard;
        this.onDeleteCard = onDeleteCard;
        build();
    }

    private void build() {
        setStyle(COLUMN_STYLE);
        setPadding(new Insets(12));
        setSpacing(8);
        setPrefWidth(260);

        Label titleLabel = new Label(column.getName());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #424242;");

        Label countBadge = new Label(String.valueOf(column.getCards().size()));
        countBadge.setStyle(
                "-fx-background-color: #e0e0e0;" +
                        "-fx-text-fill: #616161;" +
                        "-fx-font-size: 11px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 2 7 2 7;" +
                        "-fx-background-radius: 10;"
        );

        HBox titleRow = new HBox(6);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        titleRow.getChildren().addAll(titleLabel, countBadge);

        cardsContainer = new VBox();
        cardsContainer.setSpacing(8);

        ScrollPane scrollPane = new ScrollPane(cardsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        Button addButton = new Button("+ Añadir tarjeta");
        addButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #757575;" +
                        "-fx-font-size: 12px;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-color: transparent;"
        );
        addButton.setOnMouseEntered(e -> addButton.setStyle(
                "-fx-background-color: #e0e0e0;" +
                        "-fx-text-fill: #424242;" +
                        "-fx-font-size: 12px;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-radius: 6;"
        ));
        addButton.setOnMouseExited(e -> addButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #757575;" +
                        "-fx-font-size: 12px;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-color: transparent;"
        ));
        addButton.setOnAction(e -> onAddCard.accept(column));

        getChildren().addAll(titleRow, scrollPane, addButton);

        setupDragTarget();
    }

    private void setupDragTarget() {
        setOnDragOver(e -> {
            if (e.getDragboard().hasString()) {
                e.acceptTransferModes(javafx.scene.input.TransferMode.MOVE);
                setStyle(COLUMN_DRAG_STYLE);
            }
            e.consume();
        });

        setOnDragExited(e -> setStyle(COLUMN_STYLE));

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

    public void setFontSize(int titleSize, int descSize) {
        for (javafx.scene.Node node : cardsContainer.getChildren()) {
            if (node instanceof CardView cardView) {
                cardView.updateFontSize(titleSize, descSize);
            }
        }
    }

    public void applyTheme(String columnBg, String columnBorder, String titleColor,
                           String countBg, String countText, String cardBg, String cardBorder,
                           String cardText, String cardDesc, String addBtnColor) {
        setStyle(
                "-fx-background-color: " + columnBg + ";" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-color: " + columnBorder + ";" +
                        "-fx-border-width: 1;"
        );

        getChildren().forEach(node -> {
            if (node instanceof HBox hbox) {
                hbox.getChildren().forEach(child -> {
                    if (child instanceof Label label && label.getStyle().contains("font-weight: bold")) {
                        label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: " + titleColor + ";");
                    }
                    if (child instanceof Label label && label.getStyle().contains("background-radius: 10")) {
                        label.setStyle(
                                "-fx-background-color: " + countBg + ";" +
                                        "-fx-text-fill: " + countText + ";" +
                                        "-fx-font-size: 11px;" +
                                        "-fx-font-weight: bold;" +
                                        "-fx-padding: 2 7 2 7;" +
                                        "-fx-background-radius: 10;"
                        );
                    }
                });
            }
            if (node instanceof Button btn) {
                btn.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-text-fill: " + addBtnColor + ";" +
                                "-fx-font-size: 12px;" +
                                "-fx-cursor: hand;" +
                                "-fx-border-color: transparent;"
                );
            }
        });

        for (javafx.scene.Node node : cardsContainer.getChildren()) {
            if (node instanceof CardView cardView) {
                cardView.applyTheme(cardBg, cardBorder, cardText, cardDesc);
            }
        }
    }
}
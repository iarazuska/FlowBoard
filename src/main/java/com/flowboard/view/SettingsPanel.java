package com.flowboard.view;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.function.Consumer;

public class SettingsPanel extends VBox {

    private boolean visible = false;
    private Consumer<String> onFontSize;
    private Runnable onSortByPriority;
    private Runnable onSortByDate;

    private static final int PANEL_WIDTH = 280;

    public SettingsPanel() {
        getStyleClass().add("settings-panel");
        setPrefWidth(PANEL_WIDTH);
        setMinWidth(PANEL_WIDTH);
        setMaxWidth(PANEL_WIDTH);
        setPadding(new Insets(20));
        setSpacing(20);
        setTranslateX(PANEL_WIDTH);
        build();
    }

    private void build() {
        Label header = new Label("Ajustes");
        header.getStyleClass().add("settings-header");

        Separator sep1 = new Separator();

        Label fontLabel = new Label("Tamaño de fuente");
        fontLabel.getStyleClass().add("settings-section-label");

        ToggleGroup fontGroup = new ToggleGroup();
        ToggleButton small = new ToggleButton("Pequeña");
        ToggleButton medium = new ToggleButton("Normal");
        ToggleButton large = new ToggleButton("Grande");
        small.getStyleClass().add("chip");
        medium.getStyleClass().add("chip");
        large.getStyleClass().add("chip");
        small.setToggleGroup(fontGroup);
        medium.setToggleGroup(fontGroup);
        large.setToggleGroup(fontGroup);
        medium.setSelected(true);

        fontGroup.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            if (newT == null) { medium.setSelected(true); return; }
            String size = newT == small ? "small" : newT == large ? "large" : "medium";
            if (onFontSize != null) onFontSize.accept(size);
        });

        HBox fontRow = new HBox(8, small, medium, large);
        fontRow.setAlignment(Pos.CENTER_LEFT);

        Separator sep2 = new Separator();

        Label sortLabel = new Label("Ordenar tarjetas");
        sortLabel.getStyleClass().add("settings-section-label");

        Button sortPriority = new Button("Por prioridad");
        sortPriority.getStyleClass().add("settings-action-btn");
        sortPriority.setMaxWidth(Double.MAX_VALUE);
        sortPriority.setOnAction(e -> { if (onSortByPriority != null) onSortByPriority.run(); });

        Button sortDate = new Button("Por fecha límite");
        sortDate.getStyleClass().add("settings-action-btn");
        sortDate.setMaxWidth(Double.MAX_VALUE);
        sortDate.setOnAction(e -> { if (onSortByDate != null) onSortByDate.run(); });

        getChildren().addAll(
                header, sep1,
                fontLabel, fontRow,
                sep2,
                sortLabel, sortPriority, sortDate
        );
    }

    public void toggle() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(250), this);
        if (!visible) {
            tt.setToX(0);
        } else {
            tt.setToX(PANEL_WIDTH);
        }
        visible = !visible;
        tt.play();
    }

    public void setOnFontSize(Consumer<String> onFontSize) { this.onFontSize = onFontSize; }
    public void setOnSortByPriority(Runnable onSortByPriority) { this.onSortByPriority = onSortByPriority; }
    public void setOnSortByDate(Runnable onSortByDate) { this.onSortByDate = onSortByDate; }
}

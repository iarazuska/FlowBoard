package com.flowboard.view;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.function.Consumer;

public class SettingsPanel extends VBox {

    private boolean visible = false;
    private Consumer<Boolean> onDarkMode;
    private Consumer<String> onFontSize;
    private Runnable onSortByPriority;
    private Runnable onSortByDate;

    private static final int PANEL_WIDTH = 280;

    public SettingsPanel() {
        setPrefWidth(PANEL_WIDTH);
        setMinWidth(PANEL_WIDTH);
        setMaxWidth(PANEL_WIDTH);
        setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-width: 0 0 0 1;"
        );
        setPadding(new Insets(20));
        setSpacing(20);
        setTranslateX(PANEL_WIDTH);
        build();
    }

    private void build() {
        Label header = new Label("Ajustes");
        header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #212121;");

        Separator sep1 = new Separator();

        Label themeLabel = new Label("Apariencia");
        themeLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #757575;");

        Label darkLabel = new Label("Tema oscuro");
        darkLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #424242;");
        ToggleButton darkToggle = new ToggleButton("OFF");
        darkToggle.setStyle(
                "-fx-background-color: #e0e0e0;" +
                        "-fx-text-fill: #757575;" +
                        "-fx-background-radius: 20;" +
                        "-fx-font-size: 11px;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 4 12 4 12;"
        );
        darkToggle.setOnAction(e -> {
            boolean dark = darkToggle.isSelected();
            darkToggle.setText(dark ? "ON" : "OFF");
            darkToggle.setStyle(
                    "-fx-background-color: " + (dark ? "#1976d2" : "#e0e0e0") + ";" +
                            "-fx-text-fill: " + (dark ? "white" : "#757575") + ";" +
                            "-fx-background-radius: 20;" +
                            "-fx-font-size: 11px;" +
                            "-fx-cursor: hand;" +
                            "-fx-padding: 4 12 4 12;"
            );
            if (onDarkMode != null) onDarkMode.accept(dark);
        });
        HBox darkRow = new HBox(darkLabel, new Region(), darkToggle);
        HBox.setHgrow(darkRow.getChildren().get(1), Priority.ALWAYS);
        darkRow.setAlignment(Pos.CENTER_LEFT);

        Separator sep2 = new Separator();

        Label fontLabel = new Label("Tamaño de fuente");
        fontLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #757575;");

        ToggleGroup fontGroup = new ToggleGroup();
        ToggleButton small = new ToggleButton("Pequeña");
        ToggleButton medium = new ToggleButton("Normal");
        ToggleButton large = new ToggleButton("Grande");
        small.setToggleGroup(fontGroup);
        medium.setToggleGroup(fontGroup);
        large.setToggleGroup(fontGroup);
        medium.setSelected(true);

        String btnStyle = "-fx-background-radius: 6; -fx-cursor: hand; -fx-font-size: 11px; -fx-padding: 5 10 5 10;";
        String activeStyle = btnStyle + "-fx-background-color: #1976d2; -fx-text-fill: white;";
        String inactiveStyle = btnStyle + "-fx-background-color: #f5f5f5; -fx-text-fill: #424242; -fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 6;";

        small.setStyle(inactiveStyle);
        medium.setStyle(activeStyle);
        large.setStyle(inactiveStyle);

        fontGroup.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            if (newT == null) { medium.setSelected(true); return; }
            small.setStyle(newT == small ? activeStyle : inactiveStyle);
            medium.setStyle(newT == medium ? activeStyle : inactiveStyle);
            large.setStyle(newT == large ? activeStyle : inactiveStyle);
            String size = newT == small ? "small" : newT == large ? "large" : "medium";
            if (onFontSize != null) onFontSize.accept(size);
        });

        HBox fontRow = new HBox(8, small, medium, large);
        fontRow.setAlignment(Pos.CENTER_LEFT);

        Separator sep3 = new Separator();

        Label sortLabel = new Label("Ordenar tarjetas");
        sortLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #757575;");

        Button sortPriority = new Button("Por prioridad");
        sortPriority.setStyle(
                "-fx-background-color: #f5f5f5;" +
                        "-fx-text-fill: #424242;" +
                        "-fx-font-size: 12px;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-radius: 6;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 6;" +
                        "-fx-padding: 7 14 7 14;"
        );
        sortPriority.setMaxWidth(Double.MAX_VALUE);
        sortPriority.setOnAction(e -> { if (onSortByPriority != null) onSortByPriority.run(); });

        Button sortDate = new Button("Por fecha límite");
        sortDate.setStyle(
                "-fx-background-color: #f5f5f5;" +
                        "-fx-text-fill: #424242;" +
                        "-fx-font-size: 12px;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-radius: 6;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 6;" +
                        "-fx-padding: 7 14 7 14;"
        );
        sortDate.setMaxWidth(Double.MAX_VALUE);
        sortDate.setOnAction(e -> { if (onSortByDate != null) onSortByDate.run(); });

        getChildren().addAll(
                header, sep1,
                themeLabel, darkRow,
                sep2,
                fontLabel, fontRow,
                sep3,
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

    public void setOnDarkMode(Consumer<Boolean> onDarkMode) { this.onDarkMode = onDarkMode; }
    public void setOnFontSize(Consumer<String> onFontSize) { this.onFontSize = onFontSize; }
    public void setOnSortByPriority(Runnable onSortByPriority) { this.onSortByPriority = onSortByPriority; }
    public void setOnSortByDate(Runnable onSortByDate) { this.onSortByDate = onSortByDate; }

    public void applyTheme(boolean dark) {
        String bg = dark ? "#1e1e2e" : "white";
        String border = dark ? "#2d2d44" : "#e0e0e0";
        String textPrimary = dark ? "#e0e0e0" : "#212121";
        String textSecondary = dark ? "#9e9e9e" : "#757575";
        String btnBg = dark ? "#2d2d44" : "#f5f5f5";
        String btnText = dark ? "#e0e0e0" : "#424242";
        String btnBorder = dark ? "#3d3d5c" : "#e0e0e0";

        setStyle(
                "-fx-background-color: " + bg + ";" +
                        "-fx-border-color: " + border + ";" +
                        "-fx-border-width: 0 0 0 1;"
        );

        getChildren().forEach(node -> {
            if (node instanceof Label label) {
                String style = label.getStyle();
                if (style.contains("16px")) {
                    label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + textPrimary + ";");
                } else if (style.contains("bold")) {
                    label.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + textSecondary + ";");
                } else {
                    label.setStyle("-fx-font-size: 13px; -fx-text-fill: " + textPrimary + ";");
                }
            }
            if (node instanceof HBox hbox) {
                hbox.getChildren().forEach(child -> {
                    if (child instanceof Label label) {
                        label.setStyle("-fx-font-size: 13px; -fx-text-fill: " + textPrimary + ";");
                    }
                });
            }
            if (node instanceof VBox vbox) {
                vbox.getChildren().forEach(child -> {
                    if (child instanceof Button btn) {
                        btn.setStyle(
                                "-fx-background-color: " + btnBg + ";" +
                                        "-fx-text-fill: " + btnText + ";" +
                                        "-fx-font-size: 12px;" +
                                        "-fx-cursor: hand;" +
                                        "-fx-background-radius: 6;" +
                                        "-fx-border-color: " + btnBorder + ";" +
                                        "-fx-border-width: 1;" +
                                        "-fx-border-radius: 6;" +
                                        "-fx-padding: 7 14 7 14;"
                        );
                    }
                });
            }
            if (node instanceof Button btn) {
                btn.setStyle(
                        "-fx-background-color: " + btnBg + ";" +
                                "-fx-text-fill: " + btnText + ";" +
                                "-fx-font-size: 12px;" +
                                "-fx-cursor: hand;" +
                                "-fx-background-radius: 6;" +
                                "-fx-border-color: " + btnBorder + ";" +
                                "-fx-border-width: 1;" +
                                "-fx-border-radius: 6;" +
                                "-fx-padding: 7 14 7 14;"
                );
            }
        });
    }
}
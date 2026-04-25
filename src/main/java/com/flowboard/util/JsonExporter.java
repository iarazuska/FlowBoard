package com.flowboard.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flowboard.model.BoardModel;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

public class JsonExporter {

    private static final ObjectMapper mapper = createMapper();

    private static ObjectMapper createMapper() {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.enable(SerializationFeature.INDENT_OUTPUT);
        return om;
    }

    public static void export(BoardModel boardModel, Window window) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar tablero");
        fileChooser.setInitialFileName("flowboard.json");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON", "*.json")
        );

        File file = fileChooser.showSaveDialog(window);
        if (file != null) {
            try {
                mapper.writeValue(file, boardModel);
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.INFORMATION
                );
                alert.setTitle("Exportado");
                alert.setHeaderText(null);
                alert.setContentText("Tablero exportado correctamente en:\n" + file.getAbsolutePath());
                alert.showAndWait();
            } catch (Exception e) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.ERROR
                );
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error al exportar: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }
}
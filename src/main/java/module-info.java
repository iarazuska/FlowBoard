module com.flowboard {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens com.flowboard to javafx.fxml;
    opens com.flowboard.model to com.fasterxml.jackson.databind;
    exports com.flowboard;
}
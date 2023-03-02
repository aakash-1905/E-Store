module sample.ecomm {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens sample.ecomm to javafx.fxml;
    exports sample.ecomm;
}
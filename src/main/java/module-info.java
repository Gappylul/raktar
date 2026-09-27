module hu.gappy.raktar {
    requires javafx.controls;
    requires javafx.fxml;


    opens hu.gappy.raktar to javafx.fxml;
    exports hu.gappy.raktar;
}
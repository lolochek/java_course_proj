module client {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens gui.controller to javafx.fxml;
    opens controllers to java.io;
    opens server to java.io;
    opens request.tdo to javafx.base;
    exports gui;
}

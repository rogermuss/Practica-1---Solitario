module com.example.interfazjuegosolitario {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires javafx.graphics;

    opens com.example.interfazjuegosolitario to javafx.fxml;
    exports com.example.interfazjuegosolitario;

    opens solitaire to javafx.fxml;   // 🔹 Necesario para acceder al controlador
    exports solitaire;
}

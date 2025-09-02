package solitaire;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Solitaire extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/interfaz" +
                "juegosolitario/SolitaireMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setFullScreen(false);
        stage.setTitle("Solitario - Bienvenida");
        stage.setScene(scene);
        stage.show();

        // Crear manager con el Stage
        SolitaireManager manager = new SolitaireManager(stage);

        // Pasar el manager al controlador de bienvenida
        SolitaireMenuController controller = fxmlLoader.getController();
        controller.setManager(manager);
    }



    public static void main(String[] args) {
        launch(args);
    }
}

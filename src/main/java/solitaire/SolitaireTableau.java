package solitaire;

import javafx.application.Application;
import javafx.stage.Stage;

public class SolitaireTableau extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Solitaire Tableau");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

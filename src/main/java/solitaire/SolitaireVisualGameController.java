package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Mazo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class SolitaireVisualGameController {

    Mazo mazo = new Mazo();
    ArrayList<StackPane> cartasGraficas = new ArrayList<>();

    // Foundations
    @FXML VBox f1;
    @FXML VBox f2;
    @FXML VBox f3;
    @FXML VBox f4;

    // Tableaus
    @FXML VBox t1;
    @FXML VBox t2;
    @FXML VBox t3;
    @FXML VBox t4;
    @FXML VBox t5;
    @FXML VBox t6;
    @FXML VBox t7;

    // Pila
    @FXML StackPane pozo;

    // Descarte
    @FXML StackPane zonaDescarte;

    private ArrayList<VBox> tableaus = new ArrayList<>();
    private ArrayList<VBox> foundations = new ArrayList<>();

    // Definir tamaño fijo para las cartas
    private final double CARD_WIDTH = 64.0;  // Mismo que en FXML
    private final double CARD_HEIGHT = 94.0; // Mismo que en FXML
    private final double CARD_OFFSET = 1.0; // Desplazamiento para el efecto escalera

    @FXML
    private void initialize() throws IOException {
        // Configurar VBoxes primero
        configurarVBoxes();

        // Generar los StackPane con su respectivo diseño
        generarCartas();

        // Crear arreglos para las referencias de mis VBox
        crearArreglos();

        // Agregar las cartas en forma descendente a mis Tableaus
        agregarCartasAlTableau();

        // Agregar ;as cartas restamtes al pozo
        agregarCartasAlPozo();

        //
    }

    public void configurarVBoxes() {
        // Configurar todos los VBox de tableaus
        for (VBox vbox : new VBox[]{t1, t2, t3, t4, t5, t6, t7}) {
            vbox.setFillWidth(false);
            vbox.setSpacing(-50); // Espaciado negativo para superponer cartas

        }


    }

    public void crearArreglos() {
        tableaus.add(t1);
        tableaus.add(t2);
        tableaus.add(t3);
        tableaus.add(t4);
        tableaus.add(t5);
        tableaus.add(t6);
        tableaus.add(t7);

        foundations.add(f1);
        foundations.add(f2);
        foundations.add(f3);
        foundations.add(f4);
    }

    public void generarCartas() throws IOException {
        for (CartaInglesa carta : mazo.getCartas()) {
            carta.makeFaceUp();

            // Carga un nuevo StackPane desde el FXML por cada carta
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example" +
                    "/interfazjuegosolitario/card.fxml"));
            StackPane cartaStackPane = loader.load();

            // Configurar tamaño fijo para la carta (IMPORTANTE)
            cartaStackPane.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
            cartaStackPane.setMaxSize(CARD_WIDTH, CARD_HEIGHT);
            cartaStackPane.setMinSize(CARD_WIDTH, CARD_HEIGHT);


            // Acceder a los elementos dentro del FXML
            StackPane visibleCard = (StackPane) cartaStackPane.lookup("#VisibleCard");
            Label labelTop = (Label) cartaStackPane.lookup("#LabelTop");
            Label labelCenter = (Label) cartaStackPane.lookup("#LabelCenter");
            Label labelBottom = (Label) cartaStackPane.lookup("#LabelBottom");

            // Configurar la carta según sus datos
                labelTop.setText(carta.toString());
                labelTop.setStyle("-fx-text-fill: " + carta.getColor() + "; -fx-font-weight: bold;");


                labelCenter.setText(carta.toString());
                labelCenter.setStyle("-fx-text-fill: " + carta.getColor() + "; -fx-font-weight: bold;");


                labelBottom.setText(carta.getPalo().getFigura());
                labelBottom.setStyle("-fx-text-fill: " + carta.getColor() + "; -fx-font-size: 40; -fx-font-weight: bold;");


                visibleCard.setVisible(false);

            cartaStackPane.setStyle("-fx-background-color: linear-gradient(to bottom, #1a6fc4, #0d4d8c); " +
                    "-fx-background-radius: 10; " +
                    "-fx-border-color: black; " +
                    "-fx-border-radius: 10; " +
                    "-fx-border-width: 1;");

            cartasGraficas.add(cartaStackPane);
        }
    }

    public void voltearCarta(StackPane cartaStackPane) throws IOException {

        StackPane visibleCard = (StackPane) cartaStackPane.lookup("#VisibleCard");

// Voltear la carta a boca arriba \\
        if(!visibleCard.isVisible()) {
            visibleCard.setVisible(true);
            cartaStackPane.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-background-radius: 10;" +
                            "-fx-border-color: black;" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 10;"
            );
        }
        else{
            visibleCard.setVisible(false);
            cartaStackPane.setStyle("-fx-background-color: linear-gradient(to bottom, #1a6fc4, #0d4d8c); " +
                    "-fx-background-radius: 10; " +
                    "-fx-border-color: black; " +
                    "-fx-border-radius: 10; " +
                    "-fx-border-width: 1;");
        }
    }


    public void agregarCartasAlTableau() throws IOException {
        int cartaIndex = 0;

        for (int i = 1; i <= 7; i++) {
            VBox vbox = tableaus.get(i - 1);

            for (int j = 1; j <= i; j++) {
                StackPane carta = cartasGraficas.get(cartaIndex);
                cartaIndex++;

                vbox.getChildren().add(carta);
                carta.setOpacity(1.0);
                VBox.setMargin(carta, new Insets((j - 1) * CARD_OFFSET, 0, 0, 0));
                voltearCarta(carta);
                if (j == i) {
                    StackPane visibleCard = (StackPane) carta.lookup("#VisibleCard");
                    if (visibleCard != null) {
                        visibleCard.setVisible(true);

                    }
                    carta.setStyle("-fx-background-color: white; " +
                            "-fx-background-radius: 10; " +
                            "-fx-border-color: black; " +
                            "-fx-border-radius: 10; " +
                            "-fx-border-width: 1;");

                    carta.setOnMouseEntered(event -> {
                        carta.setStyle("-fx-background-color: #f0f0f0; " + // Color más claro
                                "-fx-background-radius: 10; " +
                                "-fx-border-color: #0066cc; " +      // Borde azul
                                "-fx-border-radius: 10; " +
                                "-fx-border-width: 2; " +            // Borde más grueso
                                "-fx-effect: dropshadow(gaussian, #0066cc, 10, 0.5, 0, 0);");
                    });

                    carta.setOnMouseExited(event -> {
                        carta.setStyle("-fx-background-color: white; " +   // Volver al estilo normal
                                "-fx-background-radius: 10; " +
                                "-fx-border-color: black; " +
                                "-fx-border-radius: 10; " +
                                "-fx-border-width: 1; " +
                                "-fx-effect: null;");
                    });
                }
            }
        }
    }



    public void agregarCartasAlPozo() {
        HashSet<StackPane> cartasTableau = new HashSet<>();
        for (VBox vbox : tableaus) {
            for (javafx.scene.Node node : vbox.getChildren()) {
                if (node instanceof StackPane) {
                    cartasTableau.add((StackPane) node);
                }
            }
        }

        // LIMPIAR EL POZO PRIMERO - ESTA ES LA CLAVE
        pozo.getChildren().clear();

        for (StackPane carta : cartasGraficas) {
            if(!cartasTableau.contains(carta)) {
                // QUITAR TODOS LOS MÁRGENES PREVIOS de la carta
                VBox.setMargin(carta, Insets.EMPTY);

                pozo.getChildren().add(carta);

                carta.setStyle("-fx-background-color: linear-gradient(to bottom, #1a6fc4, #0d4d8c); " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-color: black; " +
                        "-fx-border-radius: 10; " +
                        "-fx-border-width: 1;");

                StackPane visibleCard = (StackPane) carta.lookup("#VisibleCard");
                if (visibleCard != null) {
                    visibleCard.setVisible(false); // Deben estar boca abajo en el pozo
                }

                // Remover event handlers de hover
                carta.setOnMouseEntered(null);
                carta.setOnMouseExited(null);
            }

        }
        // CREAR HOVER SOLO PARA LA ÚLTIMA CARTA
        if (!pozo.getChildren().isEmpty()) {
            StackPane ultimaCarta = (StackPane) pozo.getChildren().get(pozo.getChildren().size() - 1);

            ultimaCarta.setOnMouseEntered(event -> {
                ultimaCarta.setStyle("-fx-background-color: #1a7cd4; " + // Color más claro
                        "-fx-background-radius: 10; " +
                        "-fx-border-color: #0066cc; " +
                        "-fx-border-radius: 10; " +
                        "-fx-border-width: 2; " +
                        "-fx-effect: dropshadow(gaussian, #0066cc, 10, 0.5, 0, 0);");
            });

            ultimaCarta.setOnMouseExited(event -> {
                ultimaCarta.setStyle("-fx-background-color: linear-gradient(to bottom, #1a6fc4, #0d4d8c); " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-color: black; " +
                        "-fx-border-radius: 10; " +
                        "-fx-border-width: 1;");
            });
        }


        // Asegurar que el pozo tenga el tamaño correcto
        pozo.setMaxSize(CARD_WIDTH, CARD_HEIGHT);
        pozo.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
    }

    public void setMazo(Mazo mazo) {
        this.mazo = mazo;
    }


}
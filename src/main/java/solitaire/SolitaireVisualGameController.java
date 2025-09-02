package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Mazo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class SolitaireVisualGameController {
    // Reemplazar estas variables de instancia
    private StackPane cartaSeleccionada = null;
    private ArrayList<StackPane> cartasSeleccionadas = new ArrayList<>(); // Para múltiples cartas
    private javafx.scene.Parent contenedorOrigen = null; // Cambiar de VBox a Parent
    private double offsetX, offsetY;
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
        configurarDragAndDrop();
    }

    public void configurarVBoxes() {
        // Configurar todos los VBox de tableaus
        for (VBox vbox : new VBox[]{t1, t2, t3, t4, t5, t6, t7}) {
            vbox.setFillWidth(false);
            vbox.setSpacing(-50); // Espaciado negativo para superponer cartas
        }

        for (VBox vbox : new VBox[]{f1, f2, f3, f4}) {
            vbox.setFillWidth(false);
            vbox.setSpacing(-95);
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

// Voltear la carta a boca arriba
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


    public void agregarCartasAlTableau() {
        int cartaIndex = 0;

        for (int i = 1; i <= 7; i++) {
            VBox vbox = tableaus.get(i - 1);

            for (int j = 1; j <= i; j++) {
                StackPane carta = cartasGraficas.get(cartaIndex);
                cartaIndex++;

                vbox.getChildren().add(carta);
                carta.setOpacity(1.0);
                VBox.setMargin(carta, new Insets((j - 1) * CARD_OFFSET, 0, 0, 0));

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
        pozo.setMaxSize(CARD_WIDTH, CARD_HEIGHT);
        pozo.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
    }



    public void refreshDraggableCards() {
        for (VBox columna : tableaus) {
            if (!columna.getChildren().isEmpty()) {
                StackPane ultima = (StackPane) columna.getChildren().getLast();
                if (draggableCard(ultima)) {
                    agregarHover(ultima);
                } else {
                    quitarHover(ultima);
                }
            }
        }

    }




    // Restaurar estilo normal de la carta
    private void restaurarEstiloNormal(StackPane carta) {
        StackPane visibleCard = (StackPane) carta.lookup("#VisibleCard");

        if (visibleCard.isVisible()) {
            carta.setStyle("-fx-background-color: white;" +
                    "-fx-background-radius: 10;" +
                    "-fx-border-color: black;" +
                    "-fx-border-radius: 10;" +
                    "-fx-border-width: 1;" +
                    "-fx-effect: null;");
        } else {
            carta.setStyle("-fx-background-color: linear-gradient(to bottom, #1a6fc4, #0d4d8c);" +
                    "-fx-background-radius: 10;" +
                    "-fx-border-color: black;" +
                    "-fx-border-radius: 10;" +
                    "-fx-border-width: 1;" +
                    "-fx-effect: null;");
        }
    }

    // Devolver carta a posición original si el movimiento no es válido
    private void devolverCartaAPosicionOriginal() {
        cartaSeleccionada.setLayoutX(0);
        cartaSeleccionada.setLayoutY(0);

        // Restaurar estilo normal
        restaurarEstiloNormal(cartaSeleccionada);
    }


    private boolean puedeColocarCarta(StackPane carta, VBox contenedorDestino) {
        CartaInglesa cartaObjeto = obtenerCartaObjeto(carta);

        if (tableaus.contains(contenedorDestino)) {
            return puedeColocarEnTableau(cartaObjeto, contenedorDestino);
        } else if (foundations.contains(contenedorDestino)) {
            return puedeColocarEnFoundation(cartaObjeto, contenedorDestino);
        }

        return false;
    }

    // Validación para colocar en tableau
    private boolean puedeColocarEnTableau(CartaInglesa carta, VBox tableau) {
        if (tableau.getChildren().isEmpty()) {
            // Solo reyes pueden ir en tableaus vacíos
            return carta.getValor() == 13; // Rey
        }

        StackPane ultimaCartaPane = (StackPane) tableau.getChildren().get(tableau.getChildren().size() - 1);
        CartaInglesa ultimaCarta = obtenerCartaObjeto(ultimaCartaPane);

        // Debe ser de color alternado y valor descendente
        return !carta.getColor().equals(ultimaCarta.getColor()) &&
                carta.getValor() == ultimaCarta.getValor() - 1;
    }




    // Validación para colocar en foundation
    private boolean puedeColocarEnFoundation(CartaInglesa carta, VBox foundation) {
        System.out.println("Intentando colocar carta: " + carta.toString() + " con valor: " + carta.getValor());

        if (foundation.getChildren().isEmpty()) {
            System.out.println("Foundation vacía, verificando si es As...");
            return carta.getValor() == 1;
        }

        StackPane ultimaCartaPane = (StackPane) foundation.getChildren().get(foundation.getChildren().size() - 1);
        CartaInglesa ultimaCarta = obtenerCartaObjeto(ultimaCartaPane);

        // Debe ser del mismo palo y valor ascendente
        return carta.getPalo().equals(ultimaCarta.getPalo()) &&
                carta.getValor() == ultimaCarta.getValor() + 1;
    }

    // Obtener el objeto CartaInglesa desde un StackPane
    private CartaInglesa obtenerCartaObjeto(StackPane cartaPane) {
        int index = cartasGraficas.indexOf(cartaPane);
        return mazo.getCartas().get(index);
    }




    // Encontrar en qué contenedor se soltó la carta
    private VBox encontrarContenedorDestino(double sceneX, double sceneY) {
        // Verificar tableaus
        for (VBox tableau : tableaus) {
            if (estaDentroDelContenedor(tableau, sceneX, sceneY)) {
                return tableau;
            }
        }

        // Verificar foundations
        for (VBox foundation : foundations) {
            if (estaDentroDelContenedor(foundation, sceneX, sceneY)) {
                return foundation;
            }
        }

        return null;
    }

    // Verificar si las coordenadas están dentro de un contenedor
    private boolean estaDentroDelContenedor(VBox contenedor, double sceneX, double sceneY) {
        return contenedor.getBoundsInParent().contains(
                contenedor.getParent().sceneToLocal(sceneX, sceneY)
        );
    }

    private void agregarHover(StackPane carta) {
        carta.setOnMouseEntered(event -> {
            carta.setStyle("-fx-background-color: #f0f0f0;" +
                    "-fx-background-radius: 10;" +
                    "-fx-border-color: #0066cc;" +
                    "-fx-border-radius: 10;" +
                    "-fx-border-width: 2;" +
                    "-fx-effect: dropshadow(gaussian, #0066cc, 10, 0.5, 0, 0);");
        });

        carta.setOnMouseExited(event -> {
            carta.setStyle("-fx-background-color: white;" +
                    "-fx-background-radius: 10;" +
                    "-fx-border-color: black;" +
                    "-fx-border-radius: 10;" +
                    "-fx-border-width: 1;" +
                    "-fx-effect: null;");
        });
    }

    private void quitarHover(StackPane carta) {
        carta.setOnMouseEntered(null);
        carta.setOnMouseExited(null);

        // También restaurar estilo normal
        carta.setStyle("-fx-background-color: white;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: black;" +
                "-fx-border-radius: 10;" +
                "-fx-border-width: 1;" +
                "-fx-effect: null;");
    }




    public boolean draggableCard(StackPane card) {
        javafx.scene.Parent padre = card.getParent();
        StackPane visibleCard = (StackPane) card.lookup("#VisibleCard");

        // Si está en un VBox (tableau), verificar que esté visible
        if (padre instanceof VBox) {
            VBox columna = (VBox) padre;
            return visibleCard.isVisible() && tableaus.contains(columna);
        }

        // Si está en StackPane (pozo/descarte), verificar que esté visible
        if (padre instanceof StackPane) {
            return visibleCard.isVisible();
        }

        return false;
    }


    private ArrayList<StackPane> obtenerCartasEnSecuencia(StackPane cartaInicial) {
        ArrayList<StackPane> secuencia = new ArrayList<>();
        javafx.scene.Parent padre = cartaInicial.getParent();

        // Solo aplica para tableaus
        if (!(padre instanceof VBox) || !tableaus.contains((VBox) padre)) {
            secuencia.add(cartaInicial);
            return secuencia;
        }

        VBox tableau = (VBox) padre;
        int indiceCarta = tableau.getChildren().indexOf(cartaInicial);

        // Obtener todas las cartas desde la seleccionada hasta el final
        for (int i = indiceCarta; i < tableau.getChildren().size(); i++) {
            StackPane carta = (StackPane) tableau.getChildren().get(i);
            StackPane visibleCard = (StackPane) carta.lookup("#VisibleCard");

            // Solo agregar si está boca arriba
            if (visibleCard.isVisible()) {
                secuencia.add(carta);
            } else {
                break; // Si encontramos una carta boca abajo, parar
            }
        }

        // Validar que la secuencia sea válida (colores alternados, valores descendentes)
        if (secuencia.size() > 1 && !esSecuenciaValida(secuencia)) {
            secuencia.clear();
            secuencia.add(cartaInicial); // Solo la carta inicial si la secuencia no es válida
        }

        return secuencia;
    }

    // NUEVO: Verificar si una secuencia de cartas es válida
    private boolean esSecuenciaValida(ArrayList<StackPane> secuencia) {
        for (int i = 0; i < secuencia.size() - 1; i++) {
            CartaInglesa cartaActual = obtenerCartaObjeto(secuencia.get(i));
            CartaInglesa cartaSiguiente = obtenerCartaObjeto(secuencia.get(i + 1));

            // Verificar colores alternados y valores descendentes
            if (cartaActual.getColor().equals(cartaSiguiente.getColor()) ||
                    cartaActual.getValor() != cartaSiguiente.getValor() + 1) {
                return false;
            }
        }
        return true;
    }

    // Configurar eventos de mouse para una carta específica - CORREGIDO
    private void configurarEventosCarta(StackPane carta) {
        carta.setOnMousePressed(event -> {
            if (draggableCard(carta)) {
                cartaSeleccionada = carta;
                contenedorOrigen = carta.getParent();

                // Obtener todas las cartas que se pueden mover en secuencia
                cartasSeleccionadas = obtenerCartasEnSecuencia(carta);

                // Guardar offset para un arrastre suave
                offsetX = event.getSceneX() - carta.getLayoutX();
                offsetY = event.getSceneY() - carta.getLayoutY();

                // Efecto visual para todas las cartas seleccionadas
                for (StackPane cartaEnSecuencia : cartasSeleccionadas) {
                    cartaEnSecuencia.setStyle(cartaEnSecuencia.getStyle() +
                            "-fx-effect: dropshadow(gaussian, yellow, 15, 0.8, 0, 0);");
                }

                event.consume();
            }
        });

        carta.setOnMouseDragged(event -> {
            if (cartaSeleccionada == carta && !cartasSeleccionadas.isEmpty()) {
                // Calcular el desplazamiento
                double deltaX = event.getSceneX() - offsetX - carta.getLayoutX();
                double deltaY = event.getSceneY() - offsetY - carta.getLayoutY();

                // Mover todas las cartas en la secuencia
                for (StackPane cartaEnSecuencia : cartasSeleccionadas) {
                    cartaEnSecuencia.setLayoutX(cartaEnSecuencia.getLayoutX() + deltaX);
                    cartaEnSecuencia.setLayoutY(cartaEnSecuencia.getLayoutY() + deltaY);
                }

                // Actualizar offset
                offsetX = event.getSceneX() - carta.getLayoutX();
                offsetY = event.getSceneY() - carta.getLayoutY();

                event.consume();
            }
        });

        carta.setOnMouseReleased(event -> {
            if (cartaSeleccionada == carta) {
                manejarSoltarCarta(event);
                cartaSeleccionada = null;
                cartasSeleccionadas.clear();
                contenedorOrigen = null;
                event.consume();
            }
        });
    }

    private void manejarSoltarCarta(javafx.scene.input.MouseEvent event) {
        VBox contenedorDestino = encontrarContenedorDestino(event.getSceneX(), event.getSceneY());

        if (contenedorDestino != null && puedeColocarSecuencia(cartasSeleccionadas, contenedorDestino)) {
            // Mover todas las cartas al nuevo contenedor
            moverSecuenciaDeCartas(cartasSeleccionadas, contenedorOrigen, contenedorDestino);

            // Verificar si se debe voltear una nueva carta
            verificarYVoltearCarta(contenedorOrigen);

            // Actualizar cartas arrastrables
            refreshDraggableCards();

            // Verificar si ganó el juego
            verificarYMostrarVictoria();
        } else {
            // Devolver todas las cartas a su posición original
            devolverCartasAPosicionOriginal();
        }
    }

    // NUEVO: Validar si se puede colocar una secuencia de cartas
    private boolean puedeColocarSecuencia(ArrayList<StackPane> secuencia, VBox contenedorDestino) {
        if (secuencia.isEmpty()) return false;

        // Solo validar con la primera carta de la secuencia
        StackPane primeraCarta = secuencia.get(0);
        CartaInglesa cartaObjeto = obtenerCartaObjeto(primeraCarta);

        if (tableaus.contains(contenedorDestino)) {
            return puedeColocarEnTableau(cartaObjeto, contenedorDestino);
        } else if (foundations.contains(contenedorDestino)) {
            // Las foundations solo aceptan una carta a la vez
            return secuencia.size() == 1 && puedeColocarEnFoundation(cartaObjeto, contenedorDestino);
        }

        return false;
    }

    // Mover una secuencia completa de cartas
    private void moverSecuenciaDeCartas(ArrayList<StackPane> secuencia, javafx.scene.Parent origen, VBox destino) {
        // Remover todas las cartas del contenedor origen
        for (StackPane carta : secuencia) {
            if (origen instanceof VBox) {
                ((VBox) origen).getChildren().remove(carta);
            } else if (origen instanceof StackPane) {
                ((StackPane) origen).getChildren().remove(carta);
            }

            // Resetear posición y transformaciones
            carta.setLayoutX(0);
            carta.setLayoutY(0);
        }

        // Agregar todas las cartas al contenedor destino
        for (int i = 0; i < secuencia.size(); i++) {
            StackPane carta = secuencia.get(i);
            destino.getChildren().add(carta);

            // Configurar márgenes apropiados
            if (tableaus.contains(destino)) {
                int posicion = destino.getChildren().size() - 1;
                VBox.setMargin(carta, new Insets(posicion * CARD_OFFSET, 0, 0, 0));
            } else {
                VBox.setMargin(carta, Insets.EMPTY);
            }

            // Restaurar estilo normal y reconfigurar eventos
            restaurarEstiloNormal(carta);
            configurarEventosCarta(carta);
        }
    }

    // Devolver múltiples cartas a posición original
    private void devolverCartasAPosicionOriginal() {
        for (StackPane carta : cartasSeleccionadas) {
            carta.setLayoutX(0);
            carta.setLayoutY(0);
            restaurarEstiloNormal(carta);
        }
    }




    private void verificarYVoltearCarta(javafx.scene.Parent contenedor) {
        if (contenedor instanceof VBox && tableaus.contains((VBox) contenedor)) {
            VBox tableau = (VBox) contenedor;
            if (!tableau.getChildren().isEmpty()) {
                StackPane ultimaCarta = (StackPane) tableau.getChildren().get(tableau.getChildren().size() - 1);
                StackPane visibleCard = (StackPane) ultimaCarta.lookup("#VisibleCard");

                if (!visibleCard.isVisible()) {
                    try {
                        voltearCarta(ultimaCarta);
                        agregarHover(ultimaCarta);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    @FXML
    private void manejarClicPozo() {
        if (!pozo.getChildren().isEmpty()) {
            StackPane carta = (StackPane) pozo.getChildren().get(pozo.getChildren().size() - 1);

            // Remover del pozo
            pozo.getChildren().remove(carta);

            // Agregar a zona de descarte
            zonaDescarte.getChildren().clear();
            zonaDescarte.getChildren().add(carta);

            try {
                voltearCarta(carta); // Voltear boca arriba
                configurarEventosCarta(carta); // Reconfigurar eventos
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Actualizar hover del pozo si quedan cartas
            if (!pozo.getChildren().isEmpty()) {
                StackPane nuevaUltima = (StackPane) pozo.getChildren().get(pozo.getChildren().size() - 1);
                agregarHoverPozo(nuevaUltima);
            }
        } else {
            // Si el pozo está vacío, reciclar desde descarte
            reciclarDescarte();
        }
    }

    // Reciclar cartas del descarte al pozo - NUEVO
    private void reciclarDescarte() {
        if (!zonaDescarte.getChildren().isEmpty()) {
            // Mover la carta del descarte de vuelta al pozo
            StackPane carta = (StackPane) zonaDescarte.getChildren().get(0);
            zonaDescarte.getChildren().remove(carta);

            try {
                voltearCarta(carta); // Voltear boca abajo
            } catch (IOException e) {
                e.printStackTrace();
            }

            pozo.getChildren().add(carta);
            agregarHoverPozo(carta);
        }
    }

    // Hover específico para cartas del pozo - NUEVO
    private void agregarHoverPozo(StackPane carta) {
        carta.setOnMouseEntered(event -> {
            carta.setStyle("-fx-background-color: #1a7cd4;" +
                    "-fx-background-radius: 10;" +
                    "-fx-border-color: #0066cc;" +
                    "-fx-border-radius: 10;" +
                    "-fx-border-width: 2;" +
                    "-fx-effect: dropshadow(gaussian, #0066cc, 10, 0.5, 0, 0);");
        });

        carta.setOnMouseExited(event -> {
            carta.setStyle("-fx-background-color: linear-gradient(to bottom, #1a6fc4, #0d4d8c);" +
                    "-fx-background-radius: 10;" +
                    "-fx-border-color: black;" +
                    "-fx-border-radius: 10;" +
                    "-fx-border-width: 1;");
        });
    }


    public boolean verificarVictoria() {
        // El juego se gana cuando todas las foundations tienen 13 cartas (As a Rey)
        for (VBox foundation : foundations) {
            if (foundation.getChildren().size() != 13) {
                return false;
            }
        }
        return true;
    }


    private void mostrarMensajeVictoria() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("¡Felicitaciones!");
        alert.setHeaderText("¡Has ganado!");
        alert.setContentText("¡Completaste el Solitario exitosamente!\n¿Quieres jugar otra vez?");

        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                reiniciarJuego();
            }
        });
    }


    private void verificarYMostrarVictoria() {
        if (verificarVictoria()) {
            mostrarMensajeVictoria();
        }
    }


    private void reiniciarJuego() {
        try {
            // Limpiar todos los contenedores
            for (VBox tableau : tableaus) {
                tableau.getChildren().clear();
            }
            for (VBox foundation : foundations) {
                foundation.getChildren().clear();
            }
            pozo.getChildren().clear();
            zonaDescarte.getChildren().clear();

            // Reinicializar el juego
            mazo = new Mazo();
            cartasGraficas.clear();

            generarCartas();
            agregarCartasAlTableau();
            agregarCartasAlPozo();
            configurarDragAndDrop();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void configurarDragAndDrop() {
        // Configurar para todas las cartas en tableaus
        for (VBox tableau : tableaus) {
            for (Node node : tableau.getChildren()) {
                if (node instanceof StackPane) {
                    StackPane carta = (StackPane) node;
                    configurarEventosCarta(carta);
                }
            }
        }

        // Configurar para cartas en el pozo (solo la última)
        if (!pozo.getChildren().isEmpty()) {
            StackPane ultimaCarta = (StackPane) pozo.getChildren().get(pozo.getChildren().size() - 1);
            configurarEventosCarta(ultimaCarta);
        }
    }

}
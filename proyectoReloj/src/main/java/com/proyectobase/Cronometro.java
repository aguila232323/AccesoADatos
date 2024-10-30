package com.proyectobase; //Modificar al package correcto

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Cronometro extends Application {
    private TextField inputField;
    private ProgressBar progressBar;
    private Label tiempoLabel;
    private Button iniciarButton;
    private Button cancelarButton;
    private Button temaButton;
    private int tiempoTotal;
    private int tiempoActual;
    private boolean contando;
    private boolean temaOscuro = false; // Por defecto, el tema es claro

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Contador de Tiempo");
        VBox root = createLayout();
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createLayout() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Label instruccionLabel = new Label("Introduce el tiempo en segundos:");
        inputField = new TextField();
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(200);
        tiempoLabel = new Label("Tiempo: 00:00:00");
        iniciarButton = new Button("Iniciar");
        cancelarButton = new Button("Cancelar");
        temaButton = new Button("Tema Oscuro");
        cancelarButton.setDisable(true);

        iniciarButton.setOnAction(e -> iniciarContador());
        cancelarButton.setOnAction(e -> cancelarContador());
        temaButton.setOnAction(e -> cambiarTema(root));

        Button guardarButton = new Button("Guardar Tiempo");
        Button cargarButton = new Button("Cargar Tiempo");
        guardarButton.setOnAction(e -> guardarTiempo());
        cargarButton.setOnAction(e -> cargarTiempo());

        root.getChildren().addAll(instruccionLabel, inputField, progressBar, tiempoLabel,
                iniciarButton, cancelarButton, temaButton, guardarButton, cargarButton);
        return root;
    }

    private void iniciarContador() {
        try {
            String input = inputField.getText().trim();
            if (input.isEmpty()) {
                throw new NumberFormatException("Input is empty");
            }

            tiempoTotal = Integer.parseInt(input);
            if (tiempoTotal <= 0) {
                throw new NumberFormatException("Number must be greater than zero");
            }

            tiempoActual = 0;
            contando = true;
            progressBar.setProgress(0);
            iniciarButton.setDisable(true);
            cancelarButton.setDisable(false);
            inputField.setDisable(true);

            new Thread(() -> {
                while (contando && tiempoActual < tiempoTotal) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        break; // Exit thread if interrupted
                    }
                    tiempoActual++;
                    Platform.runLater(this::actualizarUI);
                }
                if (tiempoActual >= tiempoTotal) {
                    mostrarAlerta("¡El tiempo ha finalizado!", "Tiempo completado");
                    reiniciarUI();
                }
            }).start();
        } catch (NumberFormatException ex) {
            mostrarAlerta("Por favor, introduce un número válido mayor que cero.", "Error");
        }
    }

    private void cancelarContador() {
        contando = false;
        reiniciarUI();
    }

    private void actualizarUI() {
        tiempoLabel.setText(formatoTiempo(tiempoActual));
        double progreso = (double) tiempoActual / tiempoTotal;
        progressBar.setProgress(progreso);
    }

    private void reiniciarUI() {
        iniciarButton.setDisable(false);
        cancelarButton.setDisable(true);
        inputField.setDisable(false);
        progressBar.setProgress(0);
        tiempoLabel.setText("Tiempo: 00:00:00");
    }

    private void mostrarAlerta(String mensaje, String titulo) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(titulo);
            alert.setHeaderText(null);
            alert.setContentText(mensaje);
            alert.showAndWait();
        });
    }

    private String formatoTiempo(int totalSegundos) {
        int horas = totalSegundos / 3600;
        int minutos = (totalSegundos % 3600) / 60;
        int segundos = totalSegundos % 60;
        return String.format("Tiempo: %02d:%02d:%02d", horas, minutos, segundos);
    }

    private void cambiarTema(VBox root) {
        temaOscuro = !temaOscuro; // Alternar tema
        if (temaOscuro) {
            root.setStyle("-fx-background-color: #2e2e2e; -fx-text-fill: white;");
            temaButton.setText("Tema Claro");
        } else {
            root.setStyle("-fx-background-color: white; -fx-text-fill: black;");
            temaButton.setText("Tema Oscuro");
        }
    }

    private void guardarTiempo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tiempos.txt", true))) {
            writer.write(inputField.getText().trim());
            writer.newLine();
            mostrarAlerta("Tiempo guardado exitosamente.", "Guardar Tiempo");
        } catch (IOException e) {
            mostrarAlerta("Error al guardar el tiempo.", "Error");
        }
    }

    private void cargarTiempo() {
        List<String> tiempos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("tiempos.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tiempos.add(line);
            }
            if (!tiempos.isEmpty()) {
                String tiempoSeleccionado = tiempos.get(0); // Se puede mejorar para seleccionar diferentes tiempos
                inputField.setText(tiempoSeleccionado);
                mostrarAlerta("Tiempo cargado: " + tiempoSeleccionado, "Cargar Tiempo");
            } else {
                mostrarAlerta("No hay tiempos guardados.", "Cargar Tiempo");
            }
        } catch (IOException e) {
            mostrarAlerta("Error al cargar los tiempos.", "Error");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

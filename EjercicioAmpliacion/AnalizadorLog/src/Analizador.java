
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Analizador {

    private Map<String, Long> nivelContador = new HashMap<>();
    private Map<String, Long> mensajeErrorContador = new HashMap<>();

    public void analizarLog(String rutaLog, String rutaInforme) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(rutaLog))) {
            reader.lines().forEach(linea -> procesarLinea(linea));
            generarInforme(rutaInforme);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de log: " + e.getMessage());
        }
    }

    private void procesarLinea(String linea) {
        String[] partes = linea.split(" ", 3);
        if (partes.length < 3) {
            return;
        }

        String nivel = partes[2].split(" ")[0];
        String mensaje = partes.length > 2 ? partes[2] : "";

        // Contar niveles de log
        nivelContador.merge(nivel, 1L, Long::sum);

        // Contar mensajes de error
        if (nivel.equals("ERROR")) {
            mensajeErrorContador.merge(mensaje, 1L, Long::sum);
        }
    }

    private void generarInforme(String rutaInforme) {
        List<String> lineasInforme = new ArrayList<>();

        // Agregar conteos de niveles de log
        lineasInforme.add("Estadísticas de Niveles de Log:");
        nivelContador.forEach((nivel, contador) -> lineasInforme.add(nivel + ": " + contador));


        List<Map.Entry<String, Long>> erroresComunes = mensajeErrorContador.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toList());

        lineasInforme.add("\nTop 5 Mensajes de Error Más Comunes:");
        erroresComunes.forEach(entry -> lineasInforme.add(entry.getKey() + ": " + entry.getValue()));


        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(rutaInforme))) {
            for (String linea : lineasInforme) {
                writer.write(linea);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al escribir el informe: " + e.getMessage());
        }
    }
}


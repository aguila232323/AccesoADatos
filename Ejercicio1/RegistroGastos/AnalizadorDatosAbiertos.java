import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class AnalizadorDatosAbiertos {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce la ruta del archivo a analizar:");
        String rutaArchivo = scanner.nextLine();

        if (rutaArchivo.endsWith(".csv")) {
            List<String[]> datosCSV = parsearCSV(rutaArchivo);
            mostrarResumenCSV(datosCSV);
        } else if (rutaArchivo.endsWith(".json")) {
            JsonObject datosJSON = parsearJSON(rutaArchivo);
            mostrarResumenJSON(datosJSON);
        } else if (rutaArchivo.endsWith(".xml")) {
            Document datosXML = parsearXML(rutaArchivo);
            mostrarResumenXML(datosXML);
        } else {
            System.out.println("Formato de archivo no soportado.");
        }
    }

    // 1. Parsear CSV
    public static List<String[]> parsearCSV(String rutaArchivo) {
        List<String[]> registros = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(",");
                registros.add(valores);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo CSV: " + e.getMessage());
        }
        return registros;
    }

    // 2. Parsear JSON
    public static JsonObject parsearJSON(String rutaArchivo) {
        JsonObject jsonObject = null;
        try (FileReader reader = new FileReader(rutaArchivo)) {
            jsonObject = new Gson().fromJson(reader, JsonObject.class);
        } catch (Exception e) {
            System.out.println("Error al leer el archivo JSON: " + e.getMessage());
        }
        return jsonObject;
    }

    // 3. Parsear XML
    public static Document parsearXML(String rutaArchivo) {
        Document doc = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(rutaArchivo);
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            System.out.println("Error al leer el archivo XML: " + e.getMessage());
        }
        return doc;
    }

    // Mostrar Resumen CSV
    public static void mostrarResumenCSV(List<String[]> datos) {
        if (datos.isEmpty()) {
            System.out.println("No se encontraron datos.");
            return;
        }
        System.out.println("Resumen del archivo CSV:");
        System.out.println("Número total de filas: " + datos.size());
        System.out.println("Número de columnas: " + datos.get(0).length);

        // Mostrar primeros 5 registros
        System.out.println("\nPrimeros 5 registros:");
        for (int i = 0; i < Math.min(5, datos.size()); i++) {
            System.out.println(String.join(" | ", datos.get(i)));
        }

        // Análisis básico para datos numéricos
        calcularEstadisticasCSV(datos);
    }

    // Mostrar Resumen JSON
    public static void mostrarResumenJSON(JsonObject datos) {
        if (datos == null) {
            System.out.println("No se encontraron datos.");
            return;
        }

        System.out.println("Resumen del archivo JSON:");
        Set<String> campos = datos.keySet();
        System.out.println("Campos presentes: " + campos);

        // Si hay un array de objetos, se puede mostrar los primeros 5
        if (datos.get("records") != null && datos.get("records").isJsonArray()) {
            JsonArray registros = datos.getAsJsonArray("records");
            System.out.println("\nPrimeros 5 registros:");
            for (int i = 0; i < Math.min(5, registros.size()); i++) {
                System.out.println(registros.get(i));
            }
        }

        // Para JSON, el análisis numérico puede variar según la estructura
    }

    // Mostrar Resumen XML
    public static void mostrarResumenXML(Document datos) {
        if (datos == null) {
            System.out.println("No se encontraron datos.");
            return;
        }

        System.out.println("Resumen del archivo XML:");
        NodeList nodos = datos.getDocumentElement().getChildNodes();
        System.out.println("Número total de nodos: " + nodos.getLength());

        // Mostrar los primeros 5 nodos
        System.out.println("\nPrimeros 5 registros:");
        for (int i = 0; i < Math.min(5, nodos.getLength()); i++) {
            System.out.println(nodos.item(i).getTextContent());
        }


    }

    public static void calcularEstadisticasCSV(List<String[]> datos) {
        int numFilas = datos.size();
        int numColumnas = datos.get(0).length;
        List<Double> numeros = new ArrayList<>();

        // Solo buscando datos numéricos en la última columna (por ejemplo)
        for (int i = 1; i < numFilas; i++) {
            try {
                double valor = Double.parseDouble(datos.get(i)[numColumnas - 1]);
                numeros.add(valor);
            } catch (NumberFormatException e) {
                // Ignorar si no es numérico
            }
        }

        if (!numeros.isEmpty()) {
            double min = numeros.stream().min(Double::compare).orElse(0.0);
            double max = numeros.stream().max(Double::compare).orElse(0.0);
            double sum = numeros.stream().mapToDouble(Double::doubleValue).sum();
            double promedio = sum / numeros.size();

            System.out.println("\nEstadísticas para la última columna numérica:");
            System.out.println("Mínimo: " + min);
            System.out.println("Máximo: " + max);
            System.out.println("Promedio: " + promedio);
        } else {
            System.out.println("\nNo se encontraron datos numéricos para el análisis.");
        }
    }
}

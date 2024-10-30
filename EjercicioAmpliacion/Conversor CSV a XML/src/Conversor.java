
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Conversor{

    public void convertirCSVaXML(String csvFile, String xmlFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Leer la primera línea para obtener los encabezados
            String encabezado = br.readLine();
            if (encabezado == null || encabezado.isEmpty()) {
                throw new Exception("El archivo CSV está vacío o no tiene encabezados.");
            }

            String[] headers = encabezado.split(",");
            validarEncabezados(headers);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root = document.createElement("Estudiantes");
            document.appendChild(root);

            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length != headers.length) {
                    throw new Exception("El número de columnas no coincide en la línea: " + linea);
                }

                Element estudiante = document.createElement("Estudiante");
                root.appendChild(estudiante);

                for (int i = 0; i < headers.length; i++) {
                    Element elemento = document.createElement(headers[i]);
                    elemento.appendChild(document.createTextNode(datos[i]));
                    estudiante.appendChild(elemento);
                }
            }

            // Escribir el documento XML a un archivo
            try (FileWriter writer = new FileWriter(xmlFile)) {
                writer.write(convertDocumentToString(document));
            }
        } catch (Exception e) {
            System.err.println("Error durante la conversión: " + e.getMessage());
        }
    }

    private void validarEncabezados(String[] headers) throws Exception {
        String[] expectedHeaders = {"ID", "Nombre", "Apellido", "Edad", "Curso"};
        if (headers.length != expectedHeaders.length) {
            throw new Exception("El CSV debe contener 5 columnas: ID, Nombre, Apellido, Edad, Curso.");
        }
        for (int i = 0; i < headers.length; i++) {
            if (!headers[i].equals(expectedHeaders[i])) {
                throw new Exception("El encabezado " + headers[i] + " no es válido.");
            }
        }
    }

    private String convertDocumentToString(Document doc) {
        try {
            javax.xml.transform.TransformerFactory transformerFactory = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();
            javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(doc);
            java.io.StringWriter writer = new java.io.StringWriter();
            javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(writer);
            transformer.transform(source, result);
            return writer.toString();
        } catch (Exception e) {
            System.err.println("Error al convertir Document a String: " + e.getMessage());
            return null;
        }
    }
}

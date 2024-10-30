

public class Programa {
    public static void main(String[] args) {
        Conversor conversor = new Conversor();
        String csvFile = "estudiantes.csv";
        String xmlFile = "estudiantes.xml";
        conversor.convertirCSVaXML(csvFile, xmlFile);
        System.out.println("Conversión completada. Archivo XML generado: " + xmlFile);
    }
}


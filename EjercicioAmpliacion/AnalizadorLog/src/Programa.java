

public class Programa {
    public static void main(String[] args) {
        Analizador analizador = new Analizador();
        String rutaLog = "logs.txt";
        String rutaInforme = "informe_estadisticas.txt";
        analizador.analizarLog(rutaLog, rutaInforme);
        System.out.println("Análisis completado. Informe generado: " + rutaInforme);
    }
}


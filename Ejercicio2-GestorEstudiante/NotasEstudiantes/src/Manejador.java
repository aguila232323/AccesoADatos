

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Manejador{
    private static final String ARCHIVO = "C:\\Users\\Prados\\Downloads\\Estudiantes.txt";

    // Añadir un estudiante al archivo
    public void añadirEstudiante(Estudiante estudiante) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO, true))) {
            writer.write(estudiante.getNombre() + "," + estudiante.getNota());
            writer.newLine();
            System.out.println("Estudiante añadido: " + estudiante.getNombre());
        } catch (IOException e) {
            System.out.println("Error al añadir estudiante: " + e.getMessage());
        }
    }


    public void mostrarEstudiantes() {
        List<Estudiante> estudiantes = leerEstudiantes();
        if (estudiantes.isEmpty()) {
            System.out.println("No hay estudiantes para mostrar.");
        } else {
            for (Estudiante estudiante : estudiantes) {
                System.out.println("Nombre: " + estudiante.getNombre() + ", Nota: " + estudiante.getNota());
            }
        }
    }


    public void buscarEstudiante(String nombre) {
        List<Estudiante> estudiantes = leerEstudiantes();
        boolean encontrado = false;
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println("Estudiante encontrado: Nombre: " + estudiante.getNombre() + ", Nota: " + estudiante.getNota());
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            System.out.println("Estudiante con nombre '" + nombre + "' no encontrado.");
        }
    }


    public void calcularMedia() {
        List<Estudiante> estudiantes = leerEstudiantes();
        if (estudiantes.isEmpty()) {
            System.out.println("No hay estudiantes para calcular la media.");
            return;
        }
        double suma = 0;
        for (Estudiante estudiante : estudiantes) {
            suma += estudiante.getNota();
        }
        double media = suma / estudiantes.size();
        System.out.println("La nota media de los estudiantes es: " + media);
    }


    private List<Estudiante> leerEstudiantes() {
        List<Estudiante> estudiantes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 2) {
                    String nombre = datos[0].trim();
                    double nota = Double.parseDouble(datos[1].trim());
                    estudiantes.add(new Estudiante(nombre, nota));
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer estudiantes: " + e.getMessage());
        }
        return estudiantes;
    }
}


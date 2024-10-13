import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class RegistroGastos {
    private static final String ARCHIVO_GASTOS = "C:\\Users\\Prados\\Downloads\\2º DAM\\Acceso a datos\\gastos.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("\n--- Registro de Gastos Personales ---");
            System.out.println("1. Añadir gasto");
            System.out.println("2. Ver todos los gastos");
            System.out.println("3. Calcular total de gastos");
            System.out.println("4. Ver gastos por categoría");
            System.out.println("5. Editar gasto");
            System.out.println("6. Eliminar gasto");
            System.out.println("7. Buscar por fecha");
            System.out.println("8. Exportar gastos a CSV");
            System.out.println("9. Estadísticas");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea
            switch (opcion) {
                case 1: anadirGasto(scanner); break;
                case 2: verGastos(); break;
                case 3: calcularTotalGastos(); break;
                case 4: verGastosPorCategoria(scanner); break;
                case 5: editarGasto(scanner); break;
                case 6: eliminarGasto(scanner); break;
                case 7: buscarGastosPorFecha(scanner); break;
                case 8: exportarGastosACSV(); break;
                case 9: mostrarEstadisticas(); break;
                case 0: System.out.println("¡Hasta luego!"); break;
                default: System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
        scanner.close();
    }
    private static void anadirGasto(Scanner scanner) {
        System.out.print("Introduce la fecha (DD/MM/YYYY): ");
        String fecha = scanner.nextLine();
        System.out.print("Introduce la categoría: ");
        String categoria = scanner.nextLine();
        System.out.print("Introduce la descripción: ");
        String descripcion = scanner.nextLine();
        System.out.print("Introduce la cantidad: ");
        double cantidad = scanner.nextDouble();
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_GASTOS, true))) {
            writer.println(fecha + "," + categoria + "," + descripcion + "," + cantidad);
            System.out.println("Gasto registrado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al registrar el gasto: " + e.getMessage());
        }
    }

    private static void verGastos() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_GASTOS))) {
            String linea;
            System.out.println("\n--- Todos los Gastos ---");
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                System.out.println("Fecha: " + partes[0] + ", Categoría: " + partes[1] +
                        ", Descripción: " + partes[2] + ", cantidad: $" + partes[3]);
            }
        } catch (IOException e) {
            System.out.println("Error al leer los gastos: " + e.getMessage());
        }
    }

    private static void calcularTotalGastos() {
        double total = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_GASTOS))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                total += Double.parseDouble(partes[3]);
            }
            System.out.println("Total de gastos: $" + total);
        } catch (IOException e) {
            System.out.println("Error al calcular el total de gastos: " + e.getMessage());
        }
    }

    private static void verGastosPorCategoria(Scanner scanner) {
        System.out.print("Introduce la categoría a buscar: ");
        String categoriaBuscada = scanner.nextLine().toLowerCase();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_GASTOS))) {
            String linea;
            boolean encontrado = false;
            System.out.println("\n--- Gastos de la categoría '" + categoriaBuscada + "' ---");
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes[1].toLowerCase().equals(categoriaBuscada)) {
                    System.out.println("Fecha: " + partes[0] + ", Descripción: " + partes[2] + ", cantidad: $" + partes[3]);
                    encontrado = true;
                }
            }
            if (!encontrado) {
                System.out.println("No se encontraron gastos en esta categoría.");
            }
        } catch (IOException e) {
            System.out.println("Error al buscar gastos por categoría: " + e.getMessage());
        }
    }

    // Función 5: Editar gasto
    private static void editarGasto(Scanner scanner) {
        verGastos(); // Mostrar los gastos para que el usuario elija cuál editar
        System.out.print("Introduce el número de línea del gasto que quieres editar: ");
        int numLinea = scanner.nextInt();
        scanner.nextLine(); // Consumir salto de línea

        List<String> gastos = leerGastos();
        if (numLinea < 1 || numLinea > gastos.size()) {
            System.out.println("Número de línea inválido.");
            return;
        }

        System.out.print("Introduce la nueva fecha (DD/MM/YYYY): ");
        String nuevaFecha = scanner.nextLine();
        System.out.print("Introduce la nueva categoría: ");
        String nuevaCategoria = scanner.nextLine();
        System.out.print("Introduce la nueva descripción: ");
        String nuevaDescripcion = scanner.nextLine();
        System.out.print("Introduce la nueva cantidad: ");
        double nuevaCantidad = scanner.nextDouble();

        gastos.set(numLinea - 1, nuevaFecha + "," + nuevaCategoria + "," + nuevaDescripcion + "," + nuevaCantidad);
        guardarGastos(gastos);
        System.out.println("Gasto editado correctamente.");
    }

    // Función 6: Eliminar gasto
    private static void eliminarGasto(Scanner scanner) {
        verGastos();
        System.out.print("Introduce el número de línea del gasto que quieres eliminar: ");
        int numLinea = scanner.nextInt();
        scanner.nextLine(); // Consumir salto de línea

        List<String> gastos = leerGastos();
        if (numLinea < 1 || numLinea > gastos.size()) {
            System.out.println("Número de línea inválido.");
            return;
        }

        gastos.remove(numLinea - 1);
        guardarGastos(gastos);

        System.out.println("Gasto eliminado correctamente.");
    }

    // Función 7: Buscar gastos por fecha
    private static void buscarGastosPorFecha(Scanner scanner) {
        System.out.print("Introduce la fecha inicial (DD/MM/YYYY): ");
        String fechaInicio = scanner.nextLine();
        System.out.print("Introduce la fecha final (DD/MM/YYYY): ");
        String fechaFin = scanner.nextLine();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_GASTOS))) {
            String linea;
            System.out.println("\n--- Gastos entre " + fechaInicio + " y " + fechaFin + " ---");
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                Date fechaGasto = sdf.parse(partes[0]);
                Date inicio = sdf.parse(fechaInicio);
                Date fin = sdf.parse(fechaFin);
                if (fechaGasto.compareTo(inicio) >= 0 && fechaGasto.compareTo(fin) <= 0) {
                    System.out.println("Fecha: " + partes[0] + ", Categoría: " + partes[1] + ", Descripción: " + partes[2] + ", cantidad: $" + partes[3]);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al buscar gastos por fecha: " + e.getMessage());
        }
    }

    // Función 8: Exportar a CSV
    private static void exportarGastosACSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_GASTOS));
             PrintWriter writer = new PrintWriter(new FileWriter("gastos_exportados.csv"))) {
            String linea;
            writer.println("Fecha,Categoría,Descripción,Cantidad");
            while ((linea = reader.readLine()) != null) {
                writer.println(linea);
            }
            System.out.println("Gastos exportados a gastos_exportados.csv");
        } catch (IOException e) {
            System.out.println("Error al exportar los gastos: " + e.getMessage());
        }
    }

    // Función 9: Mostrar estadísticas
    private static void mostrarEstadisticas() {
        double total = 0, max = Double.MIN_VALUE, min = Double.MAX_VALUE, cantidad;
        int cuenta = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_GASTOS))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                cantidad = Double.parseDouble(partes[3]);
                total += cantidad;
                cuenta++;
                if (cantidad > max) max = cantidad;
                if (cantidad < min) min = cantidad;
            }
            double promedio = (cuenta == 0) ? 0 : total / cuenta;
            System.out.println("Total de gastos: $" + total);
            System.out.println("Gasto promedio: $" + promedio);
            System.out.println("Gasto máximo: $" + max);
            System.out.println("Gasto mínimo: $" + min);
        } catch (IOException e) {
            System.out.println("Error al calcular estadísticas: " + e.getMessage());
        }
    }

    // Funciones auxiliares
    private static List<String> leerGastos() {
        List<String> gastos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_GASTOS))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                gastos.add(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer los gastos: " + e.getMessage());
        }
        return gastos;
    }

    private static void guardarGastos(List<String> gastos) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_GASTOS))) {
            for (String gasto : gastos) {
                writer.println(gasto);
            }
        } catch (IOException e) {
            System.out.println("Error al guardar los gastos: " + e.getMessage());
        }
    }
}

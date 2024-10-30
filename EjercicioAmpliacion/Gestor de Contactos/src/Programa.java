
import java.util.List;
import java.util.Scanner;

public class Programa {
    public static void main(String[] args) {
        GestorContactos gestor = new GestorContactos();
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("1. Añadir contacto");
            System.out.println("2. Listar contactos");
            System.out.println("3. Buscar contacto");
            System.out.println("4. Eliminar contacto");
            System.out.println("5. Salir");
            System.out.print("Elige una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcion) {
                case 1:
                    System.out.print("Nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Teléfono: ");
                    String telefono = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    contacto nuevoContacto = new contacto(nombre, telefono, email);
                    gestor.añadirContacto(nuevoContacto);
                    break;
                case 2:
                    List<contacto> contactos = gestor.listarContactos();
                    for (contacto c : contactos) {
                        System.out.println(c);
                    }
                    break;
                case 3:
                    System.out.print("Nombre del contacto a buscar: ");
                    String nombreBuscar = scanner.nextLine();
                    contacto encontrado = gestor.buscarContacto(nombreBuscar);
                    if (encontrado != null) {
                        System.out.println("Contacto encontrado: " + encontrado);
                    } else {
                        System.out.println("Contacto no encontrado.");
                    }
                    break;
                case 4:
                    System.out.print("Nombre del contacto a eliminar: ");
                    String nombreEliminar = scanner.nextLine();
                    gestor.eliminarContacto(nombreEliminar);
                    System.out.println("Contacto eliminado.");
                    break;
                case 5:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida. Intenta de nuevo.");
            }
        } while (opcion != 5);

        scanner.close();
    }
}

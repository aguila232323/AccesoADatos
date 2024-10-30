

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorContactos {
    private static final String FILE_PATH = "contactos.txt";

    public void añadirContacto(contacto contacto) {
        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
            writer.write(contacto.getNombre() + "," + contacto.getTelefono() + "," + contacto.getEmail() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<contacto> listarContactos() {
        List<contacto> contactos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    contactos.add(new contacto(data[0], data[1], data[2]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contactos;
    }

    public contacto buscarContacto(String nombre) {
        for (contacto contacto : listarContactos()) {
            if (contacto.getNombre().equalsIgnoreCase(nombre)) {
                return contacto;
            }
        }
        return null;
    }

    public void eliminarContacto(String nombre) {
        List<contacto> contactos = listarContactos();
        contactos.removeIf(contacto -> contacto.getNombre().equalsIgnoreCase(nombre));

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            for (contacto contacto : contactos) {
                writer.write(contacto.getNombre() + "," + contacto.getTelefono() + "," + contacto.getEmail() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


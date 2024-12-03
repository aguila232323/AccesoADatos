/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AcessoADatos;

/**
 *
 * @author Prados
 */
import java.io.*;
import java.util.Properties;

public class Configuracion {
    private static final String CONFIG_FILE = "cong.properties";
    private Properties properties;

    public Configuracion() {
        properties = new Properties();
        cargarConfiguracion();
    }

    private void cargarConfiguracion() {
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            System.out.println("No se pudo cargar el archivo de configuración.");
        }
    }

    public String obtenerRutaBinario() {
        return properties.getProperty("chero_binario", "empleados.dat");
    }

    public String obtenerRutaXML() {
        return properties.getProperty("chero_xml", "empleados.xml");
    }

    public String obtenerRutaJSON() {
        return properties.getProperty("chero_json", "empleados.json");
    }

    public int obtenerUltimoIdEmpleado() {
        return Integer.parseInt(properties.getProperty("id_empleado", "0"));
    }

    public void actualizarUltimoIdEmpleado(int id) {
        properties.setProperty("id_empleado", String.valueOf(id));
        guardarConfiguracion();
    }

    private void guardarConfiguracion() {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            properties.store(output, null);
        } catch (IOException e) {
            System.out.println("Error al guardar la configuración.");
        }
    }
}


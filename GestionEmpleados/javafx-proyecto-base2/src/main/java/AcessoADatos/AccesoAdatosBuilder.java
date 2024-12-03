package AcessoADatos;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.util.List;

public class AccesoAdatosBuilder {

    // Columnas de la tabla con sus tipos correctos
    @FXML
    private TableColumn<Empleado, Integer> idEmpleado;
    @FXML
    private TableColumn<Empleado, String> NombreEmpleado;
    @FXML
    private TableColumn<Empleado, String> ApellidosEmpleado;
    @FXML
    private TableColumn<Empleado, String> DepartamentoEmpleado;
    @FXML
    private TableColumn<Empleado, Double> SueldoEmpleado;

    // Botones
    @FXML
    private Button btnActulizar;
    @FXML
    private Button btnBorrar;
    @FXML
    private Button btnExportarJSON;
    @FXML
    private Button btnExportarXML;
    @FXML
    private Button btnImportarXML;
    @FXML
    private Button btnInsertar;

    // Campos de texto
    @FXML
    private TextField campoApellidos;
    @FXML
    private TextField campoDepartamento;
    @FXML
    private TextField campoNombre;
    @FXML
    private TextField campoSueldo;
    @FXML
    private Label infoLabel;

    @FXML
    private TableView<Empleado> tablaEmpleados;

    private ObservableList<Empleado> empleados;
    private Configuracion configuracion;

    public void initialize() {
        configuracion = new Configuracion();
        cargarDatosBinarios();

        idEmpleado.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        NombreEmpleado.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        ApellidosEmpleado.setCellValueFactory(cellData -> cellData.getValue().apellidosProperty());
        DepartamentoEmpleado.setCellValueFactory(cellData -> cellData.getValue().departamentoProperty());
        SueldoEmpleado.setCellValueFactory(cellData -> cellData.getValue().sueldoProperty().asObject());

        empleados = FXCollections.observableArrayList();
        tablaEmpleados.setItems(empleados);
    }

    @FXML
    void insertar(ActionEvent event) {
        String nombre = campoNombre.getText();
        String apellidos = campoApellidos.getText();
        String departamento = campoDepartamento.getText();
        String sueldoText = campoSueldo.getText();

        if (nombre.isEmpty() || apellidos.isEmpty() || departamento.isEmpty() || sueldoText.isEmpty()) {
            infoLabel.setText("Todos los campos son obligatorios.");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Todos los campos deben ser llenados.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        double sueldo;
        try {
            sueldo = Double.parseDouble(sueldoText);
            if (sueldo <= 0) {
                infoLabel.setText("El sueldo debe ser un número positivo.");
                Alert alert = new Alert(Alert.AlertType.ERROR, "El sueldo debe ser un número positivo.", ButtonType.OK);
                alert.showAndWait();
                return;
            }
        } catch (NumberFormatException e) {
            infoLabel.setText("El sueldo debe ser un número válido.");
            Alert alert = new Alert(Alert.AlertType.ERROR, "El sueldo debe ser un número válido.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        int idEmpleado = configuracion.obtenerUltimoIdEmpleado() + 1;
        Empleado nuevoEmpleado = new Empleado(idEmpleado, nombre, apellidos, departamento, sueldo);
        empleados.add(nuevoEmpleado);
        configuracion.actualizarUltimoIdEmpleado(idEmpleado);
        guardarDatosBinarios();

        campoNombre.clear();
        campoApellidos.clear();
        campoDepartamento.clear();
        campoSueldo.clear();
        infoLabel.setText("");
    }

    @FXML
    void Actualizar(ActionEvent event) {
        Empleado empleadoSeleccionado = tablaEmpleados.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado != null) {
            String nombre = campoNombre.getText();
            String apellidos = campoApellidos.getText();
            String departamento = campoDepartamento.getText();
            String sueldoText = campoSueldo.getText();

            if (nombre.isEmpty() || apellidos.isEmpty() || departamento.isEmpty() || sueldoText.isEmpty()) {
                infoLabel.setText("Todos los campos son obligatorios.");
                Alert alert = new Alert(Alert.AlertType.ERROR, "Todos los campos deben ser llenados.", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            double sueldo;
            try {
                sueldo = Double.parseDouble(sueldoText);
                if (sueldo <= 0) {
                    infoLabel.setText("El sueldo debe ser un número positivo.");
                    Alert alert = new Alert(Alert.AlertType.ERROR, "El sueldo debe ser un número positivo.", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
            } catch (NumberFormatException e) {
                infoLabel.setText("El sueldo debe ser un número válido.");
                Alert alert = new Alert(Alert.AlertType.ERROR, "El sueldo debe ser un número válido.", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            empleadoSeleccionado.setNombre(nombre);
            empleadoSeleccionado.setApellidos(apellidos);
            empleadoSeleccionado.setDepartamento(departamento);
            empleadoSeleccionado.setSueldo(sueldo);

            campoNombre.clear();
            campoApellidos.clear();
            campoDepartamento.clear();
            campoSueldo.clear();
            infoLabel.setText("");

            tablaEmpleados.refresh();
            guardarDatosBinarios();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecciona un empleado para actualizar", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    void borrar(ActionEvent event) {
        Empleado empleadoSeleccionado = tablaEmpleados.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado != null) {
            empleados.remove(empleadoSeleccionado);
            guardarDatosBinarios();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecciona un empleado para borrar", ButtonType.OK);
            alert.showAndWait();
        }
    }
    
    @FXML
    void EmpleadoTabla(MouseEvent event) {
        // Obtener el empleado seleccionado en la tabla
        Empleado empleadoSeleccionado = tablaEmpleados.getSelectionModel().getSelectedItem();

        // Si un empleado ha sido seleccionado, llenar los campos de texto con los datos del empleado
        if (empleadoSeleccionado != null) {
            campoNombre.setText(empleadoSeleccionado.getNombre());
            campoApellidos.setText(empleadoSeleccionado.getApellidos());
            campoDepartamento.setText(empleadoSeleccionado.getDepartamento());
            campoSueldo.setText(String.valueOf(empleadoSeleccionado.getSueldo()));
        }
    }


    // Exportar datos a XML
    @FXML
    void exportarXML(ActionEvent event) {
        try {
            // Convertir ObservableList a List
            List<Empleado> listaEmpleados = empleados;

            // Crear un contenedor con la lista de empleados
            EmpleadoListWrapper wrapper = new EmpleadoListWrapper(listaEmpleados);

            // Configurar JAXB para convertir la lista en XML
            JAXBContext context = JAXBContext.newInstance(EmpleadoListWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            // Escribir el XML a un archivo
            try (FileOutputStream fileOutputStream = new FileOutputStream(configuracion.obtenerRutaXML())) {
                marshaller.marshal(wrapper, fileOutputStream);
            }

            infoLabel.setText("Datos exportados a XML correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            infoLabel.setText("Error al exportar los datos a XML.");
        }
    }


    // Exportar datos a JSON
    @FXML
    void exportarJSON(ActionEvent event) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File archivoJSON = new File("empleados.json");
            objectMapper.writeValue(archivoJSON, empleados);

            infoLabel.setText("Datos exportados a JSON con éxito.");
        } catch (IOException e) {
            infoLabel.setText("Error al exportar los datos a JSON.");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ocurrió un error al exportar los datos a JSON.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    // Importar datos desde XML
    @FXML
    void importarXML(ActionEvent event) {
        try {
            // Configurar JAXB para leer el XML
            JAXBContext context = JAXBContext.newInstance(EmpleadoListWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // Leer el XML desde un archivo
            File file = new File(configuracion.obtenerRutaXML());
            if (file.exists()) {
                EmpleadoListWrapper wrapper = (EmpleadoListWrapper) unmarshaller.unmarshal(file);
                empleados.clear();
                empleados.addAll(wrapper.getEmpleados());
                tablaEmpleados.setItems(empleados);

                infoLabel.setText("Datos importados desde XML correctamente.");
            } else {
                infoLabel.setText("El archivo XML no existe.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            infoLabel.setText("Error al importar los datos desde XML.");
        }
    }


    // Importar datos desde JSON
    @FXML
    void importarJSON(ActionEvent event) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File archivoJSON = new File("empleados.json");
            if (archivoJSON.exists()) {
                List<Empleado> empleadosImportados = objectMapper.readValue(archivoJSON, objectMapper.getTypeFactory().constructCollectionType(List.class, Empleado.class));
                empleados.clear();
                empleados.addAll(empleadosImportados);

                tablaEmpleados.setItems(empleados);
                infoLabel.setText("Datos importados desde JSON con éxito.");
            } else {
                infoLabel.setText("No se encontró el archivo JSON.");
                Alert alert = new Alert(Alert.AlertType.WARNING, "No se encontró el archivo JSON para importar.", ButtonType.OK);
                alert.showAndWait();
            }
        } catch (IOException e) {
            System.err.println(e);
            infoLabel.setText("Error al importar los datos desde JSON.");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ocurrió un error al importar los datos desde JSON.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    // Guardar los datos en binario
    private void guardarDatosBinarios() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("empleados.dat"))) {
            out.writeObject(empleados);
        } catch (IOException e) {
            infoLabel.setText("Error al guardar los datos.");
        }
    }

    // Cargar los datos en binario
    private void cargarDatosBinarios() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("empleados.dat"))) {
            empleados = (ObservableList<Empleado>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            empleados = FXCollections.observableArrayList();
        }
    }
}

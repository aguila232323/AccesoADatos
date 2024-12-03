/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AcessoADatos;

/**
 *
 * @author Prados
 */
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class EmpleadoListWrapper {

    private List<Empleado> empleados;

    public EmpleadoListWrapper() {
    }

    public EmpleadoListWrapper(List<Empleado> empleados) {
        this.empleados = empleados;
    }

    @XmlElement(name = "empleado")
    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }
}


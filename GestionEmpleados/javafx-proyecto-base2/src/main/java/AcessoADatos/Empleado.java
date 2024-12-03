/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AcessoADatos;


import javafx.beans.property.*;

import java.io.Serializable;

import javafx.beans.property.*;

import javafx.beans.property.*;

import java.io.Serializable;

public class Empleado implements Serializable {

    private final IntegerProperty id;
    private final StringProperty nombre;
    private final StringProperty apellidos;
    private final StringProperty departamento;
    private final DoubleProperty sueldo;

    public Empleado(int id, String nombre, String apellidos, String departamento, double sueldo) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.apellidos = new SimpleStringProperty(apellidos);
        this.departamento = new SimpleStringProperty(departamento);
        this.sueldo = new SimpleDoubleProperty(sueldo);
    }
     public Empleado() {
        this.id = new SimpleIntegerProperty();
        this.nombre = new SimpleStringProperty();
        this.apellidos = new SimpleStringProperty();
        this.departamento = new SimpleStringProperty();
        this.sueldo = new SimpleDoubleProperty();
    }

  
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos.get();
    }

    public void setApellidos(String apellidos) {
        this.apellidos.set(apellidos);
    }

    public StringProperty apellidosProperty() {
        return apellidos;
    }

    public String getDepartamento() {
        return departamento.get();
    }

    public void setDepartamento(String departamento) {
        this.departamento.set(departamento);
    }

    public StringProperty departamentoProperty() {
        return departamento;
    }

    public double getSueldo() {
        return sueldo.get();
    }

    public void setSueldo(double sueldo) {
        this.sueldo.set(sueldo);
    }

    public DoubleProperty sueldoProperty() {
        return sueldo;
    }
}



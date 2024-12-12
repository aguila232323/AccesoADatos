
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement
public class Curso {
    @XmlElement
    private String nombre;

    @XmlElementWrapper(name = "estudiantes") // Envuelve la lista en una etiqueta "estudiantes" en el XML
    @XmlElement(name = "estudiante") // Cada elemento dentro será etiquetado como "estudiante"
    private List<Estudiante> estudiantes;

    // Constructor sin parámetros
    public Curso() {
    }

    // Constructor con parámetros
    public Curso(String nombre, List<Estudiante> estudiantes) {
        this.nombre = nombre;
        this.estudiantes = estudiantes;
    }

    // Getter para nombre
    public String getNombre() {
        return nombre;
    }

    // Setter para nombre
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Getter para estudiantes
    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    // Setter para estudiantes
    public void setEstudiantes(List<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }

    @Override
    public String toString() {
        return "Curso{" +
                "nombre='" + nombre + '\'' +
                ", estudiantes=" + estudiantes +
                '}';
    }
}


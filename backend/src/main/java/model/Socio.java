package model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "socio")
@NamedQuery(name = "Socio.findAll", query = "SELECT s FROM Socio s")
public class Socio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String dni;

    @Column(nullable = false)
    private String nombre;

    public Socio() {}

    public Socio(String dni, String nombre) {
        this.dni = dni;
        this.nombre = nombre;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() {
        return "ID: " + id + " | DNI: " + dni + " | Nombre: " + nombre;
    }
}

package model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "pelicula")
@NamedQueries({
        @NamedQuery(name = "Pelicula.findAll", query = "SELECT p FROM Pelicula p"),
        @NamedQuery(name = "Pelicula.findByAnio", query = "SELECT p FROM Pelicula p WHERE p.anio = :anio"),
        @NamedQuery(name = "Pelicula.findByValoracionSuperior", query = "SELECT p FROM Pelicula p WHERE p.valoracion >= :valoracion")
})
public class Pelicula implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String director;

    @Column(name = "anio", nullable = false)
    private int anio;

    @Column(nullable = false)
    private int valoracion;

    public Pelicula() {}

    public Pelicula(String titulo, String director, int anio, int valoracion) {
        this.titulo = titulo;
        this.director = director;
        this.anio = anio;
        this.valoracion = valoracion;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }
    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }
    public int getValoracion() { return valoracion; }
    public void setValoracion(int valoracion) { this.valoracion = valoracion; }

    @Override
    public String toString() {
        return String.format("ID: %-4d | Título: %-25s | Director: %-20s | Año: %-4d | Valoración: %d/10",
                id, titulo, director, anio, valoracion);
    }
}
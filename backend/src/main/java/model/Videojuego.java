package model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "videojuego")
@NamedQueries({
        @NamedQuery(name = "Videojuego.findAll", query = "SELECT v FROM Videojuego v")
})
public class Videojuego implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String plataforma;

    @Column(nullable = false)
    private int anio;

    @Column(nullable = false)
    private int valoracion;

    public Videojuego() {}

    public Videojuego(String titulo, String plataforma, int anio, int valoracion) {
        this.titulo = titulo;
        this.plataforma = plataforma;
        this.anio = anio;
        this.valoracion = valoracion;
    }

    // --- GETTERS Y SETTERS COMPLETOS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }

    @Override
    public String toString() {
        return String.format("ID: %-4d | %-25s | %-15s | %-4d | %d/10",
                id, titulo, plataforma, anio, valoracion);
    }
}
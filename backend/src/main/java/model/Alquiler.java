package model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "alquiler")
public class Alquiler implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Película (puede ser null si es un videojuego)
    @ManyToOne
    @JoinColumn(name = "id_pelicula")
    private Pelicula pelicula;

    // Relación con Videojuego (puede ser null si es una película)
    @ManyToOne
    @JoinColumn(name = "id_videojuego")
    private Videojuego videojuego;

    @ManyToOne
    @JoinColumn(name = "id_socio", nullable = false)
    private Socio socio;

    private LocalDate fechaAlquiler;
    private LocalDate fechaDevolucion;

    // Constructor por defecto (JPA)
    public Alquiler() {
        this.fechaAlquiler = LocalDate.now();
    }

    // Constructor para Películas
    public Alquiler(Pelicula pelicula, Socio socio) {
        this();
        this.pelicula = pelicula;
        this.socio = socio;
        this.fechaDevolucion = this.fechaAlquiler.plusDays(3); // Plazo de 3 días
    }

    // Constructor para Videojuegos
    public Alquiler(Videojuego videojuego, Socio socio) {
        this();
        this.videojuego = videojuego;
        this.socio = socio;
        this.fechaDevolucion = this.fechaAlquiler.plusDays(3); // Plazo de 3 días
    }

    // --- GETTERS Y SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Pelicula getPelicula() { return pelicula; }
    public void setPelicula(Pelicula pelicula) { this.pelicula = pelicula; }

    public Videojuego getVideojuego() { return videojuego; }
    public void setVideojuego(Videojuego videojuego) { this.videojuego = videojuego; }

    public Socio getSocio() { return socio; }
    public void setSocio(Socio socio) { this.socio = socio; }

    public LocalDate getFechaAlquiler() { return fechaAlquiler; }
    public void setFechaAlquiler(LocalDate fechaAlquiler) { this.fechaAlquiler = fechaAlquiler; }

    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }
    @Override
    public String toString() {
        // Comprueba si es una peli o un juego para mostrar el título correcto
        String itemAlquilado = (pelicula != null) 
                ? "Película: " + pelicula.getTitulo() 
                : "Videojuego: " + videojuego.getTitulo();

        return String.format("Alquiler #%-3d | %-25s | Socio: %-15s | Fecha: %s | Devolución: %s",
                id, itemAlquilado, socio.getNombre(), fechaAlquiler, fechaDevolucion);
    }
}

package dao;

import jakarta.persistence.*;
import model.Pelicula;
import model.Socio;
import model.Alquiler;
import model.Videojuego;
import java.util.List;
import java.util.function.Consumer;

/**
 * Clase DAO (Data Access Object) encargada de la persistencia
 * de datos para el sistema del Videoclub.
 * Centraliza las operaciones CRUD para Películas, Videojuegos, Socios y Alquileres.
 *
 * @author Adrián Martínez
 * @version 1.2
 */
public class PeliculaDAO {

    /** Fábrica de gestores de entidades configurada según la unidad de persistencia definida en persistence.xml. */
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("retrovideoPU");

    // --- MÉTODOS PARA PELÍCULAS ---

    public void guardar(Pelicula p) {
        ejecutarTransaccion(em -> em.persist(p));
    }

    public void modificar(Pelicula p) {
        ejecutarTransaccion(em -> em.merge(p));
    }

    public Pelicula buscarPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Pelicula.class, id);
        } finally {
            em.close();
        }
    }


    public void eliminarPelicula(Long id) {
        ejecutarTransaccion(em -> {
            Pelicula p = em.find(Pelicula.class, id);
            if (p != null) {
                em.remove(p);
            }
        });
    }

    public List<Pelicula> listarTodas() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createNamedQuery("Pelicula.findAll", Pelicula.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Pelicula> listarPorAnio(int anio) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createNamedQuery("Pelicula.findByAnio", Pelicula.class)
                    .setParameter("anio", anio)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Pelicula> consultaPersonalizada(int valoracion) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createNamedQuery("Pelicula.findByValoracionSuperior", Pelicula.class)
                    .setParameter("valoracion", valoracion)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // --- MÉTODOS PARA VIDEOJUEGOS ---

    public void guardarVideojuego(Videojuego v) {
        ejecutarTransaccion(em -> em.persist(v));
    }

    public Videojuego buscarVideojuegoPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Videojuego.class, id);
        } finally {
            em.close();
        }
    }

    public List<Videojuego> listarVideojuegos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createNamedQuery("Videojuego.findAll", Videojuego.class).getResultList();
        } finally {
            em.close();
        }
    }

    // CORRECCIÓN: Añadido el método que faltaba para borrar videojuegos
    public void eliminarVideojuego(Long id) {
        ejecutarTransaccion(em -> {
            Videojuego v = em.find(Videojuego.class, id);
            if (v != null) {
                em.remove(v);
            }
        });
    }

    // --- MÉTODOS PARA SOCIOS ---

    public void guardarSocio(Socio s) {
        ejecutarTransaccion(em -> em.persist(s));
    }

    public Socio buscarSocioPorDni(String dni) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT s FROM Socio s WHERE s.dni = :dni", Socio.class)
                    .setParameter("dni", dni)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<Socio> listarSocios() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createNamedQuery("Socio.findAll", Socio.class).getResultList();
        } finally {
            em.close();
        }
    }

    // --- MÉTODOS PARA ALQUILERES (RESERVAS e INFORMES) ---

    public void registrarAlquiler(Alquiler a) {
        ejecutarTransaccion(em -> em.persist(a));
    }

    /**
     * Busca un alquiler por su identificador único.
     */
    public Alquiler buscarAlquilerPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Alquiler.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Lista todos los alquileres activos (tanto de películas como de videojuegos).
     */
    public List<Alquiler> listarTodosAlquileres() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT a FROM Alquiler a", Alquiler.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Procesa la devolución de un alquiler eliminando el préstamo activo del registro.
     */
    public boolean devolverAlquiler(Long id) {
        Alquiler existente = buscarAlquilerPorId(id);
        if (existente == null) {
            return false;
        }
        ejecutarTransaccion(em -> {
            Alquiler a = em.find(Alquiler.class, id);
            if (a != null) {
                em.remove(a);
            }
        });
        return true;
    }

    /**
     * Recupera todos los alquileres vinculados a una película.
     * Útil para ver quién tiene qué película alquilada y su DNI.
     */
    public List<Alquiler> listarAlquileresPeliculas() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT a FROM Alquiler a WHERE a.pelicula IS NOT NULL", Alquiler.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Recupera todos los alquileres vinculados a un videojuego.
     */
    public List<Alquiler> listarAlquileresVideojuegos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT a FROM Alquiler a WHERE a.videojuego IS NOT NULL", Alquiler.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Método auxiliar genérico para gestionar el ciclo de vida de las transacciones.
     */
    private void ejecutarTransaccion(Consumer<EntityManager> accion) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            accion.accept(em);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Error en la operación de persistencia: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public List<Videojuego> listarVideojuegosPorAnio(int anio) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT v FROM Videojuego v WHERE v.anio = :anio", Videojuego.class)
                    .setParameter("anio", anio)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Videojuego> consultaPersonalizadaVideojuego(int valoracion) {
        EntityManager em = emf.createEntityManager();
        try {
            // Asumo que en tu modelo tienes un atributo 'valoracion'
            return em.createQuery("SELECT v FROM Videojuego v WHERE v.valoracion >= :valoracion", Videojuego.class)
                    .setParameter("valoracion", valoracion)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void modificarVideojuego(Videojuego v) {
        ejecutarTransaccion(em -> em.merge(v));
    }
}